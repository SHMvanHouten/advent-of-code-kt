package com.github.shmvanhouten.adventofcode2022.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun password(position: Coordinate, facing: Direction): Long {
    val column = position.x + 1
    val row = position.y + 1
    val facingScore = facingScore(facing)
    return (1000L * row) + (4L * column) + facingScore
}

fun facingScore(facing: Direction): Long {
    return when(facing) {
        Direction.NORTH -> 3L
        Direction.EAST -> 0L
        Direction.SOUTH -> 1L
        Direction.WEST -> 2L
    }
}

class Board(input: String) {
    private val map = input.blocks().first().toCoordinateMap().filter { it.value != ' ' }
    private val instructions = input.blocks()[1]

    fun followInstructions(): Pair<Coordinate, Direction> {
        var currentPosition = topLeftPosition()
        var currentFacing = Direction.EAST
        var instructions = instructions
//        println("currentPosition = $currentPosition")
        while (instructions.isNotEmpty()) {
            if(instructions.first().isLetter()) {
                val turn = instructions.first().toTurn()
                instructions = instructions.substring(1)
                currentFacing = currentFacing.turn(turn)
//                println("now facing: $currentFacing")
            } else {
                val steps = instructions.takeWhile { it.isDigit() }
                instructions = instructions.substring(steps.length)
                currentPosition = move(currentPosition, currentFacing, steps.toInt())
//                println("current position: $currentPosition")
            }
        }
        return currentPosition to currentFacing
    }

    private fun move(currentPosition: Coordinate, facing: Direction, steps: Int): Coordinate {
        var position = currentPosition
        0.until(steps).forEach{
            val newPosition = position.move(facing)
            if(map.contains(newPosition) && map[newPosition] == '#') return position
            if(!map.contains(newPosition)) {
                val new = farthestInDirection(position, facing.opposite())
                if(map[new] == '#') return position
                position = new
            } else position = newPosition
        }
        return position
    }

    private fun farthestInDirection(position: Coordinate, direction: Direction): Coordinate {
        var pos = position
        while (true) {
            val move = pos.move(direction = direction)
            if(!map.contains(move)) return pos
            pos = move
        }
    }

    private fun topLeftPosition(): Coordinate {
        val maxY = 0
        val maxX = map.filter { it.key.y == maxY }.filter { it.value == '.' }.minOf { it.key.x }
        return Coordinate(maxX, maxY)
    }

}

private fun Char.toTurn(): Turn {
    return when(this) {
        'R' -> Turn.RIGHT
        'L' -> Turn.LEFT
        else -> error("unknown turn: $this")
    }
}
