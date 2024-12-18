package com.github.shmvanhouten.adventofcode2024.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse

class Day18Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(shortestPathAfter(12, 6, example.lines())).isEqualTo(22)
        }

        @Test
        internal fun `part 1`() {
            val shortestPath = shortestPathAfter(1024, 70, input.lines())
            expectThat(shortestPath).isEqualTo(292)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(findIfStillTraversable(example, 12, 6)).isEqualTo(Coordinate(6, 1))
        }

        @Test
        internal fun `part 2`() {
            // not 41,20
            expectThat(findIfStillTraversable(input, 1024, 70)).isEqualTo(Coordinate(58,44))
        }

        @Test
        fun test() {
            println(input.lines()[3011])
           expectThat(hasPath(3011, 70, input.lines())).isFalse()
        }
    }

    private val input by lazy { readFile("/input-day18.txt")}

    private val example = """
    5,4
    4,2
    4,5
    3,0
    2,1
    6,3
    2,4
    1,5
    0,6
    3,3
    2,6
    5,1
    1,2
    5,5
    2,5
    6,5
    1,4
    0,4
    6,4
    1,1
    6,1
    1,0
    0,5
    1,6
    2,0
""".trimIndent()
}
