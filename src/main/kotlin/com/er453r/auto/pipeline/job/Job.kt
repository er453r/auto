package com.er453r.auto.pipeline.job

import com.er453r.auto.pipeline.Pipeline
import com.er453r.auto.pipeline.step.Step
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*

@Entity
data class Job(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @CreationTimestamp val createdDate: ZonedDateTime? = null,
    @UpdateTimestamp val lastModifiedDate: ZonedDateTime? = null,

    @ManyToOne
    val pipeline: Pipeline,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    val env: Map<String, String>,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,
){
    enum class Status {
        PENDING,
        RUNNING,
        DONE,
        ERROR,
    }

    @OneToMany(mappedBy = )
    val steps: List<Step>? = null
}
