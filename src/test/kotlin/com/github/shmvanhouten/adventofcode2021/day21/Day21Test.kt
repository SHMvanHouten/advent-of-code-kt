package com.github.shmvanhouten.adventofcode2021.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.min

class Day21Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val example = """
                Player 1 starting position: 4
                Player 2 starting position: 8
                """.trimIndent()
            val (player1, player2) = parse(example)
            val diceGame = DiceGame(player1, player2)
            val played = diceGame.playOut()
            assertThat(played.losingPlayer().score, equalTo(745))
            assertThat(played.deterministicDie.rolls, equalTo(993))
        }

        @Test
        internal fun `part 1`() {
            val (player1, player2) = parse(input)
            val diceGame = DiceGame(player1, player2)
            val played = diceGame.playOut()
            assertThat(played.losingPlayer().score, equalTo(909))
            assertThat(played.deterministicDie.rolls, equalTo(999))
            assertThat(
                played.losingPlayer().score * played.deterministicDie.rolls,
                equalTo(908091)
            )
        }
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `each player rolling 1 3 times each will happen once`() {
            assertThat(diePermutations.toMap()[Pair(3, 3)], equalTo(1))
        }

        @ParameterizedTest(name = "player one rolling {0} and player 2 rolling {1} likelyHood: {2}")
        @CsvSource(
            value = [
                "3, 3, 1",
                "4, 3, 3",
                "3,4, 3",
                "6, 6, 49"
            ]
        )
        internal fun `player 1 rolling x and player one rolling y happens n times`(
            player1s3DiceRoll: Int,
            player2s3DiceRoll: Int,
            likelyHood: Long
        ) {
            assertThat(diePermutations.toMap()[Pair(player1s3DiceRoll, player2s3DiceRoll)], equalTo(likelyHood))
        }

        @Test
        internal fun example() {
            val example = """
                Player 1 starting position: 4
                Player 2 starting position: 8
                """.trimIndent()
            val (player1, player2) = parse2(example)
            val (onesWins, twosWins) = evolveToWinningGameStates(player1, player2)
            assertThat(twosWins, equalTo(341960390180808))
            assertThat(onesWins, equalTo(444356092776315))
        }

        @Test
        internal fun `part 2`() {
            val (player1, player2) = parse2(input)
            val (onesWins, twosWins) = evolveToWinningGameStates(player1, player2)
            println("one wins $onesWins, and 2 wins $twosWins")
            // 90710140491134 too low
            assertThat(min(onesWins, twosWins), equalTo(1))
        }

    }

    private val input by lazy { readFile("/input-day21.txt") }

}

private fun parse(input: String): Pair<PlayerPt1, PlayerPt1> {
    val (player1, player2) = input.lines().map { it.last() }.map { it.digitToInt() }
    return PlayerPt1(player1) to PlayerPt1(player2)
}

private fun parse2(input: String): Pair<Player, Player> {
    val (player1, player2) = input.lines().map { it.last() }.map { it.digitToInt() }
    return Player(player1) to Player(player2)
}

