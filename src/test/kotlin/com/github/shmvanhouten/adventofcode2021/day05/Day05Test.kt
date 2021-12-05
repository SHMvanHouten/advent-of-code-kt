package com.github.shmvanhouten.adventofcode2021.day05

import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day05Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val coordinates = toNonDiagonalVentMap(exampleInput)
            val overlapping = coordinates.filter { it.value > 1 }
                .count()
            assertThat(overlapping, equalTo(5))
        }

        @Test
        internal fun `part 1`() {
            val coordinates = toNonDiagonalVentMap(input)
            val overlapping = coordinates.filter { it.value > 1 }
                .count()
            assertThat(overlapping, equalTo(4873))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy {readFile("/input-day05.txt")}
    private val exampleInput = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2"""

}
