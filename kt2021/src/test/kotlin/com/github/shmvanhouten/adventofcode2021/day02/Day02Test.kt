package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
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
            val destination = SimpleSubmarine().navigate(instructions).location
            assertThat(
                destination.x * destination.y,
                equalTo(150)
            )
        }

        @Test
        internal fun `part 1`() {
            val instructions = parseInstructions(input)
            val destination = SimpleSubmarine().navigate(instructions).location
            assertThat(
                destination.x * destination.y,
                equalTo(1654760)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example part 2`() {
            val input = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
            val instructions = parseInstructions(input)
            val destination = AimingSubmarine().navigate(instructions).location
            assertThat(
                destination.x * destination.y,
                equalTo(900)
            )
        }

        @Test
        internal fun `part 2`() {
            val instructions = parseInstructions(input)
            val destination = AimingSubmarine().navigate(instructions).location
            assertThat(
                destination.x * destination.y,
                equalTo(1956047400)
            )
        }
    }

    private val input by lazy {readFile("/input-day02.txt")}

}
