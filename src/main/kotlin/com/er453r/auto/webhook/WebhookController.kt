package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController {
    private val logger = KotlinLogging.logger {}

    @PostMapping("webhook/{name}")
    fun handleNotification(@PathVariable name:String, @RequestBody json: JsonNode) {
        logger.info { "Handling notification '$name': $json" }
    }
}
