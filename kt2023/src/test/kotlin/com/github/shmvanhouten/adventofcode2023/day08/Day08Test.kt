package com.github.shmvanhouten.adventofcode2023.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan
import java.math.BigInteger

class Day08Test {

    @Nested
    inner class Part1 {
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

        @Test
        internal fun `example 1`() {
            val (instructions, network) = exampleInput
                .blocks()
            val networkInstructions =
                toNetworkInstructions(instructions, network)
            expectThat(networkInstructions.traverseUntil("ZZZ").size)
                .isEqualTo(2)
        }

        @Test
        internal fun `part 1`() {
            val (instructions, network) = input
                .blocks()
            val networkInstructions =
                toNetworkInstructions(instructions, network)
            expectThat(networkInstructions.traverseUntil("ZZZ").size)
                .isEqualTo(13939)
        }
    }

    @Nested
    inner class Part2 {

        private val exampleInput = """
            LR

            11A = (11B, xxx)
            11B = (xxx, 11Z)
            11Z = (11B, xxx)
            22A = (22B, xxx)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            xxx = (xxx, xxx)
        """.trimIndent()

        // how long does it take to travel from 11A to any target ending in Z
        // how long does it take to travel from 22A to any target ending in Z
        // for any target ending in z: how long does it take to get the next z (map it)

        @Test
        internal fun `example 2`() {
            val (instructions, network) = exampleInput.blocks()
            val networkInstructions =
                com.github.shmvanhouten.adventofcode2023.day08.part2.toNetworkInstructions(instructions, network)

            expectThat(networkInstructions.findFirstPointWhereAllPathsHitTarget())
                .isEqualTo(BigInteger("6"))
        }

        @Test
        fun otherTest() {
            val (instructions, network) = """
                LR
                
                33A = (33B, xxx)
                33B = (xxx, 33Z)
                33Z = (33B, xxx)
                11A = (11B, XXX)
                11B = (XXX, 11C)
                11C = (11D, XXX)
                11D = (XXX, 11Z)
                11Z = (11B, XXX)
                22A = (22B, XXX)
                22B = (22C, 22C)
                22C = (22Z, 22Z)
                22Z = (22B, 22B)
                XXX = (XXX, XXX)
            """.trimIndent().blocks()

            val networkInstructions =
                com.github.shmvanhouten.adventofcode2023.day08.part2.toNetworkInstructions(instructions, network)

            expectThat(networkInstructions.findFirstPointWhereAllPathsHitTarget())
                .isEqualTo(BigInteger("12"))
        }

        @Test
        internal fun `part 2`() {
            val (instructions, network) = input.blocks()
            val networkInstructions =
                com.github.shmvanhouten.adventofcode2023.day08.part2.toNetworkInstructions(instructions, network)

            println(BigInteger("11206957317755125787748971").divide(BigInteger("263")))
            expectThat(networkInstructions.findFirstPointWhereAllPathsHitTarget())
                .isGreaterThan(BigInteger("33865167419"))
                .isLessThan(BigInteger("42612005010475763451517"))
                .isEqualTo(BigInteger.ZERO)
        }
    }

    private val input by lazy { readFile("/input-day08.txt")}

}
