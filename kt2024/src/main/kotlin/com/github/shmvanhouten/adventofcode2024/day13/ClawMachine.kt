package com.github.shmvanhouten.adventofcode2024.day13

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import java.math.BigInteger

fun calculateMinimumTokensToSpendToGetToPrize(clawMachines: List<ClawMachine>): BigInteger {
    return clawMachines
        .mapNotNull { findCheapestWayToPrize(it) }
        .sum()
}

private fun findCheapestWayToPrize(clawMachine: ClawMachine): BigInteger? {
    val (buttonA, buttonB, prize) = clawMachine
    val (buttonBPresses, rem) = (buttonA.x * prize.y - buttonA.y * prize.x).negate().divideAndRemainder(buttonA.y * buttonB.x - buttonA.x * buttonB.y)
    if (rem != BigInteger.ZERO) return null
    val buttonAPresses = (prize.x - buttonB.x * buttonBPresses) / buttonA.x
    return (buttonAPresses * BigInteger.valueOf(3) + buttonBPresses)
}

fun parseClawMachines(input: String): List<ClawMachine> = input.blocks()
    .map { toClawMachine(it) }

fun toClawMachine(input: String): ClawMachine {
    val (buttonA, buttonB, prize) = input.lines()
    return ClawMachine(
        toButton(buttonA),
        toButton(buttonB),
        toPrize(prize)
    )
}

fun toButton(raw: String): Vec2 {
    val (x, y) = raw.substringAfter(": ").split(", ")
        .map { it.substringAfter("+") }
    return Vec2(x.toBigInteger(), y.toBigInteger())
}

fun toPrize(raw: String): Vec2 {
    val (x, y) = raw.substringAfter(": ").split(", ")
        .map { it.substringAfter("=") }
        .map { it.toBigInteger() }
    return Vec2(x, y)
}

fun Collection<BigInteger>.sum() = reduce { acc, bigInteger -> acc.plus(bigInteger) }

data class ClawMachine(
    val buttonA: Vec2,
    val buttonB: Vec2,
    val prize: Vec2
)

data class Vec2(
    val x: BigInteger,
    val y: BigInteger
) {
    fun plus(l: BigInteger): Vec2 {
        return this.copy(x + l, y + l)
    }
}
