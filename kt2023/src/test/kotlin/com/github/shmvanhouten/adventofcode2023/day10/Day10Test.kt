package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
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
            expectThat(farthestAwayFromStart(grid)).isEqualTo(4)
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
            expectThat(farthestAwayFromStart(grid)).isEqualTo(8)
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
        internal fun `enclosed area example 1`() {
            val input = """
                ...........
                .S-------7.
                .|F-----7|.
                .||.....||.
                .||.....||.
                .|L-7.F-J|.
                .|..|.|..|.
                .L--J.L--J.
                ...........
            """.trimIndent()
            expectThat(countEnclosedTiles(charGrid(input))).isEqualTo(4)
        }

        @Test
        internal fun `enclosed area example 2`() {
            val input = """
                .F----7F7F7F7F-7....
                .|F--7||||||||FJ....
                .||.FJ||||||||L7....
                FJL7L7LJLJ||LJ.L-7..
                L--J.L7...LJS7F-7L7.
                ....F-J..F7FJ|L7L7L7
                ....L7.F7||L7|.L7L7|
                .....|FJLJ|FJ|F7|.LJ
                ....FJL-7.||.||||...
                ....L---J.LJ.LJLJ...
            """.trimIndent()
            expectThat(countEnclosedTiles(charGrid(input))).isEqualTo(8)
        }

        @Test
        internal fun `enclosed area example 3`() {
            val input = """
                FF7FSF7F7F7F7F7F---7
                L|LJ||||||||||||F--J
                FL-7LJLJ||||||LJL-77
                F--JF--7||LJLJ7F7FJ-
                L---JF-JLJ.||-FJLJJ7
                |F|F-JF---7F7-L7L|7|
                |FFJF7L7F-JF7|JL---7
                7-L-JL7||F7|L7F-7F7|
                L.L7LFJ|||||FJL7||LJ
                L7JLJL-JLJLJL--JLJ.L
            """.trimIndent()
            expectThat(countEnclosedTiles(charGrid(input))).isEqualTo(10)
        }

        @Test
        internal fun `part 2`() {
            expectThat(countEnclosedTiles(charGrid(input)))
                .isEqualTo(363)
        }
    }

    private val input by lazy { readFile("/input-day10.txt")}

}
