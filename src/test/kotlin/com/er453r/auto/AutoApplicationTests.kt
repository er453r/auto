package com.er453r.auto

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AutoApplicationTests {
    private val logger = KotlinLogging.logger {}

    @Test
    fun `Context loading`() {
        logger.info { "Context loading" }
    }
}
