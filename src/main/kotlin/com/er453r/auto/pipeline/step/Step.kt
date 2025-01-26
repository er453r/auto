package com.er453r.auto.pipeline.step

import com.er453r.auto.pipeline.job.Job
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*

@Entity
data class Step(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @CreationTimestamp val createdDate: ZonedDateTime? = null,
    @UpdateTimestamp val lastModifiedDate: ZonedDateTime? = null,

    @ManyToOne
    val job: Job,

    val image: String,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    val env: Map<String, String>,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    var result: Map<String, String>? = null,

    @Column(columnDefinition = "json")
    @Type(JsonType::class)
    var log: MutableList<StepLogLine>? = null,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,
){
    enum class Status {
        PENDING,
        RUNNING,
        DONE,
        ERROR,
    }
}
