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
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
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
