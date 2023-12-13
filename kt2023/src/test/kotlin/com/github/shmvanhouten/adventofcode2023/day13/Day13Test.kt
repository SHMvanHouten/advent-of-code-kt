package com.github.shmvanhouten.adventofcode2023.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day13Test {

    private val example = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `first example is mirrored vertically at column 5, giving it a value of 5`() {
            val input = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
            """.trimIndent()
            expectThat(getReflectionValue(input)).isEqualTo(5)
        }

        @Test
        fun `second example is mirrored horizontally at 4, giving it a value of 400`() {
            val example = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
            """.trimIndent()
            expectThat(getReflectionValue(example)).isEqualTo(400)
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.blocks().sumOf { getReflectionValue(it) })
                .isEqualTo(35210)
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

    private val input by lazy { readFile("/input-day13.txt")}

}
