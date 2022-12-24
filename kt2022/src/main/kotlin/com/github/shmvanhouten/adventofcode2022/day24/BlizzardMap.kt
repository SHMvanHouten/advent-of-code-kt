package com.github.shmvanhouten.adventofcode2022.day24

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

class BlizzardMap(input:String) {
    private val states = listOf(MapState(input)).toMutableList()

    fun getState(i: Int): MapState {
        return if(states.lastIndex >= i) states[i]
        else if(i == states.lastIndex + 1) {
            val newState = states.last().tick()
            states += newState
            newState
        } else {
            error("skipped states! Asked for $i, but last index was ${states.lastIndex}")
        }
    }

}

class MapState(
    val blizzards: List<Pair<Coordinate, Blizzard>>,
    val bottomRight: Coordinate,
    val occupiedSpace: Set<Coordinate> = blizzards.map { it.first }.toSet()
) {
    constructor(blizzards: String) : this(
        blizzardCoordinateSet(blizzards),
        Coordinate(blizzards.lines()[0].lastIndex, blizzards.lines().lastIndex)
    )

    fun draw(): String {
        val (minX, minY) = 0 to 0
        val (maxX, maxY) = bottomRight
        return (minY..maxY).joinToString("\n") { y ->
            (minX..maxX).map { x ->
                if(y == minY && x == 1 || y == maxY && x == maxX - 1) return@map '.'
                else if(x == minX || x == maxX || y == minY || y == maxY) return@map '#'
                val blizzard = blizzards.find { it.first == Coordinate(x, y) }
                blizzard?.second?.char ?: '.'
            }.joinToString("")
        }
    }

    fun tick(): MapState {
        return MapState(blizzards.map { move(it.first, it.second) }, bottomRight)
    }

    private fun move(location: Coordinate, type: Blizzard): Pair<Coordinate, Blizzard> {
        val newLoc = location.move(type.direction)
        return nextStep(newLoc) to type
    }

    private fun nextStep(newLoc: Coordinate): Coordinate {
        return if (newLoc.x == 0) newLoc.copy(x = bottomRight.x - 1)
        else if (newLoc.x == bottomRight.x) newLoc.copy(x = 1)
        else if (newLoc.y == 0) newLoc.copy(y = bottomRight.y - 1)
        else if (newLoc.y == bottomRight.y) newLoc.copy(y = 1)
        else newLoc
    }

    fun hasNoBlizzardAt(location: Coordinate): Boolean {
        return !occupiedSpace.contains(location)
    }

}

fun blizzardCoordinateSet(blizzards: String): List<Pair<Coordinate, Blizzard>> {
    return blizzards.toCoordinateMap()
        .map { it.toPair() }
        .filter { (_, c) -> c != '#' && c != '.' }
        .map { it.first to Blizzard(it.second) }
}

data class Blizzard(val char: Char, val direction: Direction = toDirection(char))

fun toDirection(char: Char): Direction = when(char) {
    '^' -> Direction.NORTH
    '>' -> Direction.EAST
    'v' -> Direction.SOUTH
    '<' -> Direction.WEST
    else -> error("unknow direction $char")
}
