package com.er453r.auto.pipeline

import com.er453r.auto.queue.QueueItem
import com.er453r.auto.utils.Process
import com.fasterxml.jackson.databind.JsonNode
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*

@Entity
class Pipeline(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @CreationTimestamp val createdDate: ZonedDateTime? = null,
    @UpdateTimestamp val lastModifiedDate: ZonedDateTime? = null,

    val name: String,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    val data: JsonNode,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    val log: MutableList<Process> = mutableListOf(),

    @OneToOne
    var queueItem: QueueItem? = null,
)
