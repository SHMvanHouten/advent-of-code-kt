package com.github.shmvanhouten.adventofcode2022.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day02Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `given Opponent chooses ROCK and I choose SCISSORS I get 3 + 0 = 3 points`() {
            val input = "A Z"
            assertThat(
                RockPaperScissors(input).scoreWhenBothEntriesAreShapes(),
                equalTo(3)
            )
        }

        @Test
        internal fun `given Opponent chooses ROCK and I choose ROCK I get 1 + 3 = 4 points`() {
            val input = "A X"
            assertThat(
                RockPaperScissors(input).scoreWhenBothEntriesAreShapes(),
                equalTo(4)
            )
        }

        @Test
        internal fun `given Opponent chooses ROCK and I choose PAPER I get 2 + 6 = 8 points`() {
            val input = "A Y"
            assertThat(
                RockPaperScissors(input).scoreWhenBothEntriesAreShapes(),
                equalTo(8)
            )
        }

        @ParameterizedTest
        @CsvSource(delimiter = ',', quoteCharacter = '\'',
        value = [
            "'A X', 4",
            "'A Y', 8",
            "'A Z', 3",
            "'B X', 1",
            "'B Y', 5",
            "'B Z', 9",
            "'C X', 7",
            "'C Y', 2",
            "'C Z', 6",
        ])
        internal fun all_the_scores(input: String, expectedResult: Long) {
            assertThat(
                RockPaperScissors(input).scoreWhenBothEntriesAreShapes(),
                equalTo(expectedResult)
            )
        }

        @Test
        internal fun `example 1`() {
            val example1 = """
                A Y
                B X
                C Z
            """.trimIndent()
            assertThat(
                RockPaperScissors(example1).scoreWhenBothEntriesAreShapes(),
                equalTo(15)
            )
        }

        @Test
        internal fun `part 1`() {
            assertThat(
                RockPaperScissors(input).scoreWhenBothEntriesAreShapes(),
                equalTo(11767)
            )
        }


    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val example1 = """
                A Y
                B X
                C Z
            """.trimIndent()
            assertThat(
                RockPaperScissors(example1).scoreWhenSecondEntryIsTheRequiredResult(),
                equalTo(12)
            )
        }

        @Test
        internal fun `part 2`() {
            assertThat(
                RockPaperScissors(input).scoreWhenSecondEntryIsTheRequiredResult(),
                equalTo(13886)
            )
        }
    }

    private val input by lazy { readFile("/2022/input-day02.txt")}

}
