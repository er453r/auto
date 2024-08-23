package com.er453r.auto.queue

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LongQueue : Queue<TestItem>(type = TestItem::class) {
    private val logger = KotlinLogging.logger {}

    override fun handle(item: TestItem) {
        logger.info { "Handling LONG item logic: $item" }

        TimeUnit.SECONDS.sleep(5)
    }
}
