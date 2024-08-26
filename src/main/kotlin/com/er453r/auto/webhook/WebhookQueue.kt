package com.er453r.auto.webhook

import com.er453r.auto.configuration.Configuration
import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.stages.BuildStage
import com.er453r.auto.pipeline.stages.CheckoutStage
import com.er453r.auto.pipeline.stages.PublishStage
import com.er453r.auto.queue.Queue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory

@Component
class WebhookQueue(
    val configuration: Configuration,
    val parsers: MutableList<out WebhookParser>,
    private val environment: Environment,
) : Queue<WebhookData>(type = WebhookData::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: WebhookData) {
        logger.info { "Handling webhook data: $item" }

        val checkoutInfo = parsers.first { it.match(item.data) }.parse(item.data)

        val pipeline = Pipeline(
            stages = listOf(
                CheckoutStage(),
                BuildStage(),
                PublishStage(),
            )
        )

        val tempDir = createTempDirectory()

        val environment = mutableMapOf(
            "REPO" to checkoutInfo.repo,
            "REV" to checkoutInfo.ref,
            "MIRROR_PATH" to configuration.mirrorPath,
            "WORKDIR" to tempDir.absolutePathString(),
            "DOCKER_REPO" to configuration.dockerRepo,
        )

        pipeline.run(environment)

        tempDir.toFile().deleteRecursively()

        logger.info { "Completed webhook data: $item" }
    }
}
