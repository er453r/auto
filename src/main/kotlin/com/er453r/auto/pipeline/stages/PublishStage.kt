package com.er453r.auto.pipeline.stages

import com.er453r.auto.pipeline.Stage
import com.er453r.auto.utils.Process
import com.er453r.auto.utils.runScript
import io.github.oshai.kotlinlogging.KotlinLogging

class PublishStage : Stage {
    private val logger = KotlinLogging.logger {}

    private val checkoutScript = ClassLoader.getSystemResource("scripts/publish.sh").readText()

    override fun run(environment: Map<String, String>, live:(Process) -> Unit): Process {
        logger.info { "Publish..." }

        return runScript(
            script = checkoutScript,
            env = environment,
            live = live,
        )
    }
}
