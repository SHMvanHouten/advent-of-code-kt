package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun main() {
    readFile("/input-day18.txt")
        .lines()
//        .onEach(::println)
        .let { dig(it) }.also(::println)
}

fun dig(instructions: List<String>): Int {
    val trench = mutableSetOf<Coordinate>()
    val rightHand = mutableSetOf<Coordinate>()
    var current = Coordinate(0, 0)
    instructions.forEach { ins ->
        val (turn, steps) = ins.words()
        val direction = when(turn) {
            "R" -> EAST
            "L" -> WEST
            "U" -> NORTH
            "D" -> SOUTH
            else -> error("unknown $ins")
        }
        repeat(steps.toInt()) {
            trench.add(current)
            rightHand.add(current.move(direction.turnRight()))
            current = current.move(direction)
        }
    }

    return extrapolate(rightHand.filter { !trench.contains(it) }, trench).size + trench.size
}

fun extrapolate(inside: List<Coordinate>, trench: MutableSet<Coordinate>): MutableSet<Coordinate> {
    val checked = inside.toMutableSet()
    var unchecked = checked.flatMap{
        it.getSurroundingManhattan().filter { !checked.contains(it) && !trench.contains(it) }
    }
    while (unchecked.isNotEmpty()) {
        checked += unchecked
        unchecked = unchecked.flatMap { it.getSurroundingManhattan().filter { !checked.contains(it) && !trench.contains(it) } }
    }
    return checked
}
