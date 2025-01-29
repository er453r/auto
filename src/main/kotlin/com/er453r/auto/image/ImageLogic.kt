package com.er453r.auto.image

import com.er453r.auto.docker.DockerUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

@Component
class ImageLogic(
    val imageRepository: ImageRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun getStorage(image: Image): String {
        val storageName = "storage-image-${UUID.randomUUID()}"

        if (image.storage == null) {
            logger.info { "Creating storage: $storageName" }

            DockerUtils.createVolume(storageName)

            image.storage = storageName
            imageRepository.save(image)
        }

        return storageName
    }

    fun clearStorage(image: Image) {
        image.storage?.let {
            logger.info { "Clearing storage: $it for volume ${image.name}" }

            DockerUtils.removeVolume(it)
            image.storage = null
            imageRepository.save(image)
        }
    }
}
