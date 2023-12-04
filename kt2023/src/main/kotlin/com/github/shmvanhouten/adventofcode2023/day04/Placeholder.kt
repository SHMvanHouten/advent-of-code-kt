package com.github.shmvanhouten.adventofcode2023.day04

import java.math.BigInteger

fun getCardScore(input: String): BigInteger {
    val (winningNrs, myNrs) = parse(input)
    return myNrs.count { winningNrs.contains(it) }
        .let {
            if(it == 0) BigInteger.ZERO
            else BigInteger.TWO.pow(it - 1)
        }
}

private fun parse(input: String): Pair<List<Int>, List<Int>> {
    val (winningNrs, myNrs) = input.substringAfter(": ").trim().split(" | ").map { it.trim() }
    return (winningNrs.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } to myNrs.split(" ").filter { it.isNotEmpty() }.map { it.toInt() })
}