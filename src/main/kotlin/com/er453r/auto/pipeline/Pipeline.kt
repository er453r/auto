package com.er453r.auto.pipeline

class Pipeline (
    val stages: List<Stage>
){
    fun run(env: MutableMap<String, String>) {
        stages.forEach{
            env.putAll(it.run(env))
        }
    }
}
