package com.github.shmvanhouten.adventofcode2023.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
        fun `draw me a box of 5`() {
            val map = """
                _____
                _#.#_
                _..._
                _#.#_
                _____
            """.trimIndent()
            val biggerMap = map.expand(15)
            val grid = charGrid(biggerMap)

            val expected = generateSequence(setOf(grid.middleCoordinate())) { steppedOn ->
                grid.takeStep(steppedOn).first
            }.drop(30).first()
                .also { println(printWithCoordinates(grid, it)) }
                .size
        }

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

        @Test
        fun `an example where the middle row and column are clear like in the input 48 times`() {
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
            }.drop(48).first()
                .also { println(printWithCoordinates(grid, it)) }
                .size
//                .take(20)
//                .onEach {
//                    println(printWithCoordinates(grid, it))
//                }``````````````````````````
            expectThat(takeStepsOnInfinitelyRepeating(48, charGrid(map.replace('_', '.'))))
                .isEqualTo(expected.toLong())
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }


    private val input by lazy { readFile("/input-day21.txt")}

}

private fun String.expand(i: Int): String {
    val line = this.lines().map { it.repeat(i) }.joinToString("\n") + "\n"
    return line.repeat(i).trim()
}
