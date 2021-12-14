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
            val (template, rules) = parseSimple(exampleInput)

            val polymer = applyRules(template, rules)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(mostCommon, equalTo(2))
            assertThat(leastCommon, equalTo(1))
        }

        @Test
        internal fun `after two step NNCB becomes NBCCNBBBCBHCB`() {
            val (template, rules) = parseSimple(exampleInput)

            val polymer = applyRules(template, rules, 2)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(mostCommon, equalTo(6))
            assertThat(leastCommon, equalTo(1))
        }

        @Test
        internal fun `after four steps NNCB becomes NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB`() {
            val (template, rules) = parseSimple(exampleInput)

            val polymer = applyRules(template, rules, 4)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(mostCommon, equalTo(23))
            assertThat(leastCommon, equalTo(5))
        }

        @Test
        internal fun `example 1`() {
            val (template, rules) = parseSimple(exampleInput)
            val polymer = applyRules(template, rules, 10)
            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElement(polymer)
            assertThat(
                (mostCommon - leastCommon),
                equalTo(1749 - 161)
            )
        }

        @Test
        internal fun `part 1`() {
            val (template, rules) = parseSimple(input)
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
        internal fun `after one step NNCB becomes NCNBCHB`() {
            val (template, rules) = parse(exampleInput)

            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElementAfterSynthesisRuns(template, rules, 1)
            assertThat(mostCommon, equalTo(2))
            assertThat(leastCommon, equalTo(1))
        }

        @Test
        internal fun `after two steps NNCB becomes NBCCNBBBCBHCB`() {
            val (template, rules) = parse(exampleInput)

            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElementAfterSynthesisRuns(template, rules, 2)
            assertThat(mostCommon, equalTo(6))
            assertThat(leastCommon, equalTo(1))
        }

        @Test
        internal fun `after four steps NNCB becomes NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB`() {
            val (template, rules) = parse(exampleInput)

            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElementAfterSynthesisRuns(template, rules, 4)
            assertThat(mostCommon, equalTo(23))
            assertThat(leastCommon, equalTo(5))
        }

        @Test
        internal fun example() {
            val (template, rules) = parse(exampleInput)

            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElementAfterSynthesisRuns(template, rules, 40)
            assertThat(mostCommon, equalTo(2192039569602))
            assertThat(leastCommon, equalTo(3849876073))
            assertThat(mostCommon - leastCommon, equalTo(2188189693529))
        }

        @Test
        internal fun `part 2`() {
            val (template, rules) = parse(input)

            val (leastCommon, mostCommon) = countMostCommonAndLeastCommonElementAfterSynthesisRuns(template, rules, 40)
            assertThat(mostCommon - leastCommon, equalTo(5208377027195))
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
