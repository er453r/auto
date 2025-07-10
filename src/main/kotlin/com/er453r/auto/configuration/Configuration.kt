package com.er453r.auto.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auto")
data class Configuration(
    val mirrorPath: String,
)