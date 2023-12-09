package com.github.shmvanhouten.adventofcode2023.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day09Test {

    val example = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()
    @Nested
    inner class Part1 {

        @Test
        internal fun `line extrapolated is 18`() {
            val line = toDataSet("0 3 6 9 12 15")
            expectThat(extrapolate(line)).isEqualTo(18)
        }

        @Test
        fun `example 1`() {
            expectThat(
            example.lines()
                .map { toDataSet(it) }
                .sumOf { extrapolate(it) }
            ).isEqualTo(114)
        }

        @Test
        internal fun `part 1`() {
            expectThat(
                input.lines()
                    .map { toDataSet(it) }
                    .sumOf { extrapolate(it) }
            ).isEqualTo(1696140818)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `new example`() {
            val line = toDataSet("10 13 16 21 30 45")
            expectThat(extrapolateBack(line)).isEqualTo(5)
        }

        @Test
        internal fun `part 2`() {
            expectThat(
                input.lines()
                    .map { toDataSet(it) }
                    .sumOf { extrapolateBack(it) }
            ).isEqualTo(1152)
        }
    }

    private val input by lazy { readFile("/input-day09.txt")}

}
