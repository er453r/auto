package com.er453r.auto.webhook

import com.er453r.auto.queue.QueueHandler
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class WebhookBuildTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var queueHandler: QueueHandler

    @Test
    fun `Webhook Build Test`() {
        val name = "test"
        val jsonNode: JsonNode = ObjectMapper().readValue(ClassLoader.getSystemResource("webhook-github.json"), JsonNode::class.java)

        mockMvc
            .perform(
                post("/webhooks/$name")
                    .content(jsonNode.toString())
                    .contentType("application/json")
            )
            .andExpect(
                status().isOk
            )

        queueHandler.handle()
    }
}
