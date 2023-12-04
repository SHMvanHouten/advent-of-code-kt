package com.github.shmvanhouten.adventofcode2023.day04

import java.math.BigInteger
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO

fun getScratchCardTotal(input: String): Int {
    val cardScores = input.lines().map { getCardCount(it) }
    val cardCounts = List(cardScores.size){1}.toMutableList()
    cardScores.forEachIndexed { index, score ->
        for (i in (index + 1)..score + index) {
            cardCounts[i] += cardCounts[index]
        }
    }
    return cardCounts.sum()
}

fun getCardCount(input: String): Int {
    val (winningNrs, myNrs) = parse(input)
    return myNrs.count { winningNrs.contains(it) }
}

fun getCardScore(input: String): BigInteger {
    return getCardCount(input)
        .let {
            if(it == 0) ZERO
            else TWO.pow(it - 1)
        }
}

private fun parse(input: String): Pair<List<Int>, List<Int>> {
    val (winningNrs, myNrs) = input.substringAfter(": ").trim().split(" | ").map { it.trim() }
    return (winningNrs.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } to myNrs.split(" ").filter { it.isNotEmpty() }.map { it.toInt() })
}