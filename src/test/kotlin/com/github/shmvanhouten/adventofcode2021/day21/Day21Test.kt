package com.github.shmvanhouten.adventofcode2021.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
        internal fun `fixme`() {
            assertThat(1, equalTo(1))
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1))
        }

    }

    private val input by lazy { readFile("/input-day21.txt") }

}

private fun parse(input: String): Pair<Player, Player> {
    val (player1, player2) = input.lines().map { it.last() }.map { it.digitToInt() }
    return Player(player1) to Player(player2)
}
