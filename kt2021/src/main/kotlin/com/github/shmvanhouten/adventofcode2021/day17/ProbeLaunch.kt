package com.github.shmvanhouten.adventofcode2021.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.negate
import kotlin.math.sqrt

fun filterVelocitiesThatEndUpInRange(
    minToMaxX: IntRange,
    minToMaxY: IntRange,
    xRange: IntRange,
    yRange: IntRange
): List<Coordinate> {
    return minToMaxY.flatMap { y -> minToMaxX.map { x -> Coordinate(x, y) } }
        .filter { it.endsUpInRange(xRange, yRange) }
}

private fun Coordinate.endsUpInRange(xRange: IntRange, yRange: IntRange): Boolean {
    return generateSequence(Coordinate(0, 0) to this) { (pos, velocity) ->
        pos + velocity to velocity.decrease()
    }
        .map { it.first }
        .takeWhile { it.x <= xRange.last && it.y >= yRange.first }
        .any { it.x in xRange && it.y in yRange }
}

fun calculateMaxHeight(velocity: Int): Int {
    return ((velocity * (velocity + 1))/2)
}

fun calculateMinAndMaxYVelocityPossible(yRange: IntRange): Pair<Int, Int> {
    return yRange.first to
            yRange.first.negate() - 1
}

private fun Coordinate.decrease(): Coordinate {
    val newX = if (x <= 0) 0
    else x - 1
    return Coordinate(newX, y - 1)
}

fun calculateMinXVelocityAndMaxXVelocity(minX: Int, maxX: Int): Pair<Int, Int> {
    return calculateMinXVelocity(minX) to maxX
}

private fun calculateMinXVelocity(minX: Int): Int {
    return (sqrt(minX * 2.0)).toInt()
}
