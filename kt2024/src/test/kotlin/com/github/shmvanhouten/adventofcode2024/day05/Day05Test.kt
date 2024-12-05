package com.github.shmvanhouten.adventofcode2024.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day05Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(sumCorrectlyOrderedLists(example)).isEqualTo(143)
        }

        @Test
        internal fun `part 1`() {
            expectThat(sumCorrectlyOrderedLists(input)).isEqualTo(6260)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(reorderIncorrectlyOrderedLists(example)).isEqualTo(123)
        }

        @Test
        internal fun `part 2`() {
            expectThat(reorderIncorrectlyOrderedLists(input)).isEqualTo(5346)
        }
    }

    private val input by lazy { readFile("/input-day05.txt")}
    private val example = """
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13

    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
""".trimIndent()
}
