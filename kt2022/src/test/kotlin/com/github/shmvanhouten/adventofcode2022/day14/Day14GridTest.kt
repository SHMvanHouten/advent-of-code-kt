package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day14GridTest {

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent()
            val cave = parseToGrid(input)

            cave.dropSandUntilTopIsReached()

            assertThat(cave.amountOfSand())
                .isEqualTo(93)

            println(cave.print())
        }

        @Test
        internal fun `part 2`() {
            val cave = parseToGrid(input)

            cave.dropSandUntilTopIsReached()

            assertThat(cave.amountOfSand())
                .isEqualTo(25161)

            println(cave.print())
        }
    }

    private val input by lazy { readFile("/input-day14.txt")}

}
