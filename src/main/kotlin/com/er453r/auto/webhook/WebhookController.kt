package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
    val objectMapper: ObjectMapper,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("webhooks/{name}")
    fun handleNotification(@PathVariable name: String, @RequestBody json: String) {
        logger.info { "New webhook notification: $name" }

        objectMapper.readValue(json, JsonNode::class.java)
    }
}
