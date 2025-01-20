package com.er453r.auto

import com.er453r.auto.docker.DockerUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DockerTests {
    private val logger = KotlinLogging.logger {}

    @Test
    fun `Inspect image for inputs`() {
        val imageInfo = DockerUtils.imageInfo("git-test")

        logger.info { imageInfo }
    }

    @Test
    fun `Logger test - basic`() {
        DockerUtils.start(
            image = "logtest",
            onCompleted = {
                logger.info { "Log test completed" }
            },
            onError = {
                logger.info { "Log test failed" }
            }
        )
    }
    
    @Test
    fun `Logger test - advanced`() {
        var finished = false

        DockerUtils.start(
            image = "logtest",
            env = mapOf("LOG_LEVEL" to "debug"),
            onLine = { line, isError ->
                logger.info { "[LINE] $line - isError: $isError" }
            },
            onCompleted = { env ->
                logger.info { "Log test completed" }

                logger.info { "Result env: $env" }

                finished = true
            },
            onError = {
                logger.info { "Log test failed" }
            }
        )

        while (!finished) {
            Thread.sleep(1000)
        }

        logger.info { "Test finished!" }
    }
}
