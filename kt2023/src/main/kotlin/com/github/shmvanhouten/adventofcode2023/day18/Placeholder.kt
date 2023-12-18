package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.strings.words

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

fun dig(instructions: List<String>): Int = dig(instructions.map { toInstruction(it) })

fun dig(instructions: List<Instruction>): Int {
    val trench = mutableSetOf<CoordinateProgression>()
    var current = Coordinate(0, 0)

    val (dir, s) = instructions.first()
    val new = current.move(dir, s)
    trench += current..new

    current = new
    var turns = 0

    instructions.windowed(2).forEach { (prev, new) ->
        val (direction, steps) = new
        val newLoc = current.move(direction, steps)
        trench += current..newLoc
        current = newLoc
        if(prev.direction.turnLeft() == direction) turns -= 1
        else if( prev.direction.turnRight() == direction) turns += 1
    }

    return shoeLace(trench.map { it.abs() }, turns > 0)
}

fun shoeLace(trenches: List<CoordinateProgression>, rightTurn: Boolean): Int {
    trenches.windowed(2).map {  }
}

data class Instruction(val direction: Direction, val distance: Int)




fun extrapolate(inside: List<Coordinate>, trench: MutableSet<CoordinateProgression>): MutableSet<Coordinate> {
    val checked = inside.toMutableSet()
    var unchecked = checked.flatMap{
        it.getSurroundingManhattan().filter { !checked.contains(it) && trench.none{r -> r.contains(it)} }
    }
    while (unchecked.isNotEmpty()) {
        checked += unchecked
        unchecked = unchecked.flatMap { it.getSurroundingManhattan().filter { !checked.contains(it) && trench.none{r -> r.contains(it)} }}
    }
    return checked
}
