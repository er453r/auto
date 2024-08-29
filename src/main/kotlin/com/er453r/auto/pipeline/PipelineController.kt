package com.er453r.auto.pipeline

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("pipelines")
class PipelineController(
    val pipelineRepository: PipelineRepository
) {
    @GetMapping
    fun list(): List<Pipeline> = pipelineRepository.findAll()
}
