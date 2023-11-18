package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day14Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun parse() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            assertThat(draw(parse(input), hit = '#', miss = '.'))
                .isEqualTo(
                """
                    ....#...##
                    ....#...#.
                    ..###...#.
                    ........#.
                    ........#.
                    #########.
                """.trimIndent()
            )
        }

        @Test
        internal fun `the first tick the sand falls straight down`() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            val cave = Cave(parse(input))
            val sandLocations = cave.tick().sandLocations
            assertThat(sandLocations).hasSize(1)
            assertThat(sandLocations.first()).isEqualTo(Coordinate(500, 8))
        }

        @Test
        internal fun `the second tick the sand falls straight down`() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            val cave = Cave(parse(input))
            val sandLocations = cave.tick(2).sandLocations
            assertThat(sandLocations).hasSize(2)
            assertThat(sandLocations.last()).isEqualTo(Coordinate(499, 8))
        }

        @Test
        internal fun example() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            val cave = Cave(parse(input))

            assertThat(cave.tickUntilSaturated().sandLocations)
                .hasSize(24)
        }

        @Test
        internal fun `part 1`() {
            val cave = Cave(parse(input))
            assertThat(cave.tickUntilSaturated().sandLocations)
                .hasSize(805)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            val cave = Cave(parse(input)).plusBedRock()

            assertThat(cave.tickUntilTopIsReached().sandLocations)
                .hasSize(93)
        }

        @Test
        @Disabled("Use Day14GridTest instead (this takes 5 seconds vs 100 ms)")
        internal fun `part 2`() {
            val cave = Cave(parse(input)).plusBedRock()

            assertThat(cave.tickUntilTopIsReached().sandLocations)
                .hasSize(25161)
        }
    }

    private val input by lazy { readFile("/input-day14.txt")}

}
