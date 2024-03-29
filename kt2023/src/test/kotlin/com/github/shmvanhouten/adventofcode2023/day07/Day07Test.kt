package com.github.shmvanhouten.adventofcode2023.day07

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day07Test {

    private val exampleInput = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent().lines()

    @Nested
    inner class Part1 {

        @Test
        fun `Five of a kind is stronger than four of a kind`() {
            val input = """
                44442 1
                22222 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(listOf(JackHand("22222 1"), JackHand("44442 1")))
        }

        @Test
        fun `full house is bigger than 3 of a kind`() {
            val input = """
                33314 1
                22233 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(listOf(JackHand("22233 1"), JackHand("33314 1")))
        }

        @Test
        fun `High card loses to any other type`() {
            val input = """
                AKJQT 1
                22345 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(listOf(JackHand("22345 1"), JackHand("AKJQT 1")))
        }

        @Test
        fun `two pair is stronger than pair`() {
            val input = """
                KK123 1
                22133 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(listOf(JackHand("22133 1"), JackHand("KK123 1")))
        }

        @Test
        fun `two matching type hands are sorted by comparing each card`() {
            val input = """
                A1234 1
                A1342 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(listOf(JackHand("A1342 1"), JackHand("A1234 1")))
        }

        @Test
        fun `AKQJT then numbers`() {
            val input = """
                22225 1
                22229 1
                2222T 1
                2222J 1
                2222K 1
                2222Q 1
                2222A 1
            """.trimIndent().lines().map { JackHand(it) }
            expectThat(input.sorted())
                .isEqualTo(
                    listOf(
                        JackHand("2222A 1"),
                        JackHand("2222K 1"),
                        JackHand("2222Q 1"),
                        JackHand("2222J 1"),
                        JackHand("2222T 1"),
                        JackHand("22229 1"),
                        JackHand("22225 1")
                    ))
        }

        @Test
        internal fun `example 1`() {
            expectThat(totalWinningsWithJacks(exampleInput)).isEqualTo(6440)
        }

        @Test
        internal fun `part 1`() {
            expectThat(totalWinningsWithJacks(input)).isEqualTo(252656917)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(totalWinningsWithJokers(exampleInput)).isEqualTo(5905)
        }

        @Test
        internal fun `part 2`() {
            expect{
                that(totalWinningsWithJokers(input)).isEqualTo(253499763)
            }
        }
    }

    private val input by lazy { readFile("/input-day07.txt").lines()}

}
