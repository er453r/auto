package com.er453r.auto.pipeline

import com.er453r.auto.checkout.CheckoutParser
import com.er453r.auto.configuration.Configuration
import com.er453r.auto.pipeline.stages.BuildStage
import com.er453r.auto.pipeline.stages.CheckoutStage
import com.er453r.auto.pipeline.stages.PublishStage
import com.er453r.auto.queue.Queue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory

@Component
class PipelineQueue(
    val configuration: Configuration,
    val parsers: MutableList<CheckoutParser>,
    val pipelineRepository: PipelineRepository,
    val objectMapper: ObjectMapper,
) : Queue<PipelineQueueItem>(type = PipelineQueueItem::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: PipelineQueueItem) {
        val pipeline = pipelineRepository.getReferenceById(item.pipelineId)

        logger.info { "Starting new pipeline: $item" }

        val checkoutInfo = parsers.first { it.match(pipeline.data) }.parse(pipeline.data)

        val workDir = createTempDirectory()

        val environment = mutableMapOf(
            "REPO" to checkoutInfo.repo,
            "REV" to checkoutInfo.ref,
            "MIRROR_PATH" to configuration.mirrorPath,
            "WORKDIR" to workDir.absolutePathString(),
            "DOCKER_REPO" to configuration.dockerRepo,
        )

        listOf(
            CheckoutStage(),
            BuildStage(),
            PublishStage(),
        ).forEach {
            val result = it.run(environment)

            pipeline.log += result

            environment.putAll(result.env)
        }

        workDir.toFile().deleteRecursively()

        logger.info { objectMapper.valueToTree<JsonNode?>(pipeline).toPrettyString() }

        logger.info { "Completed webhook data: $item" }
    }
}
