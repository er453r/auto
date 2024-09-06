package com.er453r.auto.pipeline

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("pipelines")
class PipelineController(
    val pipelineRepository: PipelineRepository
) {
    @GetMapping
    fun list(): List<Pipeline> = pipelineRepository.findAll()

    @GetMapping("{id}")
    fun get(@PathVariable id: UUID): Pipeline = pipelineRepository.getReferenceById(id)
}
