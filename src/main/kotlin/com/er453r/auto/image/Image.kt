package com.er453r.auto.image

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Image(
    @Id val name: String,
    var storage: String? = null,
)
