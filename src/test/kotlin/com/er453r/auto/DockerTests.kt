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
    fun `Logger test`() {
        DockerUtils.runImage("logtest")
    }
}
