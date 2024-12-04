package com.github.shmvanhouten.adventofcode2024.day04

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day04Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(findXMasses(example)).isEqualTo(18)
        }

        @Test
        internal fun `part 1`() {
            expectThat(findXMasses(input)).isEqualTo(2547)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {
            expectThat(findCrossMasses(example)).isEqualTo(9)
        }

        @Test
        internal fun `part 2`() {
            expectThat(findCrossMasses(input)).isEqualTo(1939)
        }
    }

    private val input by lazy { readFile("/input-day04.txt")}
    private val example = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
""".trimIndent()
}
