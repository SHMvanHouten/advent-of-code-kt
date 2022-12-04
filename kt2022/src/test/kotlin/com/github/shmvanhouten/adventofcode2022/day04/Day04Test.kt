package com.github.shmvanhouten.adventofcode2022.day04

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day04Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val input = """
                |2-4,6-8
                |2-3,4-5
                |5-7,7-9
                |2-8,3-7
                |6-6,4-6
                |2-6,4-8
            """.trimMargin()

            assertThat(
                input.parse().count { theyOverlap(it.first, it.second) },
            )
                .isEqualTo(2)
        }

        @Test
        internal fun `part 1`() {
            assertThat(
                input.parse().count { theyOverlap(it.first, it.second) },
            )
                .isEqualTo(2)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            assertThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day04.txt")}

    private fun String.parse(): List<Pair<IntRange, IntRange>> {
        return lines()
            .map { it.split(',') }
            .map { (first, second) -> first.toIntRange() to second.toIntRange() }
    }

    private fun String.toIntRange(): IntRange {
        return split('-')
            .let { (first, second) ->
                first.toInt()..second.toInt()
            }
    }
}
