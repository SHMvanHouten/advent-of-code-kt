package com.github.shmvanhouten.adventofcode2024.day21

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

private const val UP: Button = "^"
private const val A: Button = "A"
private const val RIGHT: Button = ">"
private const val DOWN: Button = "v"
private const val LEFT: Button = "<"
private const val NONE: Button = "?"

val memo = mutableMapOf<List<Button>, List<Button>>()
val buttonsToPressCountAtDepth = mutableMapOf<Sequence, Map<Int, Long>>()

typealias Sequence = List<Button>

fun calculateComplexityNBots(sequence: String, nrOfBots:Int = 25): Long {

    val directionalBot = DirectionalBot()
    val originalSequence = NumericBot().buttonsToPress(sequence)
    return originalSequence.breakIntoSequencesUntilAs()
        .sumOf {
            directionalBot.buttonsToPress(it, nrOfBots)
        } * sequence.substringBefore("A").toLong()
}

fun calculateComplexity(sequence: String): Long {
    return NumericBot().buttonsToPress(sequence)
        .let { DirectionalBot().buttonsToPress(it) }
        .let { DirectionalBot().buttonsToPress(it) }.size * sequence.substringBefore("A").toLong()
}

class NumericBot {
    fun buttonsToPress(sequence: String): List<Button> {
        var oldPos = numericGrid['A']!!
        return sequence.flatMap { c ->
            val newPos = numericGrid[c]!!
            val result = sequenceToInput(oldPos, newPos, numericGrid[' ']!!, couldPassOverNullButton(oldPos, newPos))
            oldPos = newPos
            result
        }
    }

    private fun couldPassOverNullButton(oldPos: Coordinate, newPos: Coordinate): Boolean {
        val nullButton = numericGrid[' ']!!
        return (oldPos.y == nullButton.y || newPos.y == nullButton.y)
                && (oldPos.x == nullButton.x || newPos.x == nullButton.x)
    }
}

class DirectionalBot {
    fun buttonsToPress(sequence: List<Button>): List<Button> {
        if(memo.contains(sequence)) return memo[sequence]!!
        var oldPos = directionalGrid[A]!!

        return sequence.map { b ->
            val newPosition = directionalGrid[b]!!
            val result = sequenceToInput(oldPos, newPosition, directionalGrid[NONE]!!, true)
            oldPos = newPosition
            result
        }.flatten()
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

    fun buttonsToPress(sequence: List<Button>, nrOfBots: Int, depth: Int = 1): Long {
        if(depth == nrOfBots) {
            return buttonsToPress(sequence).size.toLong()
        }
        return buttonsToPress(sequence).breakIntoSequencesUntilAs().sumOf {
            buttonsToPress(it, nrOfBots, depth + 1)
        }
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

private fun sequenceToInput(oldPos: Coordinate, newPos: Coordinate, posToAvoid: Coordinate, shouldCareAboutMissingB: Boolean): List<Button> {
    val sequence = mutableListOf<Button>()
    if(shouldCareAboutMissingB) {
        if (posToAvoid.y == newPos.y || posToAvoid.x == oldPos.x) {
            // prefer left and right
            sequence.addAll(0.until(oldPos.x - newPos.x).map { LEFT })
            sequence.addAll(0.until(newPos.x - oldPos.x).map { RIGHT })
            sequence.addAll(0.until(oldPos.y - newPos.y).map { UP })
            sequence.addAll(0.until(newPos.y - oldPos.y).map { DOWN })
        } else if (posToAvoid.y == oldPos.y || posToAvoid.x == newPos.x) {
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
    } else {
        // prefer left to up
        sequence.addAll(0.until(oldPos.x - newPos.x).map { LEFT })
        sequence.addAll(0.until(newPos.y - oldPos.y).map { DOWN })
        sequence.addAll(0.until(oldPos.y - newPos.y).map { UP })
        sequence.addAll(0.until(newPos.x - oldPos.x).map { RIGHT })
    }

    sequence.add(A)
    return sequence
}

typealias Button = String

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

fun List<Button>.breakIntoSequencesUntilAs(): List<List<Button>> {
    return buildList {
        var newList = mutableListOf<Button>()
        this@breakIntoSequencesUntilAs.forEach {
            newList+= it
            if(it == "A") {
                add(newList)
                newList = mutableListOf()
            }
        }
    }
}

fun <T> Grid<T>.toMap(): Map<T, Coordinate> { // todo move
    return grid.flatMapIndexed { y, row ->
        row.mapIndexed { x, t -> t to Coordinate(x, y) }
    }.toMap()
}