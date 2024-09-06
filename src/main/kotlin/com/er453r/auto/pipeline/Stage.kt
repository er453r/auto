package com.er453r.auto.pipeline

import com.er453r.auto.utils.Process

interface Stage {
    fun run(environment: Map<String, String>, live:(Process) -> Unit): Process
}
