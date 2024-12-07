package com.github.shmvanhouten.adventofcode2024.day07

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day07Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @ValueSource(
            strings = [
                "190: 10 19",
                "29: 10 19",
                "3267: 81 40 27",
                "292: 11 6 16 20"
            ]
        )
        fun `valid sums can be obtained by multiplying and adding`(line: String) {
            expectThat(isAValidLine(line)).isEqualTo(true)
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                "83: 17 5",
                "156: 15 6",
                "7290: 6 8 6 15",
                "161011: 16 10 13",
                "192: 17 8 14",
                "21037: 9 7 18 13",
            ]
        )
        fun `invalid sums`(line: String) {
            expectThat(isAValidLine(line)).isEqualTo(false)
        }

        @Test
        internal fun `example 1`() {
            expectThat(part1(example)).isEqualTo(3749)
        }

        @Test
        internal fun `part 1`() {
            expectThat(part1(input)).isEqualTo(3749)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example p 2`() {
            expectThat(part2(example)).isEqualTo(11387)
        }

        @Test
        internal fun `part 2`() {
            expectThat(part2(input)).isEqualTo(11387)
        }
    }

    private val input by lazy { readFile("/input-day07.txt")}
    private val example = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

}
