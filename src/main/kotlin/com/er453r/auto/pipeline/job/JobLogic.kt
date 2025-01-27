package com.er453r.auto.pipeline.job

import com.er453r.auto.docker.DockerUtils
import com.er453r.auto.image.ImageRepository
import com.er453r.auto.pipeline.step.Step
import com.er453r.auto.pipeline.step.StepLogic
import com.er453r.auto.pipeline.step.StepRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class JobLogic(
    val jobRepository: JobRepository,
    val imageRepository: ImageRepository,
    val stepRepository: StepRepository,
    val stepLogic: StepLogic,
) {
    private val logger = KotlinLogging.logger {}

    fun run(
        job: Job,
    ) {
        logger.info { "Running job $job" }

        val workdir = "job-${job.id}-workdir"

        DockerUtils.createVolume(workdir)

        job.status = Job.Status.RUNNING

        nextStep(job, job.env, listOf(), workdir)
    }

    fun nextStep(job: Job, env: Map<String, String>, previousSeps: List<String>, workdir:String):Boolean{
        // first - find first image that env satisfies all needed inputs (alphabetical, so we won't have randomness)
        imageRepository.findAll()
            .map { DockerUtils.imageInfo(it.name) }
            .filter { env.keys.containsAll(it.inputs) }
            .filter { it.image !in previousSeps }
            .minByOrNull { it.name }
            ?.let { image ->
                logger.info { "Matched image: $image" }

                val step = Step(
                    job  = job,
                    image = image.image,
                    env = env,
                )

                stepRepository.save(step)

                val volumes = mapOf(
                    "/var/run/docker.sock" to "/var/run/docker.sock",
                    workdir to "/workdir",
                )

                stepLogic.run(
                    step = step,
                    volumes = volumes,
                    onCompleted = {
                        nextStep(job, it, previousSeps + step.image, workdir)
                    },
                    onError = {
                        nextStep(job, it, previousSeps + step.image, workdir)
                    },
                )

                return true
            }

        logger.info { "No matching image found!" }

        job.status = Job.Status.DONE
        jobRepository.save(job)

        return false
    }
}
