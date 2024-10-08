package com.er453r.auto.queue

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class QueueHandler(
    val queueItemRepository: QueueItemRepository,
    val queues: List<Queue<*>>,
) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.info { "Found ${queues.size} queues to handle" }
    }

    @Scheduled(fixedDelayString = "\${auto.queue.delay}", initialDelayString = "\${auto.queue.interval}")
    fun handle() {
        logger.info { "Handling queues" }

        val pendingItems = queueItemRepository.findAllByStatusOrderByCreatedDateAsc(QueueItem.Status.PENDING)

        logger.info { "Found ${pendingItems.size} pending items" }

        pendingItems.firstOrNull()?.let { item ->
            val queue = queues.first { it.name == item.queue }

            try {
                item.status = QueueItem.Status.PROCESSING
                queueItemRepository.saveAndFlush(item)

                queue.handleRaw(item.data)

                item.status = QueueItem.Status.DONE
            }
            catch (e: Exception) {
                item.status = QueueItem.Status.ERROR
                item.details = e.stackTraceToString()
            }

            queueItemRepository.save(item)
        }
    }
}
