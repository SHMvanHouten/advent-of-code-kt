package com.github.shmvanhouten.adventofcode2023.day04

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigInteger

class Day04Test {

    private val example = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()
    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(example.lines().map { getCardScore(it) }.first())
                .isEqualTo(BigInteger.valueOf(8))
            expectThat(example.lines().sumOf { getCardScore(it) })
                .isEqualTo(BigInteger.valueOf(13))
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().sumOf { getCardScore(it) })
                .isEqualTo(BigInteger.valueOf(33950))
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

    private val input by lazy { readFile("/input-day04.txt")}

}
