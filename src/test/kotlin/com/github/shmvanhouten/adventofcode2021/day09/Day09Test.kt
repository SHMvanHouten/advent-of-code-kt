package com.github.shmvanhouten.adventofcode2021.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toIntByCoordinateMap
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day09Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val valuesByCoordinate = exampleInput.toIntByCoordinateMap()
            assertThat(
                sumRiskLevelsOfLowPoints(valuesByCoordinate),
                equalTo(15)
            )
        }

        @Test
        internal fun `part 1`() {
            val valuesByCoordinate = input.toIntByCoordinateMap()
            assertThat(
                sumRiskLevelsOfLowPoints(valuesByCoordinate),
                equalTo(491)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val valuesByCoordinate = exampleInput.toIntByCoordinateMap()
            val basins = locateBasins(valuesByCoordinate)
            assertThat(basins.size, equalTo(4))
            assertThat(basins.map { it.size }.sorted(), equalTo(listOf(3, 9, 9, 14)))
            assertThat(multiply3LargestBasins(basins), equalTo(1134))
        }

        @Test
        internal fun `part 2`() {
            val valuesByCoordinate = input.toIntByCoordinateMap()
            val basins = locateBasins(valuesByCoordinate)
            assertThat(multiply3LargestBasins(basins), equalTo(1075536))
        }

        private fun multiply3LargestBasins(basins: Set<Set<Coordinate>>) =
            basins.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

    private val input by lazy { readFile("/input-day09.txt") }
    private val exampleInput = """2199943210
3987894921
9856789892
8767896789
9899965678"""
}
