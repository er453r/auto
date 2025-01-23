package com.er453r.auto.image

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("images")
class ImageController(
    val imageRepository: ImageRepository
) {
    @GetMapping
    fun list(): List<Image> = imageRepository.findAll()

    @GetMapping("{name}")
    fun get(@PathVariable name: String): Image = imageRepository.getReferenceById(name)
}
