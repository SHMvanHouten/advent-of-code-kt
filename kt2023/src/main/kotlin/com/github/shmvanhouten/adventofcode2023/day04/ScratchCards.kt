package com.github.shmvanhouten.adventofcode2023.day04

import java.math.BigInteger.TWO

fun sumCardScores(input: String) = input.lines().sumOf { getCardScore(it) }

fun getCardScore(input: String): Int {
    val cardCount = getCardCount(input)
    return if (cardCount == 0) 0
    else TWO.pow(cardCount - 1).toInt()
}

fun getScratchCardTotal(cards: List<String>): Int {
    val cardPiles = MutableList(cards.size) {1}
    var totalNrOfCardScratched = 0
    cards.indices.forEach { index ->
        val nrOfCards = cardPiles[index]
        totalNrOfCardScratched += nrOfCards

        val nrOfPiles = getCardCount(cards[index])
        for (nextCardI in (index + 1)..nrOfPiles + index) {
            cardPiles[nextCardI] += nrOfCards
        }
    }
    return totalNrOfCardScratched
}

private fun getCardCount(input: String): Int {
    val (winningNrs, myNrs) = parse(input)
    return myNrs.count { winningNrs.contains(it) }
}

private fun parse(input: String): List<List<Int>> =
    input
        .substringAfter(": ").split(" | ")
        .map { nrList ->
            nrList.split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }