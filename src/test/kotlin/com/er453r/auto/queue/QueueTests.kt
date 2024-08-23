package com.er453r.auto.queue

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class QueueTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var queueItemRepository: QueueItemRepository

    @Autowired
    lateinit var queueHandler: QueueHandler

    @Autowired
    lateinit var testQueue: TestQueue

    @Autowired
    lateinit var longQueue: LongQueue

    @Autowired
    lateinit var errorQueue: ErrorQueue

    @Test
    fun `Basic Queue Test`() {
        logger.info { "Starting Queue Test" }

        val item = TestItem(
            id = "my-id",
            name = "my-name",
        )

        testQueue.add(item)
        longQueue.add(item)
        testQueue.add(item)
        longQueue.add(item)
        errorQueue.add(item)

        repeat(6) {
            queueHandler.handle()
        }

        queueItemRepository.findAll().forEach { logger.info { it} }
    }
}
