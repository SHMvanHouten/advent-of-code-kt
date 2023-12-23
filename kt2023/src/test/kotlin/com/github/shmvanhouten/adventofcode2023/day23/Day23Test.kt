package com.github.shmvanhouten.adventofcode2023.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day23Test {

    val example = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(longestSlipperyPath(example)).isEqualTo(94)
        }

        @Test
        internal fun `part 1`() {
            expectThat(longestSlipperyPath(input)).isEqualTo(2282)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val newEx = example.replace('>', '.').replace('v', '.').replace('<', '.').replace('^', '.')
            expectThat(longestGrippyPath(newEx)).isEqualTo(154)
        }

        @Test
        internal fun `part 2`() {
            val newIn = input.replace('>', '.').replace('v', '.').replace('<', '.').replace('^', '.')
            expectThat(longestGrippyPath(newIn)).isEqualTo(6646)
        }
    }

    private val input by lazy { readFile("/input-day23.txt")}

}
