package com.er453r.auto.utils

import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream
import java.io.File
import java.time.ZonedDateTime

data class Process(
    val cmd: String,
    val result: Int,
    val lines: List<Line> = emptyList(),
    val env: Map<String, String> = emptyMap(),
)

data class Line(
    val line: String,
    val isError: Boolean = false,
    val time: ZonedDateTime = ZonedDateTime.now(),
)

fun runScript(script: String, env: Map<String, String>): Process {
    val temp = File.createTempFile("tmp", ".sh")
    temp.writeText(script)

    val result = cmd(
        cmd = "bash -eux ${temp.absolutePath}",
        env = env
    )

    temp.delete()

    return result
}

private fun cmd(cmd: String, env: Map<String, String> = emptyMap()): Process {
    val lines = mutableListOf<Line>()

    val result = ProcessExecutor()
        .commandSplit(cmd)
        .redirectOutput(object : LogOutputStream() {
            override fun processLine(line: String) {
                lines.add(Line(line))
            }
        })
        .environment(env)
        .execute()

    val keyValue = """\+ (\w+)=(\w*).*""".toRegex()

    val returnEnv = lines.map { it.line }
        .filter { it.matches(keyValue) }
        .map { it.destructured(keyValue) }
        .associate { (key, value) -> key to value }

    return Process(
        cmd = cmd,
        result = result.exitValue,
        lines = lines,
        env = returnEnv,
    )
}
