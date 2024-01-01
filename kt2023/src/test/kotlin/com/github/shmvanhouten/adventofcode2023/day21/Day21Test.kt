package com.github.shmvanhouten.adventofcode2023.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.compositenumber.primeFactors
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day21Test {

    private val example = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(charGrid(example).takeSteps(6)).hasSize(16)
            expectThat(charGrid(example).takeSteps(50)).hasSize(42)
            expectThat(charGrid(example).takeSteps(51)).hasSize(39)
        }

        @Test
        internal fun `part 1`() {
            println(input)
            expectThat(charGrid(input).takeSteps(64)).hasSize(3594)
        }
    }

    @Nested
    inner class Part2 {

            // there are no rocks at the edges (for both example and input)
            // at some point steps will tick-tock between 42 and 39 (there are 81 '.'s)
            // returning steps at the edge will always clash with a step from inside, so we don't need to look back

            // the middle line is not blocked!
            // so we need the time: to get from S to each edge (for input, S is exactly in the middle)

            // because of the open cross in the middle: (mandelbrot!)
            //     |
            //    -+-
            //     |
            // When next step after middle fills out:
            //     |
            //    \+/
            //   -+x+-
            //    /+\
            //     |
            //  topLeft starts getting filled out from bottom right (etc.)
            //  most left start getting filled out from middle right (etc.)
            //     |
            //    \+/
            //   \+x+/
            //  -+xxx+-
            //   /+x+\
            //    /+\
            //     |

            //
            //     |
            //    \+/
            //   \+x+/
            //  \+xxx+/
            // -+xxxxx+-
            //  /+xxx+\
            //   /+x+\
            //    /+\
            //     |

        @Test
        fun `an example where the middle row and column are clear like in the input`() {
            val map = """
                _________
                _##..###_
                _.#....._
                _#.....#_
                _......._
                _##....._
                _..#...#_
                _#......_
                _________
            """.trimIndent()
            val biggerMap = map.expand(7)
            val grid = charGrid(biggerMap)

            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(30).first()
                .also { println(printWithCoordinates(grid, it)) }
                .size
//                .take(20)
//                .onEach {
//                    println(printWithCoordinates(grid, it))
//                }
            expectThat(takeStepsOnInfinitelyRepeating(30, charGrid(map.replace('_', '.'))))
                .isEqualTo(expected.toLong())
        }

        @ParameterizedTest
        @ValueSource(ints = [48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100])
        fun `an example where the middle row and column are clear like in the input x times`(value: Int) {
            val map = """
                _________
                _##..###_
                _.#....._
                _#.....#_
                _......._
                _##....._
                _..#...#_
                _#......_
                _________
            """.trimIndent()
            val biggerMap = map.expand(57)
            val grid = charGrid(biggerMap)

            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(value).first()
                .size
            expectThat(takeStepsOnInfinitelyRepeating(value, charGrid(map.replace('_', '.'))))
                .isEqualTo(expected.toLong())
        }

        @Test
        fun `an example where the middle row and column are clear like in the input 29 times`() {
            val map = """
                _________
                _##..###_
                _.#....._
                _#.....#_
                _......._
                _##....._
                _..#...#_
                _#......_
                _________
            """.trimIndent()
            val biggerMap = map.expand(7)
            val grid = charGrid(biggerMap)

            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(20).first()
                .also { println(printWithCoordinates(grid, it)) }
                .size
            expectThat(takeStepsOnInfinitelyRepeating(20, charGrid(map.replace('_', '.'))))
                .isEqualTo(expected.toLong())
        }

        @Test
        fun `an example where the middle row and column are clear like in the input 51 times`() {
            val map = """
                _________
                _##..###_
                _.#....._
                _#.....#_
                _......._
                _##....._
                _..#...#_
                _#......_
                _________
            """.trimIndent()
            val biggerMap = map.expand(13)
            val grid = charGrid(biggerMap)

            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(51).first()
                .also { println(printWithCoordinates(grid, it)) }
                .size
            expectThat(takeStepsOnInfinitelyRepeating(51, charGrid(map.replace('_', '.'))))
                .isEqualTo(expected.toLong())
        }

        @Test
        @Disabled
        fun `lets look at the outputs`() {
            val grid = charGrid(input.expand(101))
            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(65).withIndex()
                .filter { it.index % 131 == 0 }
//                .first().value.size.also { println(it) }
                .map { it.value.size }
                .zipWithNext()
//                .map { it.second - it .first }
                .zipWithNext()
//                .forEach { println(it) }
                .forEach { println("${it.first} -> ${it.second} 29578") }
        }

        @Test
        fun `part 2 - increases by 29578 every 131 steps`() {


            println(26501365 / 131) // 202300
            println(26501365 % 131)
            println(primeFactors(29578))
//            val grid = charGrid(input.expand(3))
//            generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
//                grid.takeStep(steppedOn).first
//            }.drop(65).first().count().also(::println)
            // at 65 we are at 3755
            // 33494 - 3755 = 29.739
            // 92811 - 33494 = 59.317     59317 - 29739 = 29587
            // 181706 - 92811 = 88.895
            //(181706, 300179) -> (300179, 448230) 29578
            //(300179, 448230) -> (448230, 625859) 29578

            // 202300 * 131 * 3755 * 4 = 2912534768

            // 2b - a + 29578
            expectThat(generateSequence(3755L to 33494L) { (a, b) ->
                b to (2 * b - a + 29578)
            }.drop(202300).first().first).isEqualTo(605247138198755)
        }

        @Test
        internal fun `part 2`() {
            expectThat(takeStepsOnInfinitelyRepeating(26501365, charGrid(input)))
                .isEqualTo(605247138198755)
        }
    }


    private val input by lazy { readFile("/input-day21.txt")}

}

private fun String.expand(i: Int): String {
    val line = this.lines().map { it.repeat(i) }.joinToString("\n") + "\n"
    return line.repeat(i).trim()
}
