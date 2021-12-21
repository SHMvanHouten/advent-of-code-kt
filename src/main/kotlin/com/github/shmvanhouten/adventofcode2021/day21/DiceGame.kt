package com.github.shmvanhouten.adventofcode2021.day21


class DiceGame(
    val player1: Player,
    val player2: Player,
    val deterministicDie: DeterministicDie = DeterministicDie()
) {
    fun playOut(): DiceGame {
        return play(this)
    }

    private fun play(diceGame: DiceGame) = generateSequence(diceGame) { game ->
        val (player1, die1) = game.player1.roll(game.deterministicDie)
        if (player1.wins()) return@generateSequence DiceGame(player1, game.player2, die1)
        val (player2, die2) = game.player2.roll(die1)
        DiceGame(player1, player2, die2)
    }.dropWhile { !it.hasAWinner() }
        .first()



    fun losingPlayer(): Player {
        return minOf(player1, player2, PlayerComparator())
    }

    fun hasAWinner(): Boolean {
       return player1.wins() || player2.wins()
    }
}

data class DeterministicDie(val value: Int = 1, val rolls: Long = 0) {
    fun roll(): Pair<DeterministicDie, Int> {
        var rollWorth = 0
        var newValue = value
        repeat(3) {
            rollWorth += newValue
            newValue = (newValue + 1) % 100
            if(newValue == 0) newValue = 100
        }
        return DeterministicDie(newValue, rolls + 3) to rollWorth
    }
}

fun roll(thePlayer: Player, deterministicDie: DeterministicDie): Pair<Player, DeterministicDie> {
    var player = thePlayer
    val (die, value) = deterministicDie.roll()

    val newPosition = calculatePosition(player.position, value)
    player = Player(newPosition, player.score + newPosition)
    return player to die
}

private fun calculatePosition(position: Int, die: Int): Int {
    val n = (position + die) % 10
    return if (n == 0) 10
    else n
}


data class Player(val position: Int, val score: Long = 0) {

    fun roll(deterministicDie: DeterministicDie) = roll(this, deterministicDie)
    fun wins(): Boolean {
        return score >= 1000
    }
}

class PlayerComparator : Comparator<Player> {
    override fun compare(o1: Player?, o2: Player?): Int {
        if (o1 == null || o2 == null) error("null player")
        return o1.score.compareTo(o2.score)
    }
}