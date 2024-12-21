package com.github.shmvanhouten.adventofcode2024.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isLessThan

class Day21Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `bot 1 pushes numeric buttons`() {
            val numericBot = NumericBot()
            expectThat(numericBot.buttonsToPress("029A").joinToString("") { it.va }).isEqualTo("<A^A>^^AvvvA")
        }

        @Test
        fun `bot 2 pushes directional keypad`() {
            val directionalBot = DirectionalBot()
            val bot2Result = directionalBot.buttonsToPress(NumericBot().buttonsToPress("029A"))
            expectThat(bot2Result.size)
                .isEqualTo(28)
            val bot3Result = directionalBot.buttonsToPress(bot2Result)
            expectThat(bot3Result.size)
                .isEqualTo(68)
        }

        @ParameterizedTest
        @CsvSource(value = [
            "029A, 68",
            "980A, 60",
            "179A, 68",
            "456A, 64",
            "379A, 64"
        ])
        fun `bot 2 pushes directional keypad`(sequence: String, expected: Int) {
            val directionalBot = DirectionalBot()
            val bot1Result = NumericBot().buttonsToPress(sequence)
            val bot2Result = directionalBot.buttonsToPress(bot1Result)
            val bot3Result = directionalBot.buttonsToPress(bot2Result)
            println(printSequence(sequence, bot1Result, bot2Result, bot3Result))
            expectThat(bot3Result.size)
                .isEqualTo(expected)
        }

        @Test
        fun reverse() {
            val input = "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A".map { fromVal(it.toString()) }
            val bot3Result = DirectionalBot().pressDirectional(input)
            val bot2Result = DirectionalBot().pressDirectional(bot3Result)
            val bot1Result = DirectionalBot().pressNumeric(bot2Result)
            println(printSequence(bot1Result, bot2Result, bot3Result, input))
            expectThat(bot1Result).isEqualTo("379A")
        }

        @Test
        fun `example 1`() {
            expectThat(example.lines().sumOf { calculateComplexity(it) }).isEqualTo(126384)
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().sumOf { calculateComplexity(it) }).isLessThan(188078)
            expectThat(input.lines().sumOf { calculateComplexity(it) }).isEqualTo(1)
        }
    }

    private fun printSequence(
        sequence: String,
        bot1Presses: List<Button>,
        bot2Presses: List<Button>,
        bot3Presses: List<Button>
    ): String {
        return buildString {
            append(bot3Presses.joinToString(""){ it.va })
            append("\n")
            var index = 0
            val bot2String = bot3Presses.joinToString("") {
                if(it == Button.A) bot2Presses[index++].va else " "
            }
            append(bot2String)
            append("\n")
            index = 0
            val bot1String = bot2String.map {
                if(it == 'A') bot1Presses[index++].va else " "
            }.joinToString("")
            append(bot1String)
            append("\n")
            index = 0
            bot1String.map {
                if(it == 'A') sequence[index++] else " "
            }.forEach { append(it) }
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

    private val input by lazy { readFile("/input-day21.txt")}
    private val example = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent()

}
