package com.github.shmvanhouten.adventofcode2024.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day06Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(countVisitedLocations(example)).isEqualTo(41)
        }

        @Test
        internal fun `part 1`() {
            expectThat(countVisitedLocations(input)).isEqualTo(5329)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {
            expectThat(findLocationsToLoopGuard(example).count()).isEqualTo(6)
        }

        @Test
        internal fun `part 2`() {
            expectThat(findLocationsToLoopGuard(input).count()).isEqualTo(2162)
        }
    }

    private val input by lazy { readFile("/input-day06.txt")}

    private val example = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

}
