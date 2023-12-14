package com.github.shmvanhouten.adventofcode2023.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day14Test {

    private val example = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()

    @Nested
    inner class Part1 {


        @Test
        internal fun `example 1`() {

            val tilted = tiltNorth(charGrid(example))
            expectThat(tilted.toString())
                .isEqualTo(charGrid("""
                OOOO.#.O..
                OO..#....#
                OO..O##..O
                O..#.OO...
                ........#.
                ..#....#.#
                ..O..#.O.O
                ..O.......
                #....###..
                #....#....
            """.trimIndent()).toString())
            expectThat(tilted.calculateLoad())
                .isEqualTo(136)
        }

        @Test
        internal fun `part 1`() {
            val tilted = tiltNorth(charGrid(input))
            expectThat(tilted.calculateLoad())
                .isEqualTo(107951)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `tilt south`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(grid.tiltSouth().toString())
                .isEqualTo(charGrid("""
                ...O.#.O..
                ....#....#
                ....O##..O
                ...#.OO...
                O.O.....#.
                O.#....#.#
                O....#.O.O
                OO........
                #OO..###..
                #OO..#....
            """.trimIndent()).toString())
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day14.txt")}

}
