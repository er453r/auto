package com.er453r.auto.aoc

import java.io.File

/**
 * Advent of Code scaffolding generator.
 *
 * Usage:
 *   - Run with two arguments: YEAR DAYS
 *     Example: 2025 7
 *
 * Effect:
 *   - Creates directory: src/test/kotlin/com/er453r/auto/aoc/aoc{YEAR}
 *   - For each day [1..DAYS] creates if missing:
 *       - DayXX.kt (package: com.er453r.auto.aoc.aoc{YEAR}, class: DayXX)
 *       - DayXX.txt
 *       - DayXX_test.txt
 */
object GenerateAoc {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            System.err.println("Usage: <YEAR> <DAYS>")
            return
        }

        val year = args[0].toIntOrNull()
        val days = args[1].toIntOrNull()

        if (year == null || year < 2000) {
            System.err.println("Invalid year: '${args[0]}'")
            return
        }
        if (days == null || days !in 1..25) {
            System.err.println("Invalid days: '${args[1]}' (expected 1..25)")
            return
        }

        val baseDir = File("src/test/kotlin/com/er453r/auto/aoc/aoc$year")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }

        for (d in 1..days) {
            val dayStr = d.toString().padStart(2, '0')

            val ktFile = File(baseDir, "Day${dayStr}.kt")
            val txtFile = File(baseDir, "Day${dayStr}.txt")
            val testTxtFile = File(baseDir, "Day${dayStr}_test.txt")

            if (!ktFile.exists()) {
                ktFile.writeText(dayKtTemplate(year, dayStr))
                println("Created: ${ktFile.path}")
            } else {
                println("Skipped (exists): ${ktFile.path}")
            }

            if (!txtFile.exists()) {
                txtFile.writeText("")
                println("Created: ${txtFile.path}")
            } else {
                println("Skipped (exists): ${txtFile.path}")
            }

            if (!testTxtFile.exists()) {
                testTxtFile.writeText("")
                println("Created: ${testTxtFile.path}")
            } else {
                println("Skipped (exists): ${testTxtFile.path}")
            }
        }

        println("Done. AOC $year scaffolding generated for $days day(s).")
    }

    private fun dayKtTemplate(year: Int, dayStr: String): String = """
        |package com.er453r.auto.aoc.aoc$year
        |
        |class Day$dayStr {
        |    fun part1(input: List<String>): Any {
        |        // TODO: implement
        |        return 0
        |    }
        |
        |    fun part2(input: List<String>): Any {
        |        // TODO: implement
        |        return 0
        |    }
        |}
        |
    """.trimMargin()
}
