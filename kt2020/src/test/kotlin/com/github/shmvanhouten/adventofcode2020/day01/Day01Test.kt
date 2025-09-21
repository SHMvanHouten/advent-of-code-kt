package com.github.shmvanhouten.adventofcode2020.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day01Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(findTwoNumbersThatAddUpTo2020(example))
                .isEqualTo(1721 to 299)
        }

        @Test
        internal fun `part 1`() {
            expectThat(findTwoNumbersThatAddUpTo2020(input)).isEqualTo(366 to 1654)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(findThreeNumbersThatAddUpTo2020(example))
                .isEqualTo(Triple(979,366,675))
        }

        @Test
        internal fun `part 2`() {
            expectThat(findThreeNumbersThatAddUpTo2020(input)).isEqualTo(Triple(1220, 634, 166))
        }
    }

    private val input by lazy { readFile("/input-day01.txt")}

    private val example = """
        1721
        979
        366
        299
        675
        1456
    """.trimIndent()

}
