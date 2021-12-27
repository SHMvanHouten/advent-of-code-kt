package com.github.shmvanhouten.adventofcode2021.day25

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.SOUTH

fun countStepsUntilMigrationHasStopped(fieldOfSeaCucumbers: FieldOfSeaCucumbers): Int {
    return generateSequence(fieldOfSeaCucumbers) { field ->
//        println(field.draw())
//        println()
        field.step()
    }.windowed(2)
        .takeWhile { (old, evolved) -> old != evolved }
        .count() + 1
}

data class FieldOfSeaCucumbers(
    val eastFacing: Set<Coordinate>,
    val southFacing: Set<Coordinate>,
    private val dimensions: Square
) {
    fun step(): FieldOfSeaCucumbers {
        val steppedEastFacing = eastFacing.map { it.stepEastIfPossible(eastFacing, southFacing, dimensions) }.toSet()
        val steppedSouthFacing = southFacing.map { it.stepSouthIfPossible(steppedEastFacing, southFacing, dimensions) }.toSet()
        return this.copy(eastFacing = steppedEastFacing, southFacing = steppedSouthFacing)
    }

    fun draw(): String {
        return dimensions.yRange.map { y ->
            dimensions.xRange.map { x ->
                toChar(Coordinate(x, y))
            }.joinToString("")
        }.joinToString("\n")
    }

    private fun toChar(coordinate: Coordinate) =
        if (eastFacing.contains(coordinate)) '>'
        else if (southFacing.contains(coordinate)) 'v'
        else '.'

}

private fun Coordinate.stepSouthIfPossible(
    eastFacing: Set<Coordinate>,
    southFacing: Set<Coordinate>,
    dimensions: Square
): Coordinate {
    val neighbourToTheSouth = dimensions.getNeighbourToTheSouth(this)
    return if(eastFacing.contains(neighbourToTheSouth) || southFacing.contains(neighbourToTheSouth)) this
    else neighbourToTheSouth
}

private fun Coordinate.stepEastIfPossible(
    eastFacing: Set<Coordinate>,
    southFacing: Set<Coordinate>,
    dimensions: Square
): Coordinate {
    val neighbourToTheEast = dimensions.getNeighbourToTheEast(this)
    return if(eastFacing.contains(neighbourToTheEast) || southFacing.contains(neighbourToTheEast)) this
    else neighbourToTheEast
}

data class Square(val xRange: IntRange, val yRange: IntRange) {
    fun getNeighbourToTheEast(coordinate: Coordinate): Coordinate {
        val moved = coordinate.move(EAST)
        return if (isInRange(moved)) moved
        else coordinate.copy(x = xRange.first)
    }

    fun getNeighbourToTheSouth(coordinate: Coordinate): Coordinate {
        val moved = coordinate.move(SOUTH)
        return if (isInRange(moved)) moved
        else coordinate.copy(y = yRange.first)
    }

    private fun isInRange(coordinate: Coordinate): Boolean {
        return coordinate.isInBounds(this.xRange.first, this.xRange.last, this.yRange.first, this.yRange.last)
    }
}
