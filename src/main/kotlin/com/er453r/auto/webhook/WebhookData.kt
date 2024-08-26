package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode

data class WebhookData(
    val name: String,
    val data: JsonNode,
)
