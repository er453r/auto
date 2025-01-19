package com.er453r.auto.docker

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.StreamType
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.Closeable

class DockerLogCallback(
    val onLine: (String, Boolean) -> Unit,
    val onStart: () -> Unit = {},
    val onError: () -> Unit = {},
    val onCompleted: () -> Unit = {},
    val onClose: () -> Unit = {},
) : ResultCallback<Frame> {
    private val logger = KotlinLogging.logger {}

    override fun close() {
        logger.info { "Closing log callback" }

        onClose()
    }

    override fun onStart(closeable: Closeable?) {
        logger.info { "Starting log callback" }

        onStart()
    }

    override fun onError(throwable: Throwable?) {
        logger.error(throwable) { "Error log callback" }

        onError()
    }

    override fun onComplete() {
        logger.info { "Complete log callback" }

        onCompleted()
    }

    override fun onNext(frame: Frame) {
        val line = String(frame.payload, Charsets.UTF_8)
        val isError = frame.streamType == StreamType.STDERR

        onLine(line, isError)
    }
}
