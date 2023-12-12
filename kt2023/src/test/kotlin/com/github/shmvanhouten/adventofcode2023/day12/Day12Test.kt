package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLessThan

class Day12Test {

    @Nested
    inner class Part1 {

        val example = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()

        @Test
        internal fun `# 1 can be arranged 1 way`() {
            expectThat(possibleArrangements("#? 1")).isEqualTo(1)
        }

        @Test
        fun `#!! 1,1 can be arranged 2 ways`() {
            expectThat(possibleArrangements("#??? 1,1")).isEqualTo(2)
        }

        @Test
        fun `this can be arranged in 1 way`() {
            expectThat(possibleArrangements("???.### 1,1,3"))
                .isEqualTo(1)
        }

        @Test
        fun `4 ways`() {
            expectThat(possibleArrangements(".??..??...?##. 1,1,3"))
                .isEqualTo(4)
        }

        @Test
        fun `10 ways`() {
            expectThat(possibleArrangements("?###???????? 3,2,1"))
                .isEqualTo(10)
        }

        @Test
        fun `example 1`() {
            expectThat(example.lines().sumOf { possibleArrangements(it) })
                .isEqualTo(21)
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().sumOf { possibleArrangements(it) })
                .isLessThan(7960)
                .isEqualTo(7460)
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

    private val input by lazy { readFile("/input-day12.txt")}

}
