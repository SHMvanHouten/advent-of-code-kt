package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLessThan

class Day12Test {

    val example = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()


    @Nested
    inner class Part1 {

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
                .isEqualTo(7460)
        }

        @Test
        fun `count 22`() {
            expectThat(possibleArrangements("????.????. 1,1"))
                .isEqualTo(22)
        }

        @Test
        fun `count 29`() {
            expectThat(possibleArrangements("?????.????. 1,1"))
                .isEqualTo(29)
        }

        @Test
        fun `count 2212`() {
            expectThat(possibleArrangements("????.????.?????.????. 1,1,1,1"))
                .isEqualTo(1205)
        }

    }

    @Nested
    inner class Part2 {

        @Test
        fun `1 arrangement`() {
            expectThat(possibleArrangements("???.### 1,1,3", 5))
                .isEqualTo(1)
        }

        @Test
        fun `16384 arrangements`() {
            expectThat(possibleArrangements(".??..??...?##. 1,1,3", 5))
                .isEqualTo(16384)
        }

        @Test
        fun `2500 arrangements`() {
            expectThat(possibleArrangements("????.######..#####. 1,6,5", 5))
                .isEqualTo(2500)
        }

        @Test
        fun `also 1 arrangement`() {
            expectThat(possibleArrangements("?#?#?#?#?#?#?#? 1,3,1,6", 5))
                .isEqualTo(1)
        }

        @Test
        fun `16 arrangements`() {
            expectThat(possibleArrangements("????.#...#... 4,1,1", 5))
                .isEqualTo(16)
        }

        @Test
        fun `506450 arrangements`() {
            expectThat(possibleArrangements("?###???????? 3,2,1", 5))
                .isEqualTo(506250)
        }

        @Test
        internal fun `example 1`() {
            expectThat(example.lines()
                .sumOf { possibleArrangements(it, 5) })
                .isEqualTo(525152)
        }

        @Test
        internal fun `part 2`() {
            expectThat(input.lines().sumOf { possibleArrangements(it, 5) })
                .isEqualTo(6720660274964)
        }
    }

    private val input by lazy { readFile("/input-day12.txt")}

}
