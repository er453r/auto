package com.er453r.auto.pipeline.step

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StepRepository : JpaRepository<Step, UUID>
