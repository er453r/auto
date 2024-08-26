package com.er453r.auto.pipeline

import com.er453r.auto.utils.Process

data class Pipeline (
    val results: MutableList<Process> = mutableListOf()
){
    fun run(stages: List<Stage>, env: MutableMap<String, String>) {
        stages.forEach{
            val result = it.run(env)

            results += result

            env.putAll(result.env)
        }
    }
}
