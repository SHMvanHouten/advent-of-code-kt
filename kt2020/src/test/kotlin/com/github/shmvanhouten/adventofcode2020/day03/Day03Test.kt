package com.github.shmvanhouten.adventofcode2020.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day03Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(Slope(example).traverseDown(3).encounteredTrees).isEqualTo(7)
        }

        @Test
        internal fun `part 1`() {
            expectThat(Slope(input).traverseDown(3).encounteredTrees).isEqualTo(164)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2 single down traverse`() {
            expect {
                that(Slope(example).traverseDown(1).encounteredTrees).isEqualTo(2)
                that(Slope(example).traverseDown(3).encounteredTrees).isEqualTo(7)
                that(Slope(example).traverseDown(5).encounteredTrees).isEqualTo(3)
                that(Slope(example).traverseDown(7).encounteredTrees).isEqualTo(4)
            }
        }

        @Test
        fun `traversing down two at a time`() {
            expectThat(Slope(example).traverseDown(1, 2).encounteredTrees).isEqualTo(2)
        }

        @Test
        fun `example 2`() {
            expectThat(Slope(example).multiplyTraversalCombinations()).isEqualTo(336L)
        }

        @Test
        internal fun `part 2`() {
            expectThat(Slope(input).multiplyTraversalCombinations()).isEqualTo(5007658656L)
        }
    }

    private val input by lazy { readFile("/input-day03.txt")}

    private val example = """
        ..##.......
        #...#...#..
        .#....#..#.
        ..#.#...#.#
        .#...##..#.
        ..#.##.....
        .#.#.#....#
        .#........#
        #.##...#...
        #...##....#
        .#..#...#.#
    """.trimIndent()
}
