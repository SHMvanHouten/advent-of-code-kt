package com.github.shmvanhouten.adventofcode2022.day09

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import java.lang.Math.abs

class Bridge {
    private var headPosition = Coordinate(0,0)
    private var tailPosition = Coordinate(0,0)
    val placesVisitedByTail = mutableSetOf(tailPosition)

    fun follow(instructions: String): Bridge {
        return follow(instructions.lines())
    }

    private fun follow(instructions: List<String>): Bridge {
        instructions.map { it.words() }
            .map { (direction, nrOfSteps) -> direction.toDirection() to nrOfSteps.toInt() }
            .forEach { (direction, nrOfSteps) ->
                moveHeadAndTail(direction, nrOfSteps)
            }

        return this
    }

    fun countPlacesTailVisited(): Int {
        return placesVisitedByTail.count()
    }

    private fun moveHeadAndTail(direction: Direction, nrOfSteps: Int) {
        repeat(nrOfSteps) {
            val oldHeadPosition = headPosition
            headPosition = headPosition.move(direction)

            if (abs(headPosition.x - tailPosition.x) > 1) {
                tailPosition = Coordinate(oldHeadPosition.x, headPosition.y)

            } else if (abs(headPosition.y - tailPosition.y) > 1) {
                tailPosition = Coordinate(headPosition.x, oldHeadPosition.y)

            } else {
                // do nothing
            }

            placesVisitedByTail += tailPosition
        }
    }

}

internal fun String.toDirection(): Direction {
    return when(this) {
        "R" -> Direction.EAST
        "L" -> Direction.WEST
        "U" -> Direction.NORTH
        "D" -> Direction.SOUTH
        else -> error("unknown direction $this")
    }
}

internal fun String.words(): List<String> {
    return split(' ')
}
