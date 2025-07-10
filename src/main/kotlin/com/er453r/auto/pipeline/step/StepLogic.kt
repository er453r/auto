package com.er453r.auto.pipeline.step

import com.er453r.auto.docker.DockerUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class StepLogic(
    val stepRepository: StepRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun run(
        step: Step,
        volumes: Map<String, String> = emptyMap(),
        onCompleted: (Map<String, String>) -> Unit,
        onError: (Map<String, String>) -> Unit,
    ) {
        logger.info { "Running step $step" }

        step.log = mutableListOf()

        DockerUtils.start(
            image = step.image,
            volumes = volumes,
            env = step.env,
            onLine = { line, isError ->
                step.log?.add(StepLogLine(line, isError))
                step.status = Step.Status.RUNNING

                stepRepository.save(step)
            },
            onCompleted = { env ->
                step.result = env
                step.status = Step.Status.DONE

                stepRepository.save(step)

                onCompleted(env)
            },
            onError = { env ->
                logger.warn { "Step $step failed!" }

                step.result = env
                step.status = Step.Status.ERROR

                stepRepository.save(step)

                onError(env)
            }
        )
    }
}
