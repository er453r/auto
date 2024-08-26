package com.er453r.auto.webhook

import com.er453r.auto.configuration.Configuration
import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.stages.BuildStage
import com.er453r.auto.pipeline.stages.CheckoutStage
import com.er453r.auto.pipeline.stages.PublishStage
import com.er453r.auto.queue.Queue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory


@Component
class WebhookQueue(
    val configuration: Configuration,
    val parsers: MutableList<out WebhookParser>,
) : Queue<WebhookData>(type = WebhookData::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: WebhookData) {
        logger.info { "Handling webhook data: $item" }

        val checkoutInfo = parsers.first { it.match(item.data) }.parse(item.data)

        val pipeline = Pipeline()

        val tempDir = createTempDirectory()

        val environment = mutableMapOf(
            "REPO" to checkoutInfo.repo,
            "REV" to checkoutInfo.ref,
            "MIRROR_PATH" to configuration.mirrorPath,
            "WORKDIR" to tempDir.absolutePathString(),
            "DOCKER_REPO" to configuration.dockerRepo,
        )

        pipeline.run(
            listOf(
                CheckoutStage(),
                BuildStage(),
                PublishStage(),
            ), environment
        )

        tempDir.toFile().deleteRecursively()

        logger.info { ObjectMapper().registerModule(JavaTimeModule()).valueToTree<JsonNode?>(pipeline).toPrettyString() }

        logger.info { "Completed webhook data: $item" }
    }
}
