package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
    val webhookQueue: WebhookQueue,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("webhook/{name}")
    fun handleNotification(@PathVariable name: String, @RequestBody json: JsonNode) {
        WebhookData(name, json)
            .also { logger.info { "Handling webhook notification $it" } }
            .let { webhookQueue.add(it) }
    }
}
