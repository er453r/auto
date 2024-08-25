package com.er453r.auto.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class ConfigurationTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var config: Configuration

    @Test
    fun `Configuration Test`() {
        logger.info { "Configuration: $config" }

        assertEquals(9999999, config.queue.delay)
    }
}
