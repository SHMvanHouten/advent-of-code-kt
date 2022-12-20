package com.github.shmvanhouten.adventofcode2022.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day20Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val input = """
                1
                2
                -3
                3
                -2
                0
                4
            """.trimIndent()
            assertThat(mix(input).groveCoordinates).isEqualTo(3)
        }

        @Test
        internal fun `move around multiple times`() {
            val input = """
                10
                1
                0
                1
            """.trimIndent()
            assertThat(mix(input)).isEqualTo(listOf(10L, 1, 1, 0))
        }

        @Test
        internal fun `move negative nr around multiple times`() {
            val input = """
                -10
                1
                0
                1
            """.trimIndent()
            assertThat(mix(input)).isEqualTo(listOf(0L, 1, 1, -10))
        }

        @Test
        internal fun `part 1`() {
            assertThat(mix(input).groveCoordinates).isEqualTo(6387L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                1
                2
                -3
                3
                -2
                0
                4
            """.trimIndent()
            assertThat(megaMix(input).groveCoordinates).isEqualTo(1623178306)
        }

        @Test
        internal fun `part 2`() {
            assertThat(megaMix(input).groveCoordinates).isEqualTo(2455057187825L)
        }
    }

    private val input by lazy { readFile("/input-day20.txt")}

}
