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
            @ParameterizedTest
            @CsvSource(
                value = [
                    "1, 2, 1",
                    "2, 3, 2",
                    "3, 4, 2",
                    "18, 19, 6",
                    "225, 247, 21",
                    "207, 263, 20",  // actual input
                    "20, 30, 6" // example
                ]
            )
            internal fun `reaching target in range 1--2 would require a minX velocity of 1`(
                minX: Int,
                maxX: Int,
                expectedMinXVelocity: Int
            ) {
                assertThat(
                    calculateMinXVelocityAndMaxXVelocity(minX, maxX),
                    equalTo(expectedMinXVelocity to maxX)
                )
            }
        }

        @Test
        internal fun example_1() {
            val (xRange, yRange) = example.parse()
            val maxYVelocity = calculateMaxAndMinYVelocityPossible(xRange, yRange).first
            assertThat(maxYVelocity, equalTo(9))
            assertThat(calculateMaxHeight(maxYVelocity), equalTo(45))
            assertThat(calculateMaxHeight(10), equalTo(55))
        }

        @Test
        internal fun `part 1`() {
            val (xRange, yRange) = input.parse()
            val maxYVelocity = calculateMaxAndMinYVelocityPossible(xRange, yRange).first
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
            val (maxY, minY) = calculateMaxAndMinYVelocityPossible(xRange, yRange)
            val trajectories = filterVelocitiesThatEndUpInRange(minX..maxX, minY..maxY, xRange, yRange)
            assertThat(trajectories.size, equalTo(112))
        }

        @Test
        internal fun `part 2`() {
            val (xRange, yRange) = input.parse()
            val (minX, maxX) = calculateMinXVelocityAndMaxXVelocity(xRange.first, xRange.last)
            val (maxY, minY) = calculateMaxAndMinYVelocityPossible(xRange, yRange)
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
