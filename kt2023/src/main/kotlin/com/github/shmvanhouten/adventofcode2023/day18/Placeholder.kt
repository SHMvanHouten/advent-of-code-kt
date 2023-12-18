package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn.LEFT
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn.RIGHT
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGridFromCoordinates
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween
import com.github.shmvanhouten.adventofcode.utility.strings.words
import kotlin.math.abs

fun main() {
    readFile("/input-day18.txt")
        .lines()
        .map { toInstruction(it) }
//        .onEach(::println)
        .let { dig(it) }.also(::println)
}

fun toInstruction(line: String): Instruction {
    val (turn, steps) = line.words()
    val direction = when(turn) {
        "R" -> EAST
        "L" -> WEST
        "U" -> NORTH
        "D" -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, steps.toInt())
}

fun digHex(hexInstructions: String): Long {
    val trench = hexInstructions.lines()
        .map { toInstructionHex(it) }
//        .onEach { println(it) }
    return dig(trench)
}

fun toInstructionHex(line: String): Instruction {
    val ins = line.substringBetween("(#", ")")
    val direction = when(ins.last()) {
        '0' -> EAST
        '2' -> WEST
        '3' -> NORTH
        '1' -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, ins.substring(0, ins.lastIndex).toInt(16))
}

fun dig(instructions: String): Long = dig(instructions.lines().map { toInstruction(it) })

fun dig(instructions: List<Instruction>): Long {
    val trench = getTrench(instructions)

//    return shoeLace(trench.map{it.first})
    return calculateSurfaceArea(trench)
}

fun calculateSurfaceArea(trenchPlusTurns: List<Pair<Coordinate, Turn>>): Long {
    val rightTurn = trenchPlusTurns.map { it.second }.map { if(it == RIGHT) 1 else -1 }.sum() > 0
    val calculateAreaOf = if(rightTurn) RIGHT else LEFT

    val trench = trenchPlusTurns.map { it.first }

    val minX = trench.minOf { it.x }
    val maxX = trench.maxOf { it.x }
    val minY = trench.minOf { it.y }
    val maxY = trench.maxOf { it.y }
    val gross = (maxX.toLong() - minX) * (maxY - minY)
    if(Coordinate(minX, maxY) !in trench) {
        val bottomLeft = trench.single { it.x == minX && it.y in (minY + 1)..<maxY }
        val otherBottomLeft = trench.single { it.y == maxY && it.x in (minX + 1)..<maxX }
        return gross - ((bottomLeft.x - otherBottomLeft.x) * (bottomLeft.y - otherBottomLeft.y))
    }
    if(Coordinate(maxX, minY) !in trench) {
        val topRight = trench.single { it.y == minY && it.x in (minX + 1)..<maxX }
        val otherTopRight = trench.single { it.x == maxX && it.y in (minY + 1)..<maxY }
        return gross - ((otherTopRight.x - topRight.x) * (otherTopRight.y - topRight.y))
    }
    if(Coordinate(maxX, maxY) !in trench) {
        val bottomRight = trench.single { it.x == maxX && it.y in (minY + 1)..<maxY }
        val otherBottomRight = trench.single { it.y == maxY && it.x in (minX + 1)..<maxX }
        return gross - ((bottomRight.x - otherBottomRight.x) * (otherBottomRight.y - bottomRight.y))
    }
    return gross
 }

private fun getTrench(instructions: List<Instruction>): List<Pair<Coordinate, Turn>> {
    var current = Coordinate(0, 0)
    val trench = mutableListOf(current to getTurn(instructions.last().direction, instructions.first().direction))

    val (dir, s) = instructions.first()

    current = current.move(dir, s)
    trench += current to getTurn(dir, instructions[1].direction)

    instructions.windowed(2).forEach { (prev, new) ->
        val (direction, steps) = new
        current = current.move(direction, steps)

        val turn = getTurn(prev.direction, direction)

        trench += current to turn
    }
    return trench
}

private fun getTurn(direction: Direction, nextTurn: Direction) =
    when (direction) {
        nextTurn.turnLeft() -> RIGHT
        nextTurn.turnRight() -> LEFT
        else -> error("unsupported turn from $direction to $nextTurn")
    }

fun shoeLace(trenches: List<Coordinate>): Long {
    return abs(trenches.windowed(2)
        .map { (first, second) -> first.x.toLong() * second.y.toLong() - first.y.toLong() * second.x.toLong() }.sum() /2)
}

data class Instruction(val direction: Direction, val distance: Int)

fun toGrid(input: String): Grid<Char> {
    val trench = getTrench(input.lines().map { toInstruction(it) })
//        .onEach { println("turn ${it.second} to ${it.first}") }
        .map { it.first }
        .windowed(2).flatMap { (c1, c2) -> c1..c2 }
    return charGridFromCoordinates(trench)
}
