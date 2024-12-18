package com.github.shmvanhouten.adventofcode2024.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day18Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(shortestPathAfter(example, 12, 6)).isEqualTo(22)
        }

        @Test
        internal fun `part 1`() {
            val shortestPath = shortestPathAfter(input, 1024, 70)
            expectThat(shortestPath).isEqualTo(292)
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
