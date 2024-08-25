package com.er453r.auto.checkout

import com.er453r.auto.configuration.Configuration
import com.er453r.auto.utils.runScript
import com.er453r.auto.webhook.WebhookInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

@Component
class Git(
    val configuration: Configuration,
) {
    private val logger = KotlinLogging.logger {}

    private val checkoutScript = ClassLoader.getSystemResource("scripts/checkout.sh").readText()
    private val buildScript = ClassLoader.getSystemResource("scripts/build.sh").readText()
    private val publishScript = ClassLoader.getSystemResource("scripts/publish.sh").readText()

    fun clone(webhookInfo: WebhookInfo) {
        logger.info { "Checkout..." }

        val tempDir = createTempDirectory()

        val result = runScript(
            script = checkoutScript,
            env = mapOf(
                "REPO" to webhookInfo.repo,
                "REV" to webhookInfo.ref,
                "MIRROR_PATH" to configuration.mirrorPath,
                "WORKDIR" to tempDir.absolutePathString(),
            )
        )

        result.lines.forEach { logger.info { it } }

        val env = build(webhookInfo, tempDir)

        publish(webhookInfo, env)
    }

    @OptIn(ExperimentalPathApi::class)
    fun build(webhookInfo: WebhookInfo, workdir: Path):Map<String, String> {
        logger.info { "Build..." }

        val result = runScript(
            script = buildScript,
            env = mapOf(
                "WORKDIR" to workdir.absolutePathString(),
                "REPO" to webhookInfo.repo,
                "DOCKER_REPO" to configuration.dockerRepo,
            )
        )

        workdir.deleteRecursively()

        logger.info { result.env }

        result.lines.forEach { logger.info { it } }

        return result.env
    }

    fun publish(webhookInfo: WebhookInfo, env:Map<String, String>) {
        logger.info { "Publish..." }

        val result = runScript(
            script = publishScript,
            env = env +  mapOf(
                "DOCKER_REPO" to configuration.dockerRepo,
            )
        )

        result.lines.forEach { logger.info { it } }
    }
}
