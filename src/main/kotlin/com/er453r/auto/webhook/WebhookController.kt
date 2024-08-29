package com.er453r.auto.webhook

import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.PipelineQueue
import com.er453r.auto.pipeline.PipelineQueueItem
import com.er453r.auto.pipeline.PipelineRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
    val pipelineQueue: PipelineQueue,
    val pipelineRepository: PipelineRepository,
    val objectMapper: ObjectMapper,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("webhooks/{name}")
    fun handleNotification(@PathVariable name: String, @RequestBody json: String) {
        logger.info { "New webhook notification: $name" }

        val pipeline = Pipeline(
            name = name,
            data = objectMapper.readValue(json, JsonNode::class.java),
        )

        pipelineRepository.save(pipeline)

        pipelineQueue.add(
            PipelineQueueItem(
                pipelineId = pipeline.id!!
            )
        ).let {
            pipeline.queueItem = it

            pipelineRepository.save(pipeline)
        }
    }
}
