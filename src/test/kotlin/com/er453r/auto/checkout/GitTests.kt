package com.er453r.auto.checkout

import com.er453r.auto.webhook.parsers.GithubParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GitTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var git: Git

    @Test
    fun `Git Test`() {
        val webhookJson: JsonNode = ObjectMapper().readValue(ClassLoader.getSystemResource("webhook-github.json"), JsonNode::class.java)

        logger.info { "Webhook: $webhookJson" }

        logger.info { GithubParser().parse(webhookJson) }

        git.clone(webhookInfo = GithubParser().parse(webhookJson))
    }
}
