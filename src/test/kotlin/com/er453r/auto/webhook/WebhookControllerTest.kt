package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class WebhookControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `handleNotification should log message`() {
        val name = "test"
        val jsonNode = JsonNodeFactory.instance.objectNode().put("key", "value")

        mockMvc
            .perform(
                post("/webhooks/$name")
                    .content(jsonNode.toString())
                    .contentType("application/json")
            )
            .andExpect(
                status().isOk
            )
    }
}
