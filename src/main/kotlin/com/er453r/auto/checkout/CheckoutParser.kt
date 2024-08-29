package com.er453r.auto.checkout

import com.er453r.auto.checkout.parsers.WebhookCheckoutInfo
import com.fasterxml.jackson.databind.JsonNode

interface CheckoutParser {
    fun match(json: JsonNode): Boolean
    fun parse(json: JsonNode): WebhookCheckoutInfo
}
