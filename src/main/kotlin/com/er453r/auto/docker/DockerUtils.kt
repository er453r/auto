package com.er453r.auto.docker

import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import java.net.URI

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
    }

    data class ImageInfo(
        val name: String,
        val description: String,
        val inputs: List<String>,
    )
}
