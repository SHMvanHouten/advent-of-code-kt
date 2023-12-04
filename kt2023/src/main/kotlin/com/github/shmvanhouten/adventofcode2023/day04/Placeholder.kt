package com.github.shmvanhouten.adventofcode2023.day04

import java.math.BigInteger
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO

fun getScratchCardTotal(input: String): Int {
    return getScratchCardTotal(input.lines())
}

private fun getScratchCardTotal(cards: List<String>): Int {
    val cardScores = cards.map { getCardCount(it) }
    val cardCounts = List(cards.size) { 1 }.toMutableList()
    cardScores.forEachIndexed { index, score ->
        for (i in (index + 1)..score + index) {
            cardCounts[i] += cardCounts[index]
        }
    }
    return cardCounts.sum()
}

fun getCardScore(input: String): BigInteger {
    return getCardCount(input)
        .let {
            if (it == 0) ZERO
            else TWO.pow(it - 1)
        }
}

fun getCardCount(input: String): Int {
    val (winningNrs, myNrs) = parse(input)
    return myNrs.count { winningNrs.contains(it) }
}

private fun parse(input: String): List<List<Int>> = input
    .substringAfter(": ").split(" | ")
    .map { nrList ->
        nrList.split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
    }