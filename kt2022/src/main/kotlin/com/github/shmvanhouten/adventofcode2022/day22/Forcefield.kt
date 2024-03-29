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

class Board(
    input: String,
    private val isCube: Boolean = false
) {
    private val map = input.blocks().first().toCoordinateMap().filter { it.value != ' ' }
    private val instructions = input.blocks()[1]
    private val cubeMap = CubeMap()

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
                val (newPosition, newDirection) = move(currentPosition, currentFacing, steps.toInt())
                currentPosition = newPosition
                currentFacing = newDirection
//                println("current position: $currentPosition")
            }
        }
        return currentPosition to currentFacing
    }

    private fun move(currentPosition: Coordinate, facing: Direction, steps: Int): Pair<Coordinate, Direction> {
        var direction = facing
        var position = currentPosition
        0.until(steps).forEach{
            val newPosition = position.move(direction)
            if(map.contains(newPosition) && map[newPosition] == '#') return position to direction
            if(!map.contains(newPosition)) {
                val (wrapped, newDir) = wrapAround(position, direction)
                if(map[wrapped] == '#') return position to direction // not new direction! we did not wrap around!
                position = wrapped
                direction = newDir
            } else position = newPosition
        }
        return position to direction
    }

    private fun wrapAround(position: Coordinate, direction: Direction): Pair<Coordinate, Direction> {
        return if(isCube) wrapCube(position, direction)
        else farthestInDirection(position, direction.opposite()) to direction
    }

    private fun wrapCube(position: Coordinate, direction: Direction): Pair<Coordinate, Direction> {
        return cubeMap.getNewPositionAndDirection(position, direction)
    }

    private fun farthestInDirection(
        position: Coordinate,
        direction: Direction
    ): Coordinate {
        var pos = position
        while (true) {
            val move = pos.move(direction = direction)
            if (!map.contains(move)) return pos
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

class CubeMap {
    private val coordinatePairings: List<List<Pair<Coordinate,Coordinate>>> = listOf(
/*A-J*/    (Coordinate(50,0)..Coordinate(99,0)).zip(Coordinate(0,150)..Coordinate(0,199)), // RIGHT, LEFT
/*B-I*/    (Coordinate(100,0)..Coordinate(149,0)).zip(Coordinate(0,199)..Coordinate(49,199)), // SAME DIR
/*C-F*/    (Coordinate(149,0)..Coordinate(149,49)).zip(Coordinate(99,149)..Coordinate(99,100)), // REVERSE
/*D-E*/    (Coordinate(100,49)..Coordinate(149,49)).zip(Coordinate(99,50)..Coordinate(99,99)), // RIGHT,LEFT
/*G-H*/    (Coordinate(50,149)..Coordinate(99,149)).zip((Coordinate(49,150)..Coordinate(49,199))), // RIGHT< LEFT
/*K-N*/    (Coordinate(0, 100)..Coordinate(0,149)).zip(Coordinate(50,49)..Coordinate(50,0)), // REVERSE
/*L-M*/    (Coordinate(0,100)..Coordinate(49,100)).zip(Coordinate(50,50)..Coordinate(50,99)) // RIGHT, LEFT
    )

    private val directionChanges: List<List<Pair<Direction, Direction>>> = listOf(
/*A*/   listOf((Direction.NORTH to Direction.EAST), (Direction.WEST to Direction.SOUTH)),
/*B*/   listOf((Direction.NORTH to Direction.NORTH), (Direction.SOUTH to Direction.SOUTH)),
/*C*/   listOf((Direction.EAST to Direction.WEST), (Direction.EAST to Direction.WEST)),
/*D*/   listOf((Direction.SOUTH to Direction.WEST), (Direction.EAST to Direction.NORTH)),
/*G*/   listOf((Direction.SOUTH to Direction.WEST), (Direction.EAST to Direction.NORTH)),
/*K*/   listOf((Direction.WEST to Direction.EAST), (Direction.WEST to Direction.EAST)),
/*L*/   listOf((Direction.NORTH to Direction.EAST), (Direction.WEST to Direction.SOUTH))

    )

    fun getNewPositionAndDirection(position: Coordinate, direction: Direction): Pair<Coordinate, Direction> {
        val indexToCoordPair = coordinatePairings.mapIndexedNotNull { index, coordPairs ->
            coordPairs.find { it.first == position || it.second == position }?.let { index to it }
        }
        assert(indexToCoordPair.isNotEmpty() && indexToCoordPair.size < 3)
        val (i, pair) = getCoordinatePairingForDirection(indexToCoordPair, direction)
        val pairs = directionChanges[i]
        val newDirection = pairs.first { it.first == direction }.second
        val newPosition = if(pair.first == position) pair.second
        else if(pair.second == position) pair.first
        else error("this should not happen!")
        return newPosition to newDirection
    }

    private fun getCoordinatePairingForDirection(
        indexToCoordPair: List<Pair<Int, Pair<Coordinate, Coordinate>>>,
        direction: Direction
    ): Pair<Int, Pair<Coordinate, Coordinate>> {
        if(indexToCoordPair.size == 2) {
            val (i, firstPair) = indexToCoordPair[0]
            val (i2, secondPair) = indexToCoordPair[1]
            val firstPairMatches = directionChanges[i].any { it.first == direction }
            val secondPairMatches = directionChanges[i2].any { it.first == direction }
            if(firstPairMatches && secondPairMatches) {
                error("both pairs are possible...?")
            }
            if(firstPairMatches) {
                return i to firstPair
            } else {
                return i2 to secondPair
            }
        } else if(indexToCoordPair.size == 1) {
            return indexToCoordPair.first()
        } else error("unexpected size for result $indexToCoordPair")
    }
}