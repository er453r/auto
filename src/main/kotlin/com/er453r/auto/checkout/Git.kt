package com.er453r.auto.checkout

import com.er453r.auto.utils.runScript
import com.er453r.auto.webhook.WebhookInfo
import io.github.oshai.kotlinlogging.KotlinLogging

class Git {
    private val logger = KotlinLogging.logger {}

    private val checkoutScript = ClassLoader.getSystemResource("scripts/checkout.sh").readText()

    fun clone(webhookInfo: WebhookInfo) {
        val result = runScript(
            script = checkoutScript,
            env = mapOf(
                "REPO" to webhookInfo.repo,
                "REF" to webhookInfo.ref,
            )
        )

        logger.info { "Part 1: $result" }
    }
}
