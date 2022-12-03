package com.github.shmvanhouten.adventofcode2022.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day03Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `given aa the sum of priorities is 1`() {
            assertThat(prioritySumOf("aa"))
                .isEqualTo(1)
        }

        @Test
        internal fun `the priority sum of bb is 2`() {
            assertThat(prioritySumOf("bb"))
                .isEqualTo(2)
        }

        @Test
        internal fun `the priority sum of AA is 27`() {
            assertThat(prioritySumOf("AA"))
                .isEqualTo(27)
        }

        @Test
        internal fun `the priority of abcb is 2`() {
            assertThat(prioritySumOf("abcb"))
                .isEqualTo(2)
        }

        @Test
        internal fun `the priority of abac is 1`() {
            assertThat(prioritySumOf("abac"))
                .isEqualTo(1)
        }

        @Test
        internal fun `the priority of abcdec is 3`() {
            assertThat(prioritySumOf("abcdec"))
                .isEqualTo(3)
        }

        @Test
        internal fun `it sums up the priorities for all bags`() {
            val input = """
                abcdec
                aa
            """.trimIndent()

            assertThat(prioritySumOf(input))
                .isEqualTo(4)
        }

        @Test
        internal fun example() {
            val input = """
                |vJrwpWtwJgWrhcsFMMfFFhFp
                |jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
                |PmmdzqPrVvPwwTWBwg
                |wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
                |ttgJtRGJQctTZtZT
                |CrZsJsPPZsGzwwsLwLmpwMDw
            """.trimMargin()

            assertThat(prioritySumOf(input))
                .isEqualTo(157)
        }

        @Test
        internal fun `part 1`() {
            assertThat(prioritySumOf(input))
                .isEqualTo(8493)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                |vJrwpWtwJgWrhcsFMMfFFhFp
                |jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
                |PmmdzqPrVvPwwTWBwg
                |wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
                |ttgJtRGJQctTZtZT
                |CrZsJsPPZsGzwwsLwLmpwMDw
            """.trimMargin()

            assertThat(prioritySumOfSharedItems(input))
                .isEqualTo(70)
        }

        @Test
        internal fun `part 2`() {
            assertThat(prioritySumOfSharedItems(input))
                .isEqualTo(2552)
        }
    }

    private val input by lazy { readFile("/input-day03.txt")}

}
