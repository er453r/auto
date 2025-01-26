package com.er453r.auto.pipeline.job

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("jobs")
class JobController(
    val jobRepository: JobRepository,
) {
    @GetMapping
    fun list(): List<Job> = jobRepository.findAll()

    @GetMapping("{id}")
    fun get(@PathVariable id: UUID): Job = jobRepository.getReferenceById(id)
}
