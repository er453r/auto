package com.er453r.auto.image

import com.er453r.auto.docker.DockerUtils
import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.PipelineRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PipelineTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var imageUtils: ImageUtils

    @Autowired
    lateinit var imageRepository: ImageRepository

    @Autowired
    lateinit var pipelineRepository: PipelineRepository

    @Test
    fun `Basic Pipeline Test`() {
        imageUtils.addDefault()

        pipelineRepository.save(Pipeline(
            name = "Test Pipeline",
            env = mapOf(
                "GIT_SSH" to "git@github.com:er453r/auto.git",
                "GIT_KEY" to "w00t",
            ),
        ))

        pipelineRepository.findAll().forEach { pipeline ->
            logger.info { "Pipeline $pipeline" }

            // first - find image that env satisfies all needed inputs
            val matchingImages = imageRepository.findAll().map { DockerUtils.imageInfo(it.name) }.filter { pipeline.env.keys.containsAll(it.inputs) }

            logger.info { "Matching Images: $matchingImages" }
        }
    }
}
