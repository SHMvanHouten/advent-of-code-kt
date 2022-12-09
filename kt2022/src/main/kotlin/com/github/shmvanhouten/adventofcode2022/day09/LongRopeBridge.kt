package com.github.shmvanhouten.adventofcode2022.day09

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw

class LongRopeBridge {
    private var knotPositions = (0..9).map { it to Coordinate(0,0) }.toMap().toMutableMap()
    val placesVisitedByTail = mutableSetOf(Coordinate(0,0))

    fun follow(instructions: String): LongRopeBridge {
        return follow(instructions.lines())
    }

    private fun follow(instructions: List<String>): LongRopeBridge {
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
            var oldKnotAheadPosition = knotPositions[0]!!
            println(draw(knotPositions.values, '#').replace(' ', '.'))
            println()
            knotPositions[0] = knotPositions[0]!!.move(direction) // move head knot

            for (knot in (1..9)) {
                val (oldX, oldY) = oldKnotAheadPosition
                oldKnotAheadPosition = knotPositions[knot]!!
                if(isMoreThanOneToTheSide(knot) && isMorThan1AboveOrBelow(knot)) {
                    knotPositions[knot] = Coordinate(oldX, oldY)
                }
                else if (isMoreThanOneToTheSide(knot)) {
                    knotPositions[knot] = Coordinate(oldX, knotPositions[knot - 1]!!.y)

                } else if (isMorThan1AboveOrBelow(knot)) {
                    knotPositions[knot] = Coordinate(knotPositions[knot - 1]!!.x, oldY)

                } else {
                    // do nothing
                }
            }

            placesVisitedByTail += knotPositions.getValue(9)
        }
    }

    private fun isMorThan1AboveOrBelow(knot: Int) = Math.abs(knotPositions[knot - 1]!!.y - knotPositions[knot]!!.y) > 1

    private fun isMoreThanOneToTheSide(knot: Int) = Math.abs(knotPositions[knot - 1]!!.x - knotPositions[knot]!!.x) > 1

}