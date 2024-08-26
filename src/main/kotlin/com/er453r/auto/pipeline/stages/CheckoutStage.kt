package com.er453r.auto.pipeline.stages

import com.er453r.auto.pipeline.Stage
import com.er453r.auto.utils.Process
import com.er453r.auto.utils.runScript
import io.github.oshai.kotlinlogging.KotlinLogging

class CheckoutStage : Stage {
    private val logger = KotlinLogging.logger {}

    private val checkoutScript = ClassLoader.getSystemResource("scripts/checkout.sh").readText()

    override fun run(environment: Map<String, String>): Process {
        logger.info { "Checkout..." }

        return runScript(
            script = checkoutScript,
            env = environment,
        )
    }
}
