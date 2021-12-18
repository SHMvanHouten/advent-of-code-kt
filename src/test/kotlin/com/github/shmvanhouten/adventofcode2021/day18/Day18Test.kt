package com.github.shmvanhouten.adventofcode2021.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class Day18Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @ValueSource(
            strings = [
                "[[[[[9,8],1],2],3],4]",
                "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
            ]
        )
        internal fun parse(snailFishNumber: String) {
            val unexploded = parseSnailFishNumber(snailFishNumber)
            assertThat(unexploded.first().toString(), equalTo(snailFishNumber))
        }

        @Test
        internal fun `parse input`() {
            assertThat(parseSnailFishNumber(input).joinToString("\n"), equalTo(input))
        }

        @Test
        internal fun `(1,1) + (2,2) = ((1,1),(2,2))`() {
            val input = """[1,1]
                |[2,2]
            """.trimMargin()
            val unsolvedSum = parseSnailFishNumber(input)
            val result = sum(unsolvedSum)
            assertThat(result.toString(), equalTo("[[1,1],[2,2]]"))
        }

        @Test
        internal fun `sums a longer list of snailfishnumbers`() {
            val input = """[1,1]
[2,2]
[3,3]
[4,4]"""
            val unsolvedSum = parseSnailFishNumber(input)
            val result = sum(unsolvedSum)
            assertThat(result.toString(), equalTo("[[[[1,1],[2,2]],[3,3]],[4,4]]"))
        }

        @ParameterizedTest(name = "{0} should explode to {1}")
        @CsvSource(
            value = [
                "[[[[[9,8],1],2],3],4]; [[[[0,9],2],3],4]",
                "[7,[6,[5,[4,[3,2]]]]]; [7,[6,[5,[7,0]]]]",
                "[[6,[5,[4,[3,2]]]],1]; [[6,[5,[7,0]]],3]",
                "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]; [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
                "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]; [[3,[2,[8,0]]],[9,[5,[7,0]]]]"
            ], delimiter = ';'
        )
        internal fun `pairs nested inside four pairs need to be exploded`(
            input: String,
            expected: String
        ) {
            val unexploded = parseSnailFishNumber(input).first()
            assertThat(unexploded.resolve().toString(), equalTo(expected))
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "[[[[0,7],4],[15,[0,13]]],[1,1]]; [[[[0,7],4],[[7,8],[0,13]]],[1,1]]",
                "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]; [[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"
            ], delimiter = ';'
        )
        internal fun `regular numbers above 10 need to be split`(
            input: String,
            expected: String
        ) {
            val unexploded = parseSnailFishNumber(input).first()
            assertThat(unexploded.resolve().toString(), equalTo(expected))
        }

        @Test
        internal fun `example 1`() {
            val example = """[[[[4,3],4],4],[7,[[8,4],9]]]
                |[1,1]""".trimMargin()
            val unsolvedSum = parseSnailFishNumber(example)
            val result = sum(unsolvedSum)
            assertThat(result.toString(), equalTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"))
        }

        @Test
        internal fun `example 1,5`() {
            val example = """[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"""
            val unsolvedSum = parseSnailFishNumber(example)
            val result = sum(unsolvedSum)
            assertThat(result.toString(), equalTo("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"))
        }

        @Test
        internal fun `example 2`() {
            val example = """[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
[7,[5,[[3,8],[1,4]]]]
[[2,[2,2]],[8,[8,1]]]
[2,9]
[1,[[[9,3],9],[[9,0],[0,7]]]]
[[[5,[7,4]],7],1]
[[[[4,2],2],6],[8,7]]"""
            val unsolvedSum = parseSnailFishNumber(example)
            val result = sum(unsolvedSum)
            assertThat(result.toString(), equalTo("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"))
        }

        @Test
        internal fun `part 1`() {
            assertThat(1, equalTo(1))
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

    private val input by lazy { readFile("/input-day18.txt") }

}
