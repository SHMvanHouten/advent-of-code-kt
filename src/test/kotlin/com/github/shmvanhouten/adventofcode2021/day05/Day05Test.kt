package com.github.shmvanhouten.adventofcode2021.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
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
            val overlapping = countOverlappingLocations(coordinates)
            assertThat(overlapping, equalTo(5))
        }

        @Test
        internal fun `part 1`() {
            val coordinates = toNonDiagonalVentMap(input)
            val overlapping = countOverlappingLocations(coordinates)
            assertThat(overlapping, equalTo(4873))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val coordinates = toVentMap(exampleInput)
            val overlapping = countOverlappingLocations(coordinates)
            assertThat(overlapping, equalTo(12))
        }

        @Test
        internal fun `part 2`() {
            val coordinates = toVentMap(input)
            val overlapping = countOverlappingLocations(coordinates)
            assertThat(overlapping, equalTo(19472))
        }
    }

    private val input by lazy { readFile("/input-day05.txt") }
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
