package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController {
    private val logger = KotlinLogging.logger {}

    @PostMapping("webhook")
    fun handleNotification(json: JsonNode) {
        logger.info { "Handling notification: $json" }
    }
}
