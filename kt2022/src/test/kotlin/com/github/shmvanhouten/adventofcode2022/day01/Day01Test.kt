package com.github.shmvanhouten.adventofcode2022.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class Day01Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @ValueSource(longs = [1000, 2000])
        internal fun `given one elf that elf is carrying the most`(carriedAmount: Long) {
            val input = carriedAmount.toString()
            assertThat(carryingTheMost(input))
                .isEqualTo(carriedAmount)
        }

        @Test
        internal fun `given one elf with multiiple items that elf is carrying the most`() {
            val input = """
                |1000
                |1000
            """.trimMargin()
            assertThat(carryingTheMost(input))
                .isEqualTo(2000)
        }

        @Test
        internal fun `pick the elf with the most calories (example 1)`() {
            assertThat(carryingTheMost(exampleinput))
                .isEqualTo(24000)
        }

        @Test
        internal fun `part 1`() {
            assertThat(carryingTheMost(input))
                .isEqualTo(67622)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            assertThat(top3ElvesTotal(exampleinput))
                .isEqualTo(45000L)
        }

        @Test
        internal fun `part 2`() {
            assertThat(top3ElvesTotal(input))
                .isEqualTo(201491)
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
