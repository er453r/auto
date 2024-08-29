package com.er453r.auto.queue

import com.fasterxml.jackson.databind.JsonNode
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*

@Entity
data class QueueItem(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @CreationTimestamp val createdDate: ZonedDateTime? = null,
    @UpdateTimestamp val lastModifiedDate: ZonedDateTime? = null,

    val queue: String,
    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    val data: JsonNode,

    @Lob var details: String? = null,

    @Enumerated(EnumType.STRING) var status: Status = Status.PENDING,
) {
    enum class Status {
        PENDING,
        ERROR,
        DONE,
    }
}
