package com.er453r.auto.image

import com.er453r.auto.docker.DockerUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ImageTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var imageUtils: ImageUtils

    @Autowired
    lateinit var imageRepository: ImageRepository

    @Test
    fun `Basic Image Test`() {
        imageUtils.addDefault()

        imageRepository.findAll().forEach {
            logger.info { "Image ${it.name} - ${DockerUtils.imageInfo(it.name)}" }
        }
    }
}
