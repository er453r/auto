package com.er453r.auto.docker

import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import io.github.oshai.kotlinlogging.KotlinLogging
import java.lang.Thread.sleep
import java.net.URI

private val logger = KotlinLogging.logger {}

class DockerUtils {
    companion object {
        private val CLIENT = DockerClientBuilder.getInstance().withDockerHttpClient(
            ApacheDockerHttpClient.Builder()
                .dockerHost(URI("unix:///var/run/docker.sock"))
                .build()
        ).build()

        fun imageInfo(image: String): ImageInfo {
            val inspectResponse = CLIENT.inspectImageCmd(image).exec()
            val labels = inspectResponse.config?.labels

            return ImageInfo(
                name = labels?.get("NAME") ?: "",
                description = labels?.get("DESCRIPTION") ?: "",
                inputs = labels?.get("INPUTS")?.split(" ") ?: emptyList(),
            )
        }

        fun runImage(image: String): String {
            val containerResponse: CreateContainerResponse = CLIENT
                .createContainerCmd(image)
                .exec()

            logger.info { "Created container ${containerResponse.id}" }

            CLIENT.startContainerCmd(containerResponse.id).exec()

            logger.info { "Started container ${containerResponse.id}" }

            CLIENT.logContainerCmd(containerResponse.id)
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .exec(
                DockerLogCallback(
                    onLine = { line, isError ->
                        if (isError) {
                            logger.error { line }
                        } else {
                            logger.info { line }
                        }
                    }
                )
            )

            sleep(60000)

            return ""
        }
    }

    data class ImageInfo(
        val name: String,
        val description: String,
        val inputs: List<String>,
    )
}
