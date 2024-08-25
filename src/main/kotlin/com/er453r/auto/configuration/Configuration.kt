package com.er453r.auto.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auto")
data class Configuration(
    val queue: Queue = Queue(),
){
    data class Queue(
        val delay: Long = 5000,
        val interval: Long = 5000,
    )
}
