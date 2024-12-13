package com.github.shmvanhouten.adventofcode2024.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day13Test {

    /**
     *  butA    butB  prize     butA    butB  prize
     *     x       x      x        y       y      y
     * n *94 + m *22 = 8400 && n *34 + m *67 = 5400
     * 80      40
     * 8400 - m * 22
     */

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {

        }

        @Test
        internal fun `part 1`() {
            expectThat(1).isEqualTo(1)
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

    private val input by lazy { readFile("/input-day13.txt")}

}
