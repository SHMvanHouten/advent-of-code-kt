package com.github.shmvanhouten.adventofcode2024.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day10Test {

    @Nested
    inner class Part1 {

        @Test
        fun `example trail has a score of 4`() {
            val example = """
                1190779
                1131798
                8882707
                6543456
                7650987
                8762222
                9872222
            """.trimIndent()
            expectThat(findTrailHeadScores(example)).isEqualTo(4)
        }

        @Test
        internal fun `example 1`() {
            expectThat(findTrailHeadScores(example)).isEqualTo(36)
        }

        @Test
        internal fun `part 1`() {
            expectThat(findTrailHeadScores(input)).isEqualTo(782)
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

    private val input by lazy { readFile("/input-day10.txt")}
    private val example = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()
}
