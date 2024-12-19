package com.github.shmvanhouten.adventofcode2024.day19

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day19Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(countPossibleDesigns(example).size).isEqualTo(6)
        }

        @Test
        internal fun `part 1`() {
            expectThat(countPossibleDesigns(input).size).isEqualTo(350)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(countWaysToCreateAllDesigns(example)).isEqualTo(16)
        }

        @Test
        fun `example 2 but small`() {
            val example = """
                r, wr, b, g, bwu, rb, gb, br
                
                rrbgbr
            """.trimIndent()
            expectThat(countWaysToCreateAllDesigns(example)).isEqualTo(6)
        }

        @Test
        internal fun `part 2`() {
            expectThat(countWaysToCreateAllDesigns(input)).isEqualTo(769668867512623)
        }
    }

    private val input by lazy { readFile("/input-day19.txt")}
    private val example = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent()
}
