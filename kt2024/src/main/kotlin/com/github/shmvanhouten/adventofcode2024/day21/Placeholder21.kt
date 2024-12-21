package com.github.shmvanhouten.adventofcode2024.day21

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode2024.day21.Button.*

fun calculateComplexity(sequence: String): Long {
    return NumericBot().buttonsToPress(sequence)
        .let { DirectionalBot().buttonsToPress(it) }
        .let { DirectionalBot().buttonsToPress(it) }.size * sequence.substringBefore("A").toLong()
}

class NumericBot {
    fun buttonsToPress(sequence: String): List<Button> {
        var oldPos = numericGrid['A']!!
        return sequence.flatMap { c ->
            val newPosition = numericGrid[c]!!
            val result = sequenceToInput(oldPos, newPosition, numericGrid[' ']!!)
            oldPos = newPosition
            result
        }
    }
}

class DirectionalBot {
    fun buttonsToPress(sequence: List<Button>): List<Button> {
        var oldPos = directionalGrid[A]!!
        return sequence.flatMap { b ->
            val newPosition = directionalGrid[b]!!
            val result = sequenceToInput(oldPos, newPosition, directionalGrid[NONE]!!)
            oldPos = newPosition
            result
        }
    }

    fun pressDirectional(input: List<Button>): List<Button> {
        val buttons = mutableListOf<Button>()
        var position = directionalGrid[A]!!
        input.forEach {
            when(it) {
                UP -> position = position.move(Direction.NORTH)
                A -> buttons += directionalGrid.entries.first { it.value == position }.key
                RIGHT -> position = position.move(Direction.EAST)
                DOWN -> position = position.move(Direction.SOUTH)
                LEFT -> position = position.move(Direction.WEST)
                NONE -> error("cannot have none button")
            }
        }
        return buttons
    }

    fun pressNumeric(input: List<Button>): String {
        var position = numericGrid['A']!!
        return buildString {
            input.forEach{
                when(it) {
                    UP -> position = position.move(Direction.NORTH)
                    A -> append(numericGrid.entries.first { it.value == position }.key)
                    RIGHT -> position = position.move(Direction.EAST)
                    DOWN -> position = position.move(Direction.SOUTH)
                    LEFT -> position = position.move(Direction.WEST)
                    NONE -> error("cannot have none button")
                }
            }
        }
    }
}

private fun sequenceToInput(oldPos: Coordinate, newPos: Coordinate, posToAvoid: Coordinate): List<Button> {
    val sequence = mutableListOf<Button>()
    if(posToAvoid.y == newPos.y || posToAvoid.x == oldPos.x) {
        // prefer left and right
        sequence.addAll(0.until(oldPos.x - newPos.x).map { LEFT })
        sequence.addAll(0.until(newPos.x - oldPos.x).map { RIGHT })
        sequence.addAll(0.until(oldPos.y - newPos.y).map { UP })
        sequence.addAll(0.until(newPos.y - oldPos.y).map { DOWN })
    } else if(posToAvoid.y == oldPos.y || posToAvoid.x == newPos.x) {
        // prefer up and down
        sequence.addAll(0.until(oldPos.y - newPos.y).map { UP })
        sequence.addAll(0.until(newPos.y - oldPos.y).map { DOWN })
        sequence.addAll(0.until(newPos.x - oldPos.x).map { RIGHT })
        sequence.addAll(0.until(oldPos.x - newPos.x).map { LEFT })
    } else {
        sequence.addAll(0.until(oldPos.x - newPos.x).map { LEFT })
        sequence.addAll(0.until(newPos.y - oldPos.y).map { DOWN })
        sequence.addAll(0.until(newPos.x - oldPos.x).map { RIGHT })
        sequence.addAll(0.until(oldPos.y - newPos.y).map { UP })
    }

    sequence.add(A)
    return sequence
}

enum class Button(val va: String) {
    UP("^"),
    A("A"),
    RIGHT(">"),
    DOWN("v"),
    LEFT("<"),
    NONE("?");
}

fun fromVal(va: String): Button {
    return when(va) {
        "^" -> UP
        "A" -> A
        ">" -> RIGHT
        "v" -> DOWN
        "<" -> LEFT
        else -> error("no button for $va")
    }
}

private val numericGrid: Map<Char, Coordinate> = Grid(buildList {
    add(listOf('7', '8', '9'))
    add(listOf('4', '5', '6'))
    add(listOf('1', '2', '3'))
    add(listOf(' ', '0', 'A'))
}).toMap()

private val directionalGrid: Map<Button, Coordinate> = Grid(buildList {
    add(listOf(NONE,  UP,    A))
    add(listOf(LEFT, DOWN, RIGHT))
}).toMap()

fun <T> Grid<T>.toMap(): Map<T, Coordinate> { // todo move
    return grid.flatMapIndexed { y, row ->
        row.mapIndexed { x, t -> t to Coordinate(x, y) }
    }.toMap()
}