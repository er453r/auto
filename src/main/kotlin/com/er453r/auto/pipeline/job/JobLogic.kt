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
        workdir: String? = null,
    ) {
        logger.info { "Running job $job" }

        job.status = Job.Status.RUNNING

        nextStep(job, job.env, listOf())
    }

    fun nextStep(job: Job, env: Map<String, String>, previousSeps: List<String>):Boolean{
        // first - find first image that env satisfies all needed inputs (alphabetical, so we won't have randomness)
        imageRepository.findAll()
            .map { DockerUtils.imageInfo(it.name) }
            .filter { env.keys.containsAll(it.inputs) }
            .filter { it.name !in previousSeps }
            .minByOrNull { it.name }
            ?.let { image ->
                logger.info { "Matched image: $image" }

                val step = Step(
                    job  = job,
                    image = image.name,
                    env = env,
                )

                stepRepository.save(step)

                stepLogic.run(
                    step = step,
                    workdir = null,
                    onCompleted = {
                        nextStep(job, it, previousSeps + step.image)
                    },
                    onError = {
                        nextStep(job, it, previousSeps + step.image)
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
