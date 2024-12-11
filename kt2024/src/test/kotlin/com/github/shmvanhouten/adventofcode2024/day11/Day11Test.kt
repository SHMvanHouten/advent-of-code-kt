package com.github.shmvanhouten.adventofcode2024.day11

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day11Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(blink(example, 25).count()).isEqualTo(55312)
        }

        @Test
        internal fun `part 1`() {
            expectThat(blink(input, 25).count()).isEqualTo(175006)
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

    private val input by lazy { readFile("/input-day11.txt")}
    private val example = "125 17"
}
