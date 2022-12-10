package com.github.shmvanhouten.adventofcode2022.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `regardless of input, during the first cycle, the value of register X is 1`() {
            val input = "noop"
            assertThat(runInstructions(input).first()).isEqualTo(1)
        }

        @Test
        internal fun `regardless of first input, during the second cycle, the value of register X is 1`() {
            val input = """
                noop
                noop
            """.trimIndent()

            assertThat(runInstructions(input)[1]).isEqualTo(1)
        }

        @Test
        internal fun `if the first cycle was noop, the value of register X is 1 during the 3rd cycle`() {
            val input = """
                noop
                noop
                noop
            """.trimIndent()

            assertThat(runInstructions(input)[2]).isEqualTo(1)
        }

        @Test
        internal fun `if the first cycle was addx 3, the value of register X is 4 during the 3rd cycle`() {
            val input = """
                addx 3
                noop
            """.trimIndent()

            val result = runInstructions(input)
            assertThat(result[0]).isEqualTo(1)
            assertThat(result[1]).isEqualTo(1)
            assertThat(result[2]).isEqualTo(4)
        }

        @Test
        internal fun `example, reg X at 20th cycle is 20 x 21 = 420`() {
            val result = runInstructions(example)

            assertThat(result.registerXAtCycle(20)).isEqualTo(420)
        }

        @Test
        internal fun example() {
            val result = runInstructions(example)

            assertThat(sumSignalStrengths(result)).isEqualTo(13140)
        }

        @Test
        internal fun `part 1`() {
            val result = runInstructions(input)

            assertThat(sumSignalStrengths(result)).isEqualTo(14220)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            assertThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day10.txt")}

    private val example = """
        addx 15
        addx -11
        addx 6
        addx -3
        addx 5
        addx -1
        addx -8
        addx 13
        addx 4
        noop
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx -35
        addx 1
        addx 24
        addx -19
        addx 1
        addx 16
        addx -11
        noop
        noop
        addx 21
        addx -15
        noop
        noop
        addx -3
        addx 9
        addx 1
        addx -3
        addx 8
        addx 1
        addx 5
        noop
        noop
        noop
        noop
        noop
        addx -36
        noop
        addx 1
        addx 7
        noop
        noop
        noop
        addx 2
        addx 6
        noop
        noop
        noop
        noop
        noop
        addx 1
        noop
        noop
        addx 7
        addx 1
        noop
        addx -13
        addx 13
        addx 7
        noop
        addx 1
        addx -33
        noop
        noop
        noop
        addx 2
        noop
        noop
        noop
        addx 8
        noop
        addx -1
        addx 2
        addx 1
        noop
        addx 17
        addx -9
        addx 1
        addx 1
        addx -3
        addx 11
        noop
        noop
        addx 1
        noop
        addx 1
        noop
        noop
        addx -13
        addx -19
        addx 1
        addx 3
        addx 26
        addx -30
        addx 12
        addx -1
        addx 3
        addx 1
        noop
        noop
        noop
        addx -9
        addx 18
        addx 1
        addx 2
        noop
        noop
        addx 9
        noop
        noop
        noop
        addx -1
        addx 2
        addx -37
        addx 1
        addx 3
        noop
        addx 15
        addx -21
        addx 22
        addx -6
        addx 1
        noop
        addx 2
        addx 1
        noop
        addx -10
        noop
        noop
        addx 20
        addx 1
        addx 2
        addx 2
        addx -6
        addx -11
        noop
        noop
        noop
    """.trimIndent()

}
