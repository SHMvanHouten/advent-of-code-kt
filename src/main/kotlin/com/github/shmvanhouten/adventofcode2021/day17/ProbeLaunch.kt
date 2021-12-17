package com.github.shmvanhouten.adventofcode2021.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

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
    return generateSequence(Coordinate(0,0) to this) {(pos, velocity) ->
        pos + velocity to velocity.decrease()
    }
        .map { it.first }
        .takeWhile { it.x <= xRange.last && it.y >= yRange.first }
        .any { it.x in xRange && it.y in yRange }
}

fun calculateMaxHeight(velocity: Int): Int {
    return ((1..velocity).toList().median() * velocity).toInt()
}

fun calculateMaxAndMinYVelocityPossible(xRange: IntRange, yRange: IntRange): Pair<Int, Int> {
    return yRange.last.until(xRange.last).toList()
        .last { yVelocity ->
            wouldFallInRange(yVelocity, yRange)
        } to yRange.first.until(xRange.last).toList()
        .first { yVelocity ->
            wouldFallInRange(yVelocity, yRange)
        }
}

fun wouldFallInRange(yVelocity: Int, yRange: IntRange): Boolean {
    return generateSequence(0 to yVelocity) { (position, velocity) ->
        position + velocity to velocity - 1
    }
        .takeWhile { (pos, vel) ->
            pos >= yRange.first
        }
        .map {
            it.first
        }
        .last() in yRange
}

private fun Coordinate.decrease(): Coordinate {
    val newX = if(x <= 0) 0
    else x - 1
    return Coordinate(newX, y - 1)
}

fun calculateMinXVelocityAndMaxXVelocity(minX: Int, maxX: Int): Pair<Int, Int> {
    return calculateMinXVelocity(minX) to maxX
}

private fun calculateMinXVelocity(minX: Int) =
    generateSequence(0, Int::inc)
        .runningReduce(Int::plus)
        .mapIndexed{ i, x -> i to x}
        .first { (velocity, x) -> x >= minX }
        .first

fun List<Int>.median(): Float {
    val sorted = this.sorted()
    val i = sorted.size / 2
    return if (this.size % 2 == 0) {
        sorted[i - 1].toFloat() + 0.5f
    } else {
        sorted[i].toFloat()
    }
}