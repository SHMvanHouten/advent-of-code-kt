package com.github.shmvanhouten.adventofcode2023.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day03Test {

    val exampleInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `example pt 1`() {
            expectThat(
                findPartsNumbers(exampleInput)
                    .sum()
            ).isEqualTo(4361)

        }

        @Test
        internal fun `part 1`() {
            expectThat(
                findPartsNumbers(input)
                    .sum()
            ).isEqualTo(514969)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example pt2`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day03.txt")}

}
