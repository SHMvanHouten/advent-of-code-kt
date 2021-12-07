package com.github.shmvanhouten.adventofcode2021.day07

import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day07Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val positions = exampleInput.split(',').map { it.toInt() }
            val mostEfficient = findMostFuelEfficientAlignment(positions)
            assertThat(mostEfficient.first, equalTo(2))
            assertThat(mostEfficient.second, equalTo(37))
        }

        @Test
        internal fun `part 1`() {
            val positions = input.split(',').map { it.toInt() }
            val mean = ((positions[positions.size / 2] + positions[(positions.size / 2) - 1]) / 2)
            val sum = positions.sumOf { abs(mean - it) }
            println(sum)
            // 355989

            assertThat(findMostFuelEfficientAlignment(positions).second, equalTo(355989))

        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {

            val positions = exampleInput.split(',').map { it.toInt() }
            val bestAlignment = findMostFuelEfficientAlignmentAtIncrementalFuelConsumption(positions)
            assertThat(bestAlignment.first, equalTo(5) )
            assertThat(bestAlignment.second, equalTo(168) )

        }

        @Test
        internal fun `part 2`() {
            val positions = input.split(',').map { it.toInt() }
            val bestAlignment = findMostFuelEfficientAlignmentAtIncrementalFuelConsumption(positions)
            assertThat(bestAlignment.second, equalTo(102245489) )
        }
    }

    private val input by lazy { readFile("/input-day07.txt")}
    private val exampleInput = "16,1,2,0,4,2,7,1,2,14"

}
