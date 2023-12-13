package com.github.shmvanhouten.adventofcode2023.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLessThan

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
        internal fun `fixing the smudge, the mirror is suddenly at row 3`() {
            val input = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.
            """.trimIndent()
            expectThat(getCleanReflectionValue(input)).isEqualTo(300)
        }

        @Test
        fun `fixing the smudge, the mirror is suddenly at row 1`() {
            val example = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#
            """.trimIndent()
            expectThat(getCleanReflectionValue(example)).isEqualTo(100)
        }

        @Test
        fun `example 2`() {
            expectThat(example.blocks().sumOf { getCleanReflectionValue(it) })
                .isEqualTo(400)
        }

        @Test
        fun `this grid should work`() {
            val example = """
                .##......
                ###.####.
                ##.##...#
                ..###..##
                ...##..##
                #..#.##.#
                ..#......
                .##..##..
                .##..##..
            """.trimIndent()
            expectThat(getCleanReflectionValue(example)).isEqualTo(6)
        }

        @Test
        internal fun `part 2`() {
            expectThat(input.blocks().sumOf { getCleanReflectionValue(it) })
                .isEqualTo(31974)
        }
    }

    private val input by lazy { readFile("/input-day13.txt")}

}
