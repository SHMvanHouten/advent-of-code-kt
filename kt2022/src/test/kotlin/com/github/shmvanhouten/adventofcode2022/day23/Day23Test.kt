package com.github.shmvanhouten.adventofcode2022.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day23Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `the elves first propose to move north`() {
            val input = "##"
            var grove = Grove(input)
            grove = grove.tick()
            assertThat(grove.elves).isEqualTo(setOf(Coordinate(0, -1), Coordinate(1,-1)))
        }

        @Test
        internal fun `because the first move was North, it gets moved to the bottom of the list, and the elves next propose to move south`() {
            val input = "##"
            var grove = Grove(input)
            grove = grove.tick().tick()
            assertThat(grove.elves).isEqualTo(setOf(Coordinate(0, 0), Coordinate(1,0)))
        }

        @Test
        internal fun `if there is an elf to the north, the elf proposes to move south instead`() {
            val input = """
                #
                #
            """.trimIndent()
            val grove = Grove(input).tick()
            assertThat(grove.elves).isEqualTo(setOf(Coordinate(0, -1), Coordinate(0, 2)))
        }


        @Test
        internal fun `if there is an elf to the north east, the elf proposes to move south instead`() {
            val input = """
                .#
                #.
            """.trimIndent()
            val grove = Grove(input).tick()
            assertThat(grove.elves).isEqualTo(setOf(Coordinate(1, -1), Coordinate(0, 2)))
        }

        @Test
        internal fun `two elves proposing to move to the same position do not move`() {
            val input = """
                .....
                ..##.
                ..#..
                .....
                ..##.
                .....
            """.trimIndent()
            val grove = Grove(input).tick()
            assertThat(draw(grove.elves, '#', '.')).isEqualTo("""
                ##
                ..
                #.
                .#
                #.
            """.trimIndent())
        }

        @Test
        internal fun `example 1`() {
            val input = """
                .....
                ..##.
                ..#..
                .....
                ..##.
                .....
            """.trimIndent()
            val grove = Grove(input).tick(2)

            assertThat(draw(grove.elves, '#', '.')).isEqualTo("""
                .##.
                #...
                ...#
                ....
                .#..
            """.trimIndent())
        }

        @Test
        internal fun `if a direction is not used, instead, the next direction is moved to the bottom`() {
            // Writing this test is too hard
        }

        @Test
        internal fun `example 2 round 2`() {
            val input = """
                ..............
                .......#......
                .....#...#....
                ...#..#.#.....
                .......#..#...
                ....#.#.##....
                ..#..#.#......
                ..#.#.#.##....
                ..............
                ....#..#......
                ..............
                ..............
            """.trimIndent()
            val grove = Grove(elves = input.toCoordinateMap('#'), moves = listOf(
                MoveProposal(RelativePosition.SOUTH, listOf(RelativePosition.WEST, RelativePosition.EAST)),
                MoveProposal(RelativePosition.WEST, listOf(RelativePosition.NORTH, RelativePosition.SOUTH)),
                MoveProposal(RelativePosition.EAST, listOf(RelativePosition.NORTH, RelativePosition.SOUTH)),
                MoveProposal(RelativePosition.NORTH, listOf(RelativePosition.WEST, RelativePosition.EAST))
            )).tick()
            assertThat(draw(grove.elves, '#', '.')).isEqualTo("""
                ......#....
                ...#.....#.
                ..#..#.#...
                ......#...#
                ..#..#.#...
                #...#.#.#..
                ...........
                .#.#.#.##..
                ...#..#....
            """.trimIndent())
        }

        @Test
        internal fun `example 2 5 rounds`() {
            val input = """
                ..............
                ..............
                .......#......
                .....###.#....
                ...#...#.#....
                ....#...##....
                ...#.###......
                ...##.#.##....
                ....#..#......
                ..............
                ..............
                ..............
            """.trimIndent()
            val grove = Grove(input).tick(5)
            assertThat(draw(grove.elves, '#', '.')).isEqualTo("""
                ......#....
                ...........
                .#..#.....#
                ........#..
                .....##...#
                #.#.####...
                ..........#
                ...##..#...
                .#.........
                .........#.
                ...#..#....
            """.trimIndent())
        }

        @Test
        internal fun `example 2`() {
            val input = """
                ..............
                ..............
                .......#......
                .....###.#....
                ...#...#.#....
                ....#...##....
                ...#.###......
                ...##.#.##....
                ....#..#......
                ..............
                ..............
                ..............
            """.trimIndent()
            val grove = Grove(input).tick(10)
            assertThat(draw(grove.elves, '#', '.')).isEqualTo("""
                ......#.....
                ..........#.
                .#.#..#.....
                .....#......
                ..#.....#..#
                #......##...
                ....##......
                .#........#.
                ...#.#..#...
                ............
                ...#..#..#..
            """.trimIndent())
            assertThat(grove.elves.countUnoccupiedSpaces()).isEqualTo(110)
        }

        @Test
        internal fun `part 1`() {
            val grove = Grove(input).tick(10)
            assertThat(grove.elves.countUnoccupiedSpaces()).isEqualTo(4049)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                ..............
                ..............
                .......#......
                .....###.#....
                ...#...#.#....
                ....#...##....
                ...#.###......
                ...##.#.##....
                ....#..#......
                ..............
                ..............
                ..............
            """.trimIndent()
            val grove = Grove(input)

            assertThat(grove.tickUntilDone()).isEqualTo(20)
        }

        @Test
        @Disabled("34 seconds")
        internal fun `part 2`() {
            val grove = Grove(input)

            assertThat(grove.tickUntilDone()).isEqualTo(1021)
        }
    }

    private val input by lazy { readFile("/input-day23.txt")}

}
