package com.er453r.auto.queue

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("queue")
class QueueItemController(
    val queueItemRepository: QueueItemRepository
) {
    @GetMapping
    fun getAllQueueItems(): List<QueueItem> = queueItemRepository.findAll()
}
