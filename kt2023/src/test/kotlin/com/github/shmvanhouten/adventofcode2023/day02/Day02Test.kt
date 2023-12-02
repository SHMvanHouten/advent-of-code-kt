package com.github.shmvanhouten.adventofcode2023.day02

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class Day02Test {

    val exampleInput = """
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """.trimIndent()

    @Nested
    inner class Part1 {

        private val requirements = listOf(Cube(RED, 12), Cube(GREEN, 13), Cube(BLUE, 14))

        @Test
        internal fun example() {

            val games = exampleInput.lines().map { parse(it) }

            expect {
                that(games[0].isPossible(requirements))
                    .isTrue()
                that(games[1].isPossible(requirements))
                    .isTrue()
                that(games[4].isPossible(requirements))
                    .isTrue()

                that(games[2].isPossible(requirements))
                    .isFalse()
                that(games[3].isPossible(requirements))
                    .isFalse()

                that(games.sumIdsOfPossibleGames(requirements))
                    .isEqualTo(8)
            }

        }

        @Test
        fun `part 1`() {
            val games = input.lines().map { parse(it) }
            expectThat(games.sumIdsOfPossibleGames(requirements))
                .isEqualTo(2449)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `game 1 could be played with 4 red, 2 green, 6 blue`() {
            val game1 = exampleInput.lines().first().let { parse(it) }
            expectThat(game1.minimumPossible())
                .isEqualTo(listOf(Cube(RED, 4), Cube(GREEN, 2), Cube(BLUE, 6)))
        }

        @Test
        fun `example 2`() {
            val games = exampleInput.lines().map { parse(it) }
            expectThat(games.sumOfPowers())
                .isEqualTo(2286)
        }

        @Test
        internal fun `part 2`() {
            val games = input.lines().map { parse(it) }
            expectThat(games.sumOfPowers())
                .isEqualTo(63981)
        }
    }

    private val input by lazy { readFile("/input-day02.txt")}

}
