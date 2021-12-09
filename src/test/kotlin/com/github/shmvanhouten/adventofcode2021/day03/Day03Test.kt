package com.github.shmvanhouten.adventofcode2021.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day03Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `gamma rate for 0 is 0`() {
            val input = """
                0
            """.trimIndent()
            assertThat(
                findGammaAndEpsilonRate(input.lines()).first,
                equalTo(0)
            )
        }

        @Test
        internal fun `gamma rate for 1 is 1`() {
            val input = """
                1
            """.trimIndent()
            assertThat(
                findGammaAndEpsilonRate(input.lines()).first,
                equalTo(1)
            )
        }

        @Test
        internal fun `gamma rate for 01 00 00 is 0`() {
            val input = """
                01
                00
                00
            """.trimIndent()
            assertThat(
                findGammaAndEpsilonRate(input.lines()).first,
                equalTo(0)
            )
        }

        @Test
        internal fun `example 1`() {
            val input = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010"""
            val (gamma, epsilon) = findGammaAndEpsilonRate(input.lines())
            assertThat(gamma, equalTo(22))
            assertThat(epsilon, equalTo(9) )
        }

        @Test
        internal fun `part 1`() {
            val (gamma, epsilon) = findGammaAndEpsilonRate(input.lines())
            assertThat(gamma * epsilon, equalTo(1025636) )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val input = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010"""
            val (oxygen, co2) = findOxygenGeneratorAndCO2Scrubber(input.lines())
            assertThat(oxygen, equalTo(23))
            assertThat(co2, equalTo(10) )
        }

        @Test
        internal fun `part 2`() {
            val (oxygen, co2) = findOxygenGeneratorAndCO2Scrubber(input.lines())
            assertThat(oxygen * co2, equalTo(793873) )
        }
    }

    private val input by lazy {readFile("/input-day03.txt")}

}
