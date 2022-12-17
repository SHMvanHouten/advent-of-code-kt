package com.github.shmvanhouten.adventofcode2022.day17

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
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
                .isEqualTo(3166)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example pattern should repeat at some point`() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
            val drawn = draw(Cavern().simulate(input, 150))
            val startOfRepeatingBlock = "|.######|"
            val repeatingBlock = (drawn.substringAfter(startOfRepeatingBlock)
                .substringBefore(startOfRepeatingBlock) + startOfRepeatingBlock).trim()
            assertThat(drawn).contains(repeatingBlock)
            assertThat(drawn).contains(repeatingBlock + "\n" + repeatingBlock)
            assertThat(drawn).contains(repeatingBlock + "\n" + repeatingBlock + "\n" + repeatingBlock)
        }

        @Test
        internal fun `example_ at block 37 and 38 the pattern starts repeating`() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

            val drawn37 = draw(Cavern().simulate(input, 37))
            assertThat(drawn37.lines()).doesNotContain("|.######|")

            val drawn38 = draw(Cavern().simulate(input, 38))
            assertThat(drawn38.lines()).contains("|.######|") // + on the right + reverse L next to it
        }

        @Test
        internal fun `then at block 72 the pattern repeats again`() {
            val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

            val drawn72 = draw(Cavern().simulate(input, 73, "|.######|"))
            assertThat(drawn72.lines().count { it == "|.######|" }).isEqualTo(2)
        }

        @Test
        internal fun example() {

            val result = calculateBlockHeightForBigTarget(
                input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>",
                repeatsAt = 37,
                repeatsAtAgain = 72,
                startOfRepeatingBlock = "|.######|"
            )

            assertThat(result).isEqualTo(1514285714288L)
        }

        private val startOfRepeating = """
            |###..#.|
            |.#####.|
            |..###..|
            |..###..|
            |..#.#..|
            |..#.#..|
        """.trimIndent()
        @Test
        @Disabled("slow, and only used for nearing in on the answer")
        internal fun findActualInputRepeating() {
            val drawn = draw(Cavern().simulate(input, 11000, startOfRepeating))
            println(drawn)
            println()
        }

        @Test
        internal fun `part 2`() {

            val result = calculateBlockHeightForBigTarget(
                input,
                repeatsAt = 284,
                repeatsAtAgain = 2039,
                startOfRepeatingBlock = startOfRepeating
            )

            val expected = 1577207977186L
            assertThat(result).isEqualTo(expected)
        }
    }

    private val input by lazy { readFile("/input-day17.txt")}

}
