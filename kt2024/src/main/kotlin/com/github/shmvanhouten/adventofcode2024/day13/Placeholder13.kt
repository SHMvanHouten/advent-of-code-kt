package com.github.shmvanhouten.adventofcode2024.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import java.math.BigInteger

fun main() {
    parse(readFile("/input-day13.txt"))
//    parse(example)
        .mapNotNull { findCheapestWayToPrize(it) }
        .onEach(::println)
        .sum().also { println(it) }
}

private fun findCheapestWayToPrize(clawMachine: ClawMachine): Int? {
    val (buttonA, buttonB, prize) = clawMachine
    val buttonAPressesAndResultingPrize = generateSequence(Coordinate(0, 0)) { it + buttonA }
        .map { prize - it }
        .takeWhile { prize.x > 0 && prize.y > 0 }
        .take(100)
        .toList()
    return buttonAPressesAndResultingPrize.mapIndexedNotNull { aPresses, resPrize ->
        val buttonBPresses = findButtonBsToPrize(buttonB, resPrize)
        if(buttonBPresses != null) aPresses * 3 + buttonBPresses
        else null
    }.minOrNull()
}

fun findButtonBsToPrize(buttonB: Coordinate, resPrize: Coordinate): Int? {
    val (divideX, remainderX) = resPrize.x.toBigInteger().divideAndRemainder(buttonB.x.toBigInteger())
    val (divideY, remainderY) = resPrize.y.toBigInteger().divideAndRemainder(buttonB.y.toBigInteger())
    return if(remainderX == BigInteger.ZERO && remainderY == BigInteger.ZERO && divideY.equals(divideX)) {
        divideX.toInt()
    } else null
}

private fun parse(input: String): List<ClawMachine> = input.blocks()
    .map { toClawMachine(it) }

fun toClawMachine(input: String): ClawMachine {
    val (buttonA, buttonB, prize) = input.lines()
    return ClawMachine(
        toButton(buttonA),
        toButton(buttonB),
        toPrize(prize)
    )
}

fun toButton(raw: String): Coordinate {
    val (x, y) = raw.substringAfter(": ").split(", ")
        .map { it.substringAfter("+") }
    return Coordinate(x.toInt(), y.toInt())
}

fun toPrize(raw: String): Coordinate {
    val (x, y) = raw.substringAfter(": ").split(", ")
        .map { it.substringAfter("=") }
        .map { it.toInt() }
    return Coordinate(x, y)
}

data class ClawMachine(
    val buttonA: Coordinate,
    val buttonB: Coordinate,
    val prize: Coordinate
)

private val example = """
    Button A: X+94, Y+34
    Button B: X+22, Y+67
    Prize: X=8400, Y=5400

    Button A: X+26, Y+66
    Button B: X+67, Y+21
    Prize: X=12748, Y=12176

    Button A: X+17, Y+86
    Button B: X+84, Y+37
    Prize: X=7870, Y=6450

    Button A: X+69, Y+23
    Button B: X+27, Y+71
    Prize: X=18641, Y=10279
""".trimIndent()