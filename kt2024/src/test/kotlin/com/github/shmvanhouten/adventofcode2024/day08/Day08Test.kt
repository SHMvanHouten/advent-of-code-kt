package com.github.shmvanhouten.adventofcode2024.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day08Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(listAntinodes(example).count()).isEqualTo(14)
        }

        @Test
        internal fun `part 1`() {
            expectThat(listAntinodes(input).count()).isEqualTo(305)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(listAntinodesOnALine(example).count()).isEqualTo(34)
        }

        @Test
        internal fun `part 2`() {
            expectThat(listAntinodesOnALine(input).count()).isEqualTo(1150)
        }
    }

    private val input by lazy { readFile("/input-day08.txt") }
    private val example = """
    ............
    ........0...
    .....0......
    .......0....
    ....0.......
    ......A.....
    ............
    ............
    ........A...
    .........A..
    ............
    ............
""".trimIndent()
}
