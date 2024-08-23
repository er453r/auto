package com.er453r.auto.webhook.parsers

import com.er453r.auto.webhook.WebhookInfo
import com.fasterxml.jackson.databind.JsonNode

class GithubParser {
    fun parse(json: JsonNode) = WebhookInfo(
        repo = json.at("/repository/ssh_url").textValue(),
        ref = json.at("/after").textValue(),
    )
}
