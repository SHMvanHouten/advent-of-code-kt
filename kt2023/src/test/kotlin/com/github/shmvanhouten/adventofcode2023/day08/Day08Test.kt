package com.github.shmvanhouten.adventofcode2023.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day08Test {

    private val exampleInput = """
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val (instructions, network) = exampleInput
                .blocks()
            val networkInstructions = toNetworkInstructions(instructions, network)
            expectThat(networkInstructions.traverseUntil("ZZZ").size)
                .isEqualTo(2)
        }

        @Test
        internal fun `part 1`() {
            val (instructions, network) = input
                .blocks()
            val networkInstructions = toNetworkInstructions(instructions, network)
            expectThat(networkInstructions.traverseUntil("ZZZ").size)
                .isEqualTo(2)
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

    private val input by lazy { readFile("/input-day08.txt")}

}
