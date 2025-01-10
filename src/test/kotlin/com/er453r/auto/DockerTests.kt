package com.er453r.auto

import com.github.dockerjava.core.DockerClientImpl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DockerTests {
    private val logger = KotlinLogging.logger {}

    @Test
    fun `Docker Tests`() {
        logger.info { "Docker Tests" }

        val client = DockerClientImpl.getInstance()

        logger.info { client.pingCmd().exec() }

    }
}
