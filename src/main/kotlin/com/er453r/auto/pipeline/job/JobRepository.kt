package com.er453r.auto.pipeline.job

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JobRepository : JpaRepository<Job, UUID>
