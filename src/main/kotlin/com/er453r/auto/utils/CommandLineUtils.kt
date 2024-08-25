package com.er453r.auto.utils

import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream
import java.io.File
import java.time.ZonedDateTime

data class Process(
    val cmd: String,
    val result: Int,
    val lines: List<Line> = emptyList(),
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
        cmd = "bash -x ${temp.absolutePath}",
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

    return Process(
        cmd = cmd,
        result = result.exitValue,
        lines = lines,
    )
}
