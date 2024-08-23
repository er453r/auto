package com.er453r.auto.queue

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
data class QueueItem(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @CreationTimestamp val createdDate: ZonedDateTime? = null,
    @UpdateTimestamp val lastModifiedDate: ZonedDateTime? = null,

    val queue: String,
    val data: String,

    @Lob var details: String? = null,

    @Enumerated(EnumType.STRING) var status: QueueItemStatus = QueueItemStatus.PENDING,
)
