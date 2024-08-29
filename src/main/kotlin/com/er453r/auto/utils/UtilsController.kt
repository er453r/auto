package com.er453r.auto.utils

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("utils")
class UtilsController {
    @GetMapping("healthcheck")
    fun healthCheck() = mapOf("status" to "OK")
}
