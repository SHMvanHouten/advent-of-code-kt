package com.github.shmvanhouten.adventofcode2024.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day01Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(findIdDistances(example)).isEqualTo(11)
        }

        @Test
        internal fun `part 1`() {
            expectThat(findIdDistances(input)).isEqualTo(3508942)
        }

        private val example = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            expectThat(sumSimilarityScores(input)).isEqualTo(26593248)
        }
    }

    private val input by lazy { readFile("/input-day01.txt")}

}
