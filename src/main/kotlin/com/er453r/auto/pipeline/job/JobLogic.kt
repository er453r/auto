package com.er453r.auto.pipeline.job

import com.er453r.auto.docker.DockerUtils
import com.er453r.auto.image.ImageLogic
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
    val imageLogic: ImageLogic,
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
        // first - find the first image that env satisfies all necessary inputs (alphabetical, so we won't have randomness)
        imageRepository.findAll()
            .map { Pair(it, DockerUtils.imageInfo(it.name)) }
            .filter { (_, info) -> env.keys.containsAll(info.inputs) }
            .filter { (_, info) -> info.image !in previousSeps }
            .minByOrNull { (_, info) -> info.image }
            ?.let { (image, info) ->
                logger.info { "Matched image: $info" }

                val step = Step(
                    job  = job,
                    image = info.image,
                    env = env,
                )

                stepRepository.save(step)

                val volumes = mutableMapOf(
                    workdir to "/workdir",
                )

                if(info.docker) {
                    logger.info { "Attaching docker socket to image ${info.image}" }

                    volumes += "/var/run/docker.sock" to "/var/run/docker.sock"
                }

                if(info.storage) {
                    val volume = imageLogic.getStorage(image)

                    logger.info { "Attaching storage volume $volume to image ${info.image}" }

                    volumes += volume to "/storage"
                }

                stepLogic.run(
                    step = step,
                    volumes = volumes,
                    onCompleted = {
                        nextStep(job, it, previousSeps + step.image, workdir)
                    },
                    onError = {
                        logger.warn { "Step $step failed!" }

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
