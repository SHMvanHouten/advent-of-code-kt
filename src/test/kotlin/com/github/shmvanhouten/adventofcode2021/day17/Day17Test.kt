package com.github.shmvanhouten.adventofcode2021.day17

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day17Test {

    @Nested
    inner class Part1 {

        @Nested
        inner class Calculate_min_x_and_max_x_velocity {

            // max x velocity is always the max x
            @ParameterizedTest
            @CsvSource(
                value = [
                    "1, 1",
                    "2, 2",
                    "3, 2",
                    "18, 6",
                    "225, 21",
                    "207, 20",  // actual input
                    "20, 6", // example
                    "39320, 280",
                    "123123, 496"
                ]
            )
            internal fun `reaching target in range 1--2 would require a minX velocity of 1`(
                minX: Int,
                expectedMinXVelocity: Int
            ) {
                assertThat(
                    calculateMinXVelocityAndMaxXVelocity(minX, Int.MAX_VALUE).first,
                    equalTo(expectedMinXVelocity)
                )
            }
        }

        @Test
        internal fun example_1() {
            val (_, yRange) = example.parse()
            val (_, maxYVelocity) = calculateMinAndMaxYVelocityPossible(yRange)
            assertThat(maxYVelocity, equalTo(9))
            assertThat(calculateMaxHeight(maxYVelocity), equalTo(45))
            assertThat(calculateMaxHeight(1), equalTo(1))
            assertThat(calculateMaxHeight(2), equalTo(3))
            assertThat(calculateMaxHeight(3), equalTo(6))
            assertThat(calculateMaxHeight(4), equalTo(10))
            assertThat(calculateMaxHeight(5), equalTo(15))
            assertThat(calculateMaxHeight(6), equalTo(21))
            assertThat(calculateMaxHeight(7), equalTo(28))
            assertThat(calculateMaxHeight(8), equalTo(36))
            assertThat(calculateMaxHeight(9), equalTo(45))
            assertThat(calculateMaxHeight(10), equalTo(55))
            assertThat(calculateMaxHeight(11), equalTo(66))
            assertThat(calculateMaxHeight(12), equalTo(78))
        }

        @Test
        internal fun `part 1`() {
            val (xRange, yRange) = input.parse()
            val (_, maxYVelocity) = calculateMinAndMaxYVelocityPossible(yRange)
            assertThat(maxYVelocity, equalTo(114))
            assertThat(calculateMaxHeight(maxYVelocity), equalTo(6555))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val (xRange, yRange) = example.parse()
            val (minX, maxX) = calculateMinXVelocityAndMaxXVelocity(xRange.first, xRange.last)
            val (minY, maxY) = calculateMinAndMaxYVelocityPossible(yRange)
            val trajectories = filterVelocitiesThatEndUpInRange(minX..maxX, minY..maxY, xRange, yRange)
            assertThat(trajectories.size, equalTo(112))
        }

        @Test
        internal fun `part 2`() {
            val (xRange, yRange) = input.parse()
            val (minX, maxX) = calculateMinXVelocityAndMaxXVelocity(xRange.first, xRange.last)
            val (minY, maxY) = calculateMinAndMaxYVelocityPossible(yRange)
            val trajectories = filterVelocitiesThatEndUpInRange(minX..maxX, minY..maxY, xRange, yRange)
            assertThat(trajectories.size, equalTo(4973))
        }
    }

    private val input by lazy { readFile("/input-day17.txt") }
    private val example = "target area: x=20..30, y=-10..-5"

}

private fun String.parse(): Pair<IntRange, IntRange> {
    val words = this.words()
    return words[2].substring(0, words[2].lastIndex).toRange() to
            words[3].substring(0, words[3].length).toRange()
}

private fun String.toRange(): IntRange {
    val start = this.substring(this.indexOf('=') + 1, this.indexOf('.'))
    val end = this.substring(this.indexOf("..") + 2)
    return start.toInt()..end.toInt()
}

private fun String.words(): List<String> {
    return this.split(' ')
}
