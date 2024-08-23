package com.er453r.auto.queue

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class TestQueue : Queue<TestItem>(type = TestItem::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: TestItem) {
        logger.info { "Handling item logic: $item" }
    }
}
