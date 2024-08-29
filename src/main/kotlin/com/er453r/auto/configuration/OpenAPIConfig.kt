package com.er453r.auto.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "AUTO API",
        version = "v1",
        description = "AUTO API",
    ),
)
class OpenAPIConfig
