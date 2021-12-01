package com.github.shmvanhouten.adventofcode2021.day01

import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day01Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `count the numbers that are higher than the previous`() {
            assertThat(
                countNumberOfIncreases(listOf(0)),
                equalTo(0)
            )
            assertThat(
                countNumberOfIncreases(listOf(0, 1)),
                equalTo(1)
            )
            assertThat(
                countNumberOfIncreases(listOf(0, 1, 0)),
                equalTo(1)
            )
        }

        @Test
        internal fun `example 1`() {
            val input = listOf(
                199L,
                200,
                208,
                210,
                200,
                207,
                240,
                269,
                260,
                263
            )
            assertThat(
                countNumberOfIncreases(input),
                equalTo(7)
            )
        }

        @Test
        internal fun `part 1`() {
            val input = readFile("/input-day01.txt").lines().map { it.toLong() }
            assertThat(
                countNumberOfIncreases(input),
                equalTo(1696)
            )
        }
    }

    @Nested
    inner class Part_2 {
        @Test
        internal fun `example 2`() {
            val input = listOf(
                199L,
                200,
                208,
                210,
                200,
                207,
                240,
                269,
                260,
                263
            )
            assertThat(
                countNumberOfIncreasesFor3SlidingWindow(input),
                equalTo(5)
            )
        }

        @Test
        internal fun `part 2`() {
            val input = readFile("/input-day01.txt").lines().map { it.toLong() }
            assertThat(
                countNumberOfIncreasesFor3SlidingWindow(input),
                equalTo(1737)
            )
        }
    }

}
