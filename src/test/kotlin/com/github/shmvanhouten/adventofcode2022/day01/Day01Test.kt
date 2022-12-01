package com.github.shmvanhouten.adventofcode2022.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class Day01Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @ValueSource(longs = [1000, 2000])
        internal fun `given one elf that elf is carrying the most`(carriedAmount: Long) {
            val input = carriedAmount.toString()
            assertThat(carryingTheMost(input), equalTo(carriedAmount) )
        }

        @ParameterizedTest
        @CsvSource(
            delimiter = ',',
            value = [
                "'1000\n1000',2000"
            ]
        )
        internal fun `given one elf with multiiple items that elf is carrying the most`(input: String, carriedAmount: Long) {
            assertThat(carryingTheMost(input), equalTo(carriedAmount) )
        }

        @Test
        internal fun `pick the elf with the most calories (example 1)`() {


            assertThat(carryingTheMost(exampleinput), equalTo(24000))
        }

        @Test
        internal fun `part 1`() {
            assertThat(carryingTheMost(input), equalTo(67622) )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            assertThat(top3ElvesTotal(exampleinput), equalTo(45000L))
        }

        @Test
        internal fun `part 2`() {
            assertThat(top3ElvesTotal(input), equalTo(201491) )
        }
    }

    private val input by lazy { readFile("/input-day01.txt")}
    private val exampleinput = """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000
            """.trimIndent()
}
