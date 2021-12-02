package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.util.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day02Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `test input`() {
            val input = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
            val instructions = parseInstructions(input)
            val result = steer(instructions)
            assertThat(
                result.x * result.y,
                equalTo(150)
            )
        }

        @Test
        internal fun `part 1`() {
            val instructions = parseInstructions(input)
            val result = steer(instructions)
            assertThat(
                result.x * result.y,
                equalTo(1654760)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy {readFile("/input-day02.txt")}

}
