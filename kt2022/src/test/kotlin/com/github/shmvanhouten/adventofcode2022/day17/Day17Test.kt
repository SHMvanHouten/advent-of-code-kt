package com.github.shmvanhouten.adventofcode2022.day17

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.extremes
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day17Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `first shape is a flat beam, given only jets to the right, it lands on the right`() {
            val input = ">>>>"
            val wall = Coordinate(-1, 1)
            assertThat(draw(Cavern().simulate(input, 1) + wall)).isEqualTo("|...####|")
        }

        @Test
        internal fun `given only jets to the left, it lands on the left`() {
            val input = "<<<<"
            assertThat(draw(Cavern().simulate(input, 1))).isEqualTo("|####...|")
        }

        @Test
        internal fun `given rrrl it lands 1 block away from the right wall`() {
            val input = ">>><"
            assertThat(draw(Cavern().simulate(input, 1))).isEqualTo("|..####.|")
        }

        @Test
        internal fun `the next block is the plus`() {
            val input = ">>>>>>>>"
            assertThat(draw(Cavern().simulate(input, 2)))
                .isEqualTo("""
                    |.....#.|
                    |....###|
                    |.....#.|
                    |...####|""".trimIndent())
        }

        @Test
        internal fun `the plus drops 1 lower if it is blown to the left but the beam is to the right`() {
            val input = ">>>><<<<<"
            assertThat(draw(Cavern().simulate(input, 2)))
                .isEqualTo("""
                    |.#.....|
                    |###....|
                    |.#.####|""".trimIndent())
        }

        @Test
        internal fun `plus cannot move into the beam`() {
            val input = ">>><<><<>"
            assertThat(draw(Cavern().simulate(input, 2)))
                .isEqualTo("""
                    |.#.....|
                    |###....|
                    |.#####.|""".trimIndent()
                )
        }

        @Test
        internal fun `same goes for moving to the left`() {
            val input = "<<<>>>>><"
            assertThat(draw(Cavern().simulate(input, 2)))
                .isEqualTo("""
                    |.....#.|
                    |....###|
                    |.#####.|""".trimIndent()
                )
        }

        @Test
        internal fun `next block is backwards L`() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
            assertThat(draw(Cavern().simulate(input, 3)))
                .isEqualTo("""
                    |..#....|
                    |..#....|
                    |####...|
                    |..###..|
                    |...#...|
                    |..####.|
                """.trimIndent()
                )
        }

        @Test
        internal fun `example drawn`() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
            assertThat(draw(Cavern().simulate(input, 10)))
                .isEqualTo("""
                    |....#..|
                    |....#..|
                    |....##.|
                    |##..##.|
                    |######.|
                    |.###...|
                    |..#....|
                    |.####..|
                    |....##.|
                    |....##.|
                    |....#..|
                    |..#.#..|
                    |..#.#..|
                    |#####..|
                    |..###..|
                    |...#...|
                    |..####.|
                """.trimIndent())
        }

        @Test
        internal fun example() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
            assertThat(Cavern().simulate(input, 2022).maxOf { it.y })
                .isEqualTo(3068)
        }

        @Test
        internal fun `part 1`() {
            assertThat(Cavern().simulate(input, 2022).maxOf { it.y })
                .isEqualTo(3068)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            assertThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day17.txt")}

}

fun draw(coordinates: Collection<Coordinate>, hit: Char = '#', miss: Char = '.'): String {
    val (minY, maxY) = coordinates.map { it.y }.extremes() ?: error("empty collection $coordinates")
    val (minX, maxX) = 0 to 6
    return maxY.downTo(minY).joinToString("\n") { y ->
        (minX..maxX).map { x ->
            if (coordinates.contains(Coordinate(x, y))) hit
            else miss
        }.joinToString("", prefix = "|", postfix = "|")
    }
}
