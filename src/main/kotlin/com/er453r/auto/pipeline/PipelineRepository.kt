package com.er453r.auto.pipeline

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PipelineRepository : JpaRepository<Pipeline, UUID>
