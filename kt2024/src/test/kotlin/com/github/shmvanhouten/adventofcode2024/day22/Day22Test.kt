package com.github.shmvanhouten.adventofcode2024.day22

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day22Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `for starting number 123, the next secret numbers would be`() {
            var result = calculateNextNumber(123)
            expectThat(result).isEqualTo(15887950)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(16495136)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(527345)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(704524)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(1553684)
        }

        @Test
        fun `example 1`() {
            val example = """
                1
                10
                100
                2024
            """.trimIndent()
            val input = example.lines().map { it.toLong() }
            expectThat(input.sumOf { calculateNextNumber(it, 2000) })
                .isEqualTo(37327623)
        }

        @Test
        internal fun `part 1`() {
            val lines = input.lines().map { it.toLong() }
            expectThat(lines.sumOf { calculateNextNumber(it, 2000) })
                .isEqualTo(20401393616)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `with starting number 123, this is the sequence of associated changes`() {
            expectThat(associatedChanges(123).subList(0, 9)).isEqualTo(
                listOf(
                    0 to -3,
                    6 to 6,
                    5 to -1,
                    4 to -1,
                    4 to 0,
                    6 to 2,
                    4 to -2,
                    4 to 0,
                    2 to -2,
                )
            )
        }

        @Test
        fun `first occurrence of each sequence`() {
            expectThat(firstOccurrenceOfEachSequenceToBananasBought(
                associatedChanges(123).subList(0, 9))
            ).isEqualTo(listOf(
                listOf(-3, 6, -1, -1) to 4,
                listOf(6, -1, -1, 0) to 4,
                listOf(-1, -1, 0, 2) to 6,
                listOf(-1, 0, 2, -2) to 4,
                listOf(0, 2, -2, 0) to 4,
                listOf(2, -2, 0, -2) to 2,
            ))
        }

        @Test
        fun `example 2`() {
            expectThat(findMostBananas(listOf(1, 2, 3, 2024L)))
                .isEqualTo(23)
        }

        @Test
        internal fun `part 2`() {
            expectThat(findMostBananas(input.lines().map { it.toLong() } ))
                .isEqualTo(2272)
        }
    }

    private val input by lazy { readFile("/input-day22.txt")}

}
