package com.er453r.auto.image

import org.springframework.stereotype.Component

@Component
class ImageUtils(
    val imageRepository: ImageRepository,
) {
    fun addDefault() {
        listOf(
            "auto.git",
            "auto.build",
        ).forEach {
            imageRepository.save(Image(name = it))
        }
    }
}
