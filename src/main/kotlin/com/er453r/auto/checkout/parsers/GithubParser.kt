package com.er453r.auto.checkout.parsers

import com.er453r.auto.checkout.CheckoutParser
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component

@Component
class GithubParser : CheckoutParser {
    override fun match(json: JsonNode) = json.toString().contains("github.com")

    override fun parse(json: JsonNode) = WebhookCheckoutInfo(
        repo = json.at("/repository/ssh_url").textValue(),
        ref = json.at("/after").textValue(),
    )
}
