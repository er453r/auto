package com.er453r.auto.pipeline

import com.er453r.auto.configuration.Configuration
import com.er453r.auto.queue.Queue
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class PipelineQueue(
    val configuration: Configuration,
    val pipelineRepository: PipelineRepository,
    val objectMapper: ObjectMapper,
) : Queue<PipelineQueueItem>(type = PipelineQueueItem::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: PipelineQueueItem) {
        val pipeline = pipelineRepository.getReferenceById(item.pipelineId)

        logger.info { "Starting new pipeline: $item" }



        logger.info { "Completed webhook data: $item" }
    }
}
