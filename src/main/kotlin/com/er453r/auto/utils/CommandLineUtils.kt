package com.er453r.auto.utils

import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream
import java.io.File
import java.time.ZonedDateTime
import java.util.UUID

data class Process(
    val id: UUID,
    val result: Int? = null,
    val lines: List<Line> = emptyList(),
    val env: Map<String, String> = emptyMap(),
)

data class Line(
    val line: String,
    val isError: Boolean = false,
    val time: ZonedDateTime = ZonedDateTime.now(),
)

fun runScript(script: String, env: Map<String, String>, live:(Process) -> Unit): Process {
    val temp = File.createTempFile("tmp", ".sh")
    temp.writeText(script)

    val result = cmd(
        cmd = "bash -eux ${temp.absolutePath}",
        env = env,
        live = live,
    )

    temp.delete()

    return result
}

val keyValue = """\+ (\w+)=(\w*).*""".toRegex()

private fun cmd(cmd: String, env: Map<String, String> = emptyMap(), live:(Process) -> Unit): Process {
    val lines = mutableListOf<Line>()
    val id = UUID.randomUUID()

    val result = ProcessExecutor()
        .commandSplit(cmd)
        .redirectOutput(object : LogOutputStream() {
            override fun processLine(line: String) {
                lines.add(Line(line))

                val returnEnv = lines.map { it.line }
                    .filter { it.matches(keyValue) }
                    .map { it.destructured(keyValue) }
                    .associate { (key, value) -> key to value }

                live(Process(
                    id = id,
                    lines = lines,
                    env = returnEnv + env,
                ))
            }
        })
        .environment(env)
        .execute()

    val returnEnv = lines.map { it.line }
        .filter { it.matches(keyValue) }
        .map { it.destructured(keyValue) }
        .associate { (key, value) -> key to value }

    return Process(
        id = id,
        result = result.exitValue,
        lines = lines,
        env = returnEnv + env,
    )
}
