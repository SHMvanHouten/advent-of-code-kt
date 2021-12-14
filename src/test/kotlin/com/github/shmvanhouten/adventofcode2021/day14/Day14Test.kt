package com.github.shmvanhouten.adventofcode2021.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day14Test {

    @Nested
    inner class Part1 {

        //            Template:     NNCB
//            After step 1: NCNBCHB
//            After step 2: NBCCNBBBCBHCB
//            After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
//            After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
        @Test
        internal fun `after one step NNCB becomes NCNBCHB`() {
            val (template, rules) = parse(exampleInput)

            assertThat(applyRules(template, rules), equalTo("NCNBCHB"))
        }

        @Test
        internal fun `after two step NNCB becomes NBCCNBBBCBHCB`() {
            val (template, rules) = parse(exampleInput)

            assertThat(applyRules(template, rules, 2), equalTo("NBCCNBBBCBHCB"))
        }

        @Test
        internal fun `after four steps NNCB becomes NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB`() {
            val (template, rules) = parse(exampleInput)

            assertThat(applyRules(template, rules, 4), equalTo("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"))
        }

        @Test
        internal fun `example 1`() {
            val (template, rules) = parse(exampleInput)
            val polymer = applyRules(template, rules, 10)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(
                (mostCommon - leastCommon),
                equalTo(1749 - 161)
            )
        }

        @Test
        internal fun `part 1`() {
            val (template, rules) = parse(input)
            val polymer = applyRules(template, rules, 10)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(
                (mostCommon - leastCommon),
                equalTo(2874)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1))
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1))
        }
    }

    private val input by lazy { readFile("/input-day14.txt") }
    private val exampleInput = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C"""

}
