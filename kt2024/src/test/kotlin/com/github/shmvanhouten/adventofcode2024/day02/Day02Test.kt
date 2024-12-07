package com.github.shmvanhouten.adventofcode2024.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day02Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @CsvSource(value = [
            "7 6 4 2 1, true",
            "7 6 4 1 0, true",
            "7 6 5 1 0, false",
            "1 2 7 8 9, false",
            "9 7 6 2 1, false",
            "1 3 2 4 5, false",
            "4 3 2 1 3, false",
            "8 6 4 4 1, false",
            "8 6 4 3 3, false",
            "1 3 6 11 12, false",
            "1 3 6 7 9, true",
        ])
        internal fun `a report is safe or unsafe`(reportString: String, isSafe: Boolean) {
            val report = reportString.parse()
            expectThat(report.isSafe()).isEqualTo(isSafe)
        }

        @Test
        internal fun `example 1`() {
            expectThat(example.lines().map { it.parse() }.count { it.isSafe() }).isEqualTo(2)
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().map { it.parse() }.count { it.isSafe() }).isEqualTo(572)
        }
    }

    @Nested
    inner class Part2 {

        @ParameterizedTest
        @CsvSource(value = [
            "7 6 4 2 1, true",
            "1 2 7 8 9, false",
            "9 7 6 2 1, false",
            "1 3 2 4 5, true",
            "8 6 4 4 1, true",
            "1 3 6 7 9, true",
            "29 32 30 31 34 35 37, true"
        ])
        internal fun `a report is safe or unsafe`(reportString: String, isSafe: Boolean) {
            val report = reportString.parse()
            expectThat(report.isSafeDampened()).isEqualTo(isSafe)
        }

        @Test
        internal fun `part 2`() {
            expectThat(input.lines().map { it.parse() }.count { it.isSafeDampened() }).isEqualTo(612)
        }
    }

    private val input by lazy { readFile("/input-day02.txt")}
    private val example = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()
}
