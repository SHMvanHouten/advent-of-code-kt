package com.github.shmvanhouten.adventofcode2021.day21

private val range = 1..3
private val possibleTripleDiceRollsLikelyHood = range
    .flatMap { n -> range.flatMap { n2 -> range.map { n3 -> n + n2 + n3 } } }
    .groupingBy { it }.eachCount()
val diePermutations = possibleTripleDiceRollsLikelyHood
    .flatMap { (roll, likelyHood) ->
        possibleTripleDiceRollsLikelyHood.map { (roll2, likelyHood2) ->
            Pair(roll, roll2) to likelyHood * likelyHood2.toLong()
        }
    }

fun evolveToWinningGameStates(player1: Player, player2: Player): Pair<Long, Long> {
    val gameStatesToEvolve = mutableMapOf((GameState(player1, player2) to 1L))
    val gameStatesPlayer1Won = mutableMapOf<GameState, Long>()
    val gameStatesPlayer2Won = mutableMapOf<GameState, Long>()

    while (gameStatesToEvolve.isNotEmpty()) {
        val (gameState, count) = gameStatesToEvolve.poll()
        if (gameState.player1.wins()) gameStatesPlayer1Won.merge(gameState, count, Long::plus)
        else if (gameState.player2.wins()) gameStatesPlayer2Won.merge(gameState, count, Long::plus)
        else
            permuteNextGameStates(gameState)
                .map { it.first to it.second * count }
                .forEach { (gs, occurrence) -> gameStatesToEvolve.merge(gs, occurrence, Long::plus) }

    }

    return gameStatesPlayer1Won.values.reduce(Long::plus)/ permutationsWherePlayer2RolledAfterHeAlreadyLost() to
            gameStatesPlayer2Won.values.reduce(Long::plus)
}

private fun permutationsWherePlayer2RolledAfterHeAlreadyLost() =
    possibleTripleDiceRollsLikelyHood.values.reduce(Int::plus)

fun permuteNextGameStates(gameState: GameState): List<Pair<GameState, Long>> {
    return diePermutations
        .map { (rolls, likelyHood) -> gameState.doRolls(rolls) to likelyHood }
}

data class GameState(val player1: Player, val player2: Player) {
    fun doRolls(rolls: Pair<Int, Int>): GameState {
        return GameState(
            player1.roll(rolls.first),
            player2.roll(rolls.second)
        )
    }

}

data class Player(val position: Int, val score: Long = 0) {
    fun roll(diceRoll: Int): Player {
        val position1 = calculatePosition(position, diceRoll)
        return Player(
            position1, score + position1
        )
    }

    fun wins() = score >= 21
}

fun calculatePosition(position: Int, die: Int): Int {
    return (position + die - 1) % 10 + 1
}

private fun <K, V> MutableMap<K, V>.poll(): MutableMap.MutableEntry<K, V> {
    val entry = this.entries.first()
    this.remove(entry.key)
    return entry
}
