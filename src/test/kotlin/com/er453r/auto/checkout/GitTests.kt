package com.er453r.auto.webhook

import com.er453r.auto.checkout.Git
import com.er453r.auto.webhook.parsers.GithubParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GitTests {
    private val logger = KotlinLogging.logger {}

    @Test
    fun `Git Test`() {
        val webhookJson: JsonNode = ObjectMapper().readValue(ClassLoader.getSystemResource("webhook-github.json"), JsonNode::class.java)

        logger.info { "Webhook: $webhookJson" }

        logger.info { GithubParser().parse(webhookJson) }

        Git().clone(webhookInfo = GithubParser().parse(webhookJson))
    }
}
