package com.github.shmvanhouten.adventofcode2023.day11

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromPicture
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day11Test {

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


    @Nested
    inner class Part1 {
        @Test
        internal fun `example 1`() {
            val universe = boolGridFromPicture(example, '#')
            val expandedUniverse = universe.expand()
            println(expandedUniverse.map { if (it) '#' else '.' })

            expectThat(universe.countPathsBetweenAllGalaxies()).isEqualTo(374)
        }

        @Test
        internal fun `part 1`() {
            val universe = boolGridFromPicture(input, '#')
            expectThat(universe.countPathsBetweenAllGalaxies()).isEqualTo(9556896)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            val universe = boolGridFromPicture(example, '#')

            expectThat(universe.countPathsBetweenAllGalaxies(9)).isEqualTo(1030)
            expectThat(universe.countPathsBetweenAllGalaxies(99)).isEqualTo(8410)
        }

        @Test
        internal fun `part 2`() {
            val universe = boolGridFromPicture(input, '#')

            expectThat(universe.countPathsBetweenAllGalaxies(1000000 - 1)).isEqualTo(685038186836)
        }
    }

    private val input by lazy { readFile("/input-day11.txt")}

}
