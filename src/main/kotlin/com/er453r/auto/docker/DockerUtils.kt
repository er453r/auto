package com.er453r.auto.docker

import com.er453r.auto.image.ImageMetadata
import com.er453r.auto.utils.destructured
import com.github.dockerjava.api.command.CreateContainerResponse
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import io.github.oshai.kotlinlogging.KotlinLogging
import java.net.URI

private val logger = KotlinLogging.logger {}

class DockerUtils {
    companion object {
        private val CLIENT = DockerClientBuilder.getInstance().withDockerHttpClient(
            ApacheDockerHttpClient.Builder()
                .dockerHost(URI("unix:///var/run/docker.sock"))
                .build()
        ).build()

        private val ENV_REGEX = Regex("^(\\w+)=(.*)$")

        fun imageInfo(image: String): ImageInfo {
            val inspectResponse = CLIENT.inspectImageCmd(image).exec()
            val labels = inspectResponse.config?.labels

            return ImageInfo(
                image = image,
                name = labels?.get(ImageMetadata.NAME.key) ?: "",
                description = labels?.get(ImageMetadata.DESCRIPTION.key) ?: "",
                inputs = labels?.get(ImageMetadata.INPUTS.key)?.split(" ") ?: emptyList(),
                docker = labels?.containsKey(ImageMetadata.DOCKER.key) ?: false,
                storage = labels?.containsKey(ImageMetadata.STORAGE.key) ?: false,
            )
        }

        fun createVolume(name: String) {
            CLIENT.createVolumeCmd().withName(name).exec()
        }

        fun removeVolume(name: String) {
            CLIENT.removeVolumeCmd(name).exec()
        }

        fun start(
            image: String,
            env: Map<String, String> = emptyMap(),
            volumes: Map<String, String> = emptyMap(),
            onLine: (String, Boolean) -> Unit = { _, _ -> },
            onCompleted: (Map<String, String>) -> Unit,
            onError: (Map<String, String>) -> Unit,
        ) {
            val containerResponse: CreateContainerResponse = CLIENT
                .createContainerCmd(image)
                .withEnv(env.toList())
                .withHostConfig(
                    HostConfig.newHostConfig().withBinds(
                        volumes.map { Bind(it.key, Volume("/${it.value}")) }
                ))
                .exec()

            logger.info { "Binds ${volumes.map { Bind(it.key, Volume("/${it.value}")) }}" }

            logger.info { "Created container ${containerResponse.id}" }

            CLIENT.startContainerCmd(containerResponse.id).exec()

            logger.info { "Started container ${containerResponse.id}" }

            val resultEnv = env.toMutableMap()

            CLIENT.logContainerCmd(containerResponse.id)
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .exec(
                    DockerLogCallback(
                        onLine = { line, isError ->
                            if (line.matches(ENV_REGEX)) {
                                val (key, value) = line.destructured(ENV_REGEX)

                                resultEnv[key] = value
                            }

                            if (isError)
                                logger.error { line }
                            else
                                logger.info { line }

                            onLine(line, isError)
                        },
                        onCompleted = { onCompleted(resultEnv) },
                        onError = { onError(resultEnv) },
                    )
                )
        }
    }

    data class ImageInfo(
        val image: String,
        val name: String,
        val description: String,
        val inputs: List<String>,
        val docker:Boolean,
        val storage:Boolean,
    )
}
