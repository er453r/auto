package com.er453r.auto.queue

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QueueItemRepository : JpaRepository<QueueItem, UUID> {
    fun findAllByStatusOrderByCreatedDateAsc(status: QueueItemStatus): List<QueueItem>
}
