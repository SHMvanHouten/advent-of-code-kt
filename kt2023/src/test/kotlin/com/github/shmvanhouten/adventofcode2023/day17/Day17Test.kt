package com.github.shmvanhouten.adventofcode2023.day17

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.gridTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day17Test {

    private val example = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
        """.trimIndent()


    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val grid = gridTo(example) { it.digitToInt() }
            expectThat(grid.coolestPath()).isEqualTo(102)
        }

        @Test
        internal fun `part 1`() {
            val grid = gridTo(input) { it.digitToInt() }
            expectThat(grid.coolestPath()).isEqualTo(1099)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val grid = gridTo(example) { it.digitToInt() }
            expectThat(grid.ultraPath()).isEqualTo(94)
        }

        @Test
        internal fun `part 2`() {
            val grid = gridTo(input) { it.digitToInt() }
            expectThat(grid.ultraPath()).isEqualTo(1266)
        }
    }

    private val input by lazy { readFile("/input-day17.txt")}

}
