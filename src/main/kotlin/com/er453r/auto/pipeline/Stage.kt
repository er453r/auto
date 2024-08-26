package com.er453r.auto.pipeline

interface Stage {
    fun run(environment: Map<String, String>): Map<String, String>
}
