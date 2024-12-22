package com.github.shmvanhouten.adventofcode2024.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan

class Day21Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `bot 1 pushes numeric buttons`() {
            val numericBot = NumericBot()
            expectThat(numericBot.buttonsToPress("029A").joinToString("") { it }).isEqualTo("<A^A^^>AvvvA")
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
            println(printSequence(sequence, listOf(bot1Result, bot2Result, bot3Result)))
            expectThat(bot3Result.size)
                .isEqualTo(expected)
        }

        @Test
        fun reverse() {
            val input = "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A".map { fromVal(it.toString()) }
            val bot3Result = DirectionalBot().pressDirectional(input)
            val bot2Result = DirectionalBot().pressDirectional(bot3Result)
            val bot1Result = DirectionalBot().pressNumeric(bot2Result)
            println(printSequence(bot1Result, listOf(bot2Result, bot3Result, input)))
            expectThat(bot1Result).isEqualTo("379A")
        }

        @Test
        fun `example 1`() {
            expectThat(example.lines().sumOf { calculateComplexity(it) }).isEqualTo(126384)
        }

        @ParameterizedTest
        @CsvSource(value = [
            "789A, 66",
            "968A, 70",
            "286A, 68",
            "349A, 72",
            "170A, 72",
        ])
        fun `part 1 input`(sequence: String, expected: Int) {
            val directionalBot = DirectionalBot()
            val bot1Result = NumericBot().buttonsToPress(sequence)
            val bot2Result = directionalBot.buttonsToPress(bot1Result)
            val bot3Result = directionalBot.buttonsToPress(bot2Result)
            println(printSequence(sequence, listOf(bot1Result, bot2Result, bot3Result)))
            expectThat(bot3Result.size)
                .isEqualTo(expected)
        }

        @Test
        internal fun `part 1`() {
            expectThat(input.lines().sumOf { calculateComplexityNBots(it, 2) }).isEqualTo(176650)
        }
    }

    @Nested
    inner class Part2 {
        @ParameterizedTest
        @CsvSource(value = [
            "789A, 2, 52074",
            "968A, 2, 67760",
            "286A, 2, 19448",
            "349A, 2, 25128",
            "170A, 2, 12240",
            "029A, 2, 1972",
            "980A, 2, 58800",
            "179A, 2, 12172",
            "456A, 2, 29184",
            "379A, 2, 24256",
            "789A, 3, 132552",
            "968A, 3, 172304",
            "286A, 3, 50336",
            "349A, 3, 62122",
            "170A, 3, 29920",
            "029A, 3, 4872",
            "980A, 3, 147000",
            "179A, 3, 30072",
            "456A, 3, 75696",
            "379A, 3, 60640",
            "789A, 4, 328224",
            "968A, 4, 431728",
            "286A, 4, 126984",
            "349A, 4, 156352",
            "170A, 4, 75480",
            "029A, 4, 12180",
            "980A, 4, 364560",
            "179A, 4, 74822",
            "456A, 4, 188784",
            "379A, 4, 151600",
            "789A, 5, 831606",
            "968A, 5, 1095776",
            "286A, 5, 323180",
            "349A, 5, 395766",
            "170A, 5, 190060",
            "029A, 5, 30798",
            "980A, 5, 923160",
            "179A, 5, 188666",
            "456A, 5, 479712",
            "379A, 5, 384306",
            "789A, 6, 2101896",
            "968A, 6, 2768480",
            "286A, 6, 817388",
            "349A, 6, 1000932",
            "170A, 6, 482460",
            "029A, 6, 77836",
            "980A, 6, 2332400",
            "179A, 6, 477214",
            "456A, 6, 1213872",
            "379A, 6, 971756",
            "789A, 7, 5335218",
            "968A, 7, 7031552",
            "286A, 7, 2076932",
            "349A, 7, 2539324",
            "170A, 7, 1222980",
            "029A, 7, 197432",
            "980A, 7, 5921160",
            "179A, 7, 1210398",
            "456A, 7, 3082560",
            "379A, 7, 2467290",
            "789A, 8, 13534506",
            "968A, 8, 17830560",
            "286A, 8, 5267548",
            "349A, 8, 6441144",
            "170A, 8, 3105220",
            "029A, 8, 500772",
            "980A, 8, 15017520",
            "179A, 8, 3070924",
            "456A, 8, 7821312",
            "379A, 8, 6258048"
        ])
        internal fun `for n amount of bots, the complexity is`(sequence: String, n: Int, expected: Long) {
            expectThat(calculateComplexityNBots(sequence, n)).isEqualTo(expected)
        }

        @Test
        fun `for n amount of bots, complexity is`() {
            expectThat(calculateComplexityNBots("789A", 2)).isEqualTo(52074L)
        }

        @Test
        internal fun `part 2`() {
            expectThat(input.lines().sumOf { calculateComplexityNBots(it, 25) }).isLessThan(348796545149904)
            expectThat(input.lines().sumOf { calculateComplexityNBots(it, 25) }).isGreaterThan(137396351418306)
            expectThat(input.lines().sumOf { calculateComplexityNBots(it, 25) }).isEqualTo(1)
        }

        @Test
        fun `for printing testcases`() {
            (2..8).joinToString("\n") {n ->
                listOf("789A",
                        "968A",
                        "286A",
                        "349A",
                        "170A",
                        "029A",
                        "980A",
                        "179A",
                        "456A",
                        "379A"
                ).joinToString(separator = "\",\n\"", "\"", "\",") {
                            "$it, $n, ${calculateComplexityNBots(it, n)}"
                }
            }.also { println(it) }
        }

        @Test
        fun `for printing sequence sequences`() {
            val sequence = "286A"
            val directionalBot = DirectionalBot()
            val originalSequence = NumericBot().buttonsToPress(sequence)
            val sequences = generateSequence(originalSequence) {
                directionalBot.buttonsToPress(it)
            }.take(7).toList()
            println(printSequence(sequence, sequences))
        }
    }

    private fun printSequence(
        sequence: String,
        botPresses: List<List<Button>>
    ): String {
        val reversed = botPresses.reversed()
        return buildString {

            var index = 0
            var prevString = reversed.first().joinToString(""){it}
            append(prevString)
            append("\n")
            reversed.subList(1, reversed.size).forEach {botPresses ->
                prevString = prevString.map {
                    if(it == 'A') botPresses[index++] else " "
                }.joinToString("")
                index = 0
                append(prevString)
                append("\n")
            }
            index = 0
            prevString.map {
                if(it == 'A') sequence[index++] else " "
            }.forEach { append(it) }
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
