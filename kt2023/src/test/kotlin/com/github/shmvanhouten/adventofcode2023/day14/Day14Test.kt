package com.github.shmvanhouten.adventofcode2023.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLessThan

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
                    .....#....
                    ....#....#
                    ...O.##...
                    ...#......
                    O.O....O#O
                    O.#..O.#.#
                    O....#....
                    OO....OO..
                    #OO..###..
                    #OO.O#...O
                """.trimIndent()).toString())
        }

        @Test
        fun `tilt west`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(grid.tiltWest().toString())
                .isEqualTo(charGrid("""
                    O....#....
                    OOO.#....#
                    .....##...
                    OO.#OO....
                    OO......#.
                    O.#O...#.#
                    O....#OO..
                    O.........
                    #....###..
                    #OO..#....
                """.trimIndent()).toString())
        }

        @Test
        fun `tilt east`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(grid.tiltEast().toString())
                .isEqualTo(charGrid("""
                    ....O#....
                    .OOO#....#
                    .....##...
                    .OO#....OO
                    ......OO#.
                    .O#...O#.#
                    ....O#..OO
                    .........O
                    #....###..
                    #..OO#....
                """.trimIndent()).toString())
        }

        @Test
        fun `spin once`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(spin(grid, 1).toString())
                .isEqualTo(charGrid("""
                    .....#....
                    ....#...O#
                    ...OO##...
                    .OO#......
                    .....OOO#.
                    .O#...O#.#
                    ....O#....
                    ......OOOO
                    #...O###..
                    #..OO#....
                """.trimIndent()).toString())
        }

        @Test
        fun `spin twice`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(spin(grid, 2).toString())
                .isEqualTo(charGrid("""
                    .....#....
                    ....#...O#
                    .....##...
                    ..O#......
                    .....OOO#.
                    .O#...O#.#
                    ....O#...O
                    .......OOO
                    #..OO###..
                    #.OOO#...O
                """.trimIndent()).toString())
        }

        @Test
        fun `spin three times`() {
            val grid = charGrid(example).toMutableGrid()
            expectThat(spin(grid, 3).toString())
                .isEqualTo(charGrid("""
                    .....#....
                    ....#...O#
                    .....##...
                    ..O#......
                    .....OOO#.
                    .O#...O#.#
                    ....O#...O
                    .......OOO
                    #...O###.O
                    #.OOO#...O
                """.trimIndent()).toString())
        }

        @Test
        fun `compare smart to regular`() {
            val grid = charGrid(example)
            expectThat(spinSmart(grid, 281).toString())
                .isEqualTo(spin(grid.toMutableGrid(), 281).toString())
        }

        @Test
        fun `spin 1000000000 times`() {
            val grid = charGrid(example)
            expectThat(spinSmart(grid, 1000000000).calculateLoad())
                .isEqualTo(64)
        }

        @Test
        internal fun `part 2`() {
            val grid = charGrid(input)
            expectThat(spinSmart(grid, 1000000000).calculateLoad())
                .isLessThan(96064)
                .isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day14.txt")}

}
