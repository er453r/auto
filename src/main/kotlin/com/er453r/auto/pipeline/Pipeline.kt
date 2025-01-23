package com.er453r.auto.pipeline

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
    val env: Map<String, String>,
)
