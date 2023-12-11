package com.github.shmvanhouten.adventofcode2023.day11

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromPicture
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day11Test {

    @Nested
    inner class Part1 {

        val example = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()
        @Test
        internal fun `fixme`() {
            val universe = boolGridFromPicture(example, '#')
            val expandedUniverse = universe.expand()
//            println(expandedUniverse.map { if (it) '#' else '.' })
            expectThat(expandedUniverse.countPathsBetweenAllGalaxies()).isEqualTo(374)
        }

        @Test
        internal fun `part 1`() {
            val universe = boolGridFromPicture(input, '#')
            val expandedUniverse = universe.expand()
//            println(expandedUniverse.map { if (it) '#' else '.' })
            expectThat(expandedUniverse.countPathsBetweenAllGalaxies()).isEqualTo(9556896)
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

    private val input by lazy { readFile("/input-day11.txt")}

}
