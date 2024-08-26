package com.er453r.auto.webhook

import com.fasterxml.jackson.databind.JsonNode

interface WebhookParser {
    fun match(json: JsonNode): Boolean
    fun parse(json: JsonNode): WebhookCheckoutInfo
}
