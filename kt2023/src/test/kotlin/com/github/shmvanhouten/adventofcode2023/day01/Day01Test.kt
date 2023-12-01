package com.github.shmvanhouten.adventofcode2023.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import firstNumber
import lastNumber
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import sumCalibrationValues1
import sumCalibrationValues2

class Day01Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            expectThat(sumCalibrationValues1(input)).isEqualTo(54634)
        }

    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = listOf(
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen"
            )
            expectThat(
                input.sumOf { (firstNumber(it) + lastNumber(it)).toInt() }
            ).isEqualTo(281)
        }

        @Test
        fun `four7xfour is 44`() {
            expectThat(firstNumber("four7xfour")).isEqualTo("4")
            expectThat(lastNumber("four7xfour")).isEqualTo("4")
        }

        @Test
        fun print() {
            input.lines()
                .map { it to (firstNumber(it) + lastNumber(it)) }
                .forEach { (line, result) -> println("line $line: result: $result") }
        }

        @Test
        internal fun `part 2`() {
            expectThat(
                sumCalibrationValues2(input)
            ).isEqualTo(53855)
        }


    }


    private val input by lazy { readFile("/input-day01.txt")}

}
