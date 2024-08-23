package com.er453r.auto.queue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass

abstract class Queue<T : Any>(val type: KClass<T>) {
    private val logger = KotlinLogging.logger {}
    private val objectMapper = ObjectMapper().registerKotlinModule()

    @Autowired
    lateinit var queueItemRepository: QueueItemRepository

    val name: String = this.javaClass.simpleName

    init {
        logger.info { "Created queue $name" }
    }

    fun add(item: T) {
        queueItemRepository.save(
            QueueItem(
                queue = name,
                data = objectMapper.writeValueAsString(item),
            )
        )
    }

    fun handleRaw(item: String) {
        handle(objectMapper.readValue(item, type.java))
    }

    abstract fun handle(item: T)
}
