package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day10Test {

    @Nested
    inner class Part1 {


        @Test
        internal fun `example 1`() {
            val example = """
                .....
                .S-7.
                .|.|.
                .L-J.
                .....
            """.trimIndent()
            val grid = charGrid(example)
            expectThat(farthestAwayFromStart(grid, Direction.EAST)).isEqualTo(4)
        }

        @Test
        internal fun `example 2`() {
            val example = """
                7-F7-
                .FJ|7
                SJLL7
                |F--J
                LJ.LJ
            """.trimIndent()
            val grid = charGrid(example)
            expectThat(farthestAwayFromStart(grid, Direction.EAST)).isEqualTo(8)
        }

        @Test
        internal fun `part 1`() {
            val grid = charGrid(input)
            expectThat(farthestAwayFromStart(grid)).isEqualTo(7102)
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

    private val input by lazy { readFile("/input-day10.txt")}

}
