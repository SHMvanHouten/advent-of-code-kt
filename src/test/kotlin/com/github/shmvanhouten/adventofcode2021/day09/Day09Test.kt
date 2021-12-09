package com.github.shmvanhouten.adventofcode2021.day09

import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.github.shmvanhouten.adventofcode2021.coordinate.toCoordinateMap
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day09Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val valuesByCoordinate = exampleInput.toCoordinateMap()
            assertThat(
                sumRiskLevelsOfLowPoints(valuesByCoordinate),
                equalTo(15)
            )
        }

        @Test
        internal fun `part 1`() {
            val valuesByCoordinate = input.toCoordinateMap()
            assertThat(
                sumRiskLevelsOfLowPoints(valuesByCoordinate),
                equalTo(491)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1))
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1))

        }
    }

    private val input by lazy { readFile("/input-day09.txt") }
    private val exampleInput = """2199943210
3987894921
9856789892
8767896789
9899965678"""
}
