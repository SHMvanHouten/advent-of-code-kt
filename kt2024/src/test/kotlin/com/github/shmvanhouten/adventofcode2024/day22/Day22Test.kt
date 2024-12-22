package com.github.shmvanhouten.adventofcode2024.day22

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day22Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `for starting number 123, the next secret numbers would be`() {
            var result = calculateNextNumber(123)
            expectThat(result).isEqualTo(15887950)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(16495136)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(527345)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(704524)
            result = calculateNextNumber(result)
            expectThat(result).isEqualTo(1553684)
        }

        @Test
        fun `example 1`() {
            val example = """
                1
                10
                100
                2024
            """.trimIndent()
            val input = example.lines().map { it.toLong() }
            expectThat(input.sumOf { calculateNextNumber(it, 2000) })
                .isEqualTo(37327623)
        }

        @Test
        internal fun `part 1`() {
            val lines = input.lines().map { it.toLong() }
            expectThat(lines.sumOf { calculateNextNumber(it, 2000) })
                .isEqualTo(20401393616)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day22.txt")}

}
