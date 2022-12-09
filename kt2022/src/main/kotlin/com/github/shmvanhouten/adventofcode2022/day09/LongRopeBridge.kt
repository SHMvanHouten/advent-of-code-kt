package com.github.shmvanhouten.adventofcode2022.day09

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import kotlin.math.abs

class LongRopeBridge(private val amountOfKnots: Int = 2) {
    private var knotPositions = MutableList(amountOfKnots){Coordinate(0, 0)}
    val placesVisitedByTail = mutableSetOf(Coordinate(0,0))

    fun follow(instructions: String): LongRopeBridge {
        return instructions.lines()
            .map { it.words() }
            .map { (direction, nrOfSteps) -> direction.toDirection() to nrOfSteps.toInt() }
            .let { follow(it) }
    }

    private fun follow(instructions: List<Pair<Direction, Int>>): LongRopeBridge {
        instructions
            .forEach { (direction, nrOfSteps) ->
                moveRope(direction, nrOfSteps)
            }

        return this
    }

    fun countPlacesTailVisited(): Int {
        return placesVisitedByTail.count()
    }

    private fun moveRope(direction: Direction, nrOfSteps: Int) {
        repeat(nrOfSteps) {
            var knotAheadPreviousPosition = knotPositions[0]
            knotPositions[0] = knotPositions[0].move(direction)

            for (knot in (1.until(amountOfKnots))) {
                val currentKnotsStartingPosition = knotPositions[knot]

                when {
                    knotAheadMovedDiagonally(knot) -> {
                        knotPositions[knot] = knotAheadPreviousPosition
                    }
                    knotAheadMovedVertically(knot) -> {
                        knotPositions[knot] = Coordinate(knotAheadPreviousPosition.x, knotPositions[knot - 1].y)

                    }
                    knotAheadMovedHorizontally(knot) -> {
                        knotPositions[knot] = Coordinate(knotPositions[knot - 1].x, knotAheadPreviousPosition.y)

                    }

                    else -> {}// do nothing, previous knot has not moved enough to pull this one
                }
                knotAheadPreviousPosition = currentKnotsStartingPosition
            }

            placesVisitedByTail += knotPositions[amountOfKnots - 1]
        }
    }

    private fun knotAheadMovedDiagonally(knot: Int) = knotAheadMovedVertically(knot) && knotAheadMovedHorizontally(knot)

    private fun knotAheadMovedHorizontally(knot: Int) = abs(knotPositions[knot - 1].y - knotPositions[knot].y) > 1

    private fun knotAheadMovedVertically(knot: Int) = abs(knotPositions[knot - 1].x - knotPositions[knot].x) > 1

}

private fun String.toDirection(): Direction {
    return when(this) {
        "R" -> Direction.EAST
        "L" -> Direction.WEST
        "U" -> Direction.NORTH
        "D" -> Direction.SOUTH
        else -> error("unknown direction $this")
    }
}

private fun String.words(): List<String> {
    return split(' ')
}