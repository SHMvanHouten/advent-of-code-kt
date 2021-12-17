package com.github.shmvanhouten.adventofcode2021.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

fun calculateMaxHeight(velocity: Int): Int {
    return ((1..velocity).toList().median() * velocity).toInt()
}

fun calculateMaxAndMinYVelocityPossible(xRange: IntRange, yRange: IntRange): Pair<Int, Int> {
    return yRange.last.until(xRange.last).toList()
        .last { yVelocity ->
            wouldFallInRange(yVelocity, yRange)
        } to yRange.last.until(xRange.last).toList()
        .first { yVelocity ->
            wouldFallInRange(yVelocity, yRange)
        }
}

fun wouldFallInRange(yVelocity: Int, yRange: IntRange): Boolean {
    val last = generateSequence(0 to yVelocity) { (position, velocity) ->
        position + velocity to velocity - 1
    }
        .takeWhile { (pos, vel) ->
//            println(pos to vel)
            pos >= yRange.first
        }
        .map {
            it.first
        }
        .last()
    val any = last in yRange
    println("$last is in range $yRange? $any")
    return any
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


//fun wouldReachTheTarget(velocity: Int, minX: Int, maxX: Int): Boolean {
//    return generateSequence(0, Int::inc).first { wouldReachTheTarget(it, minX, maxX) } to maxX
//    return generateSequence(0 to velocity) {(pos, vel) -> pos + vel to vel - 1}
//        .takeWhile { (pos, _) -> pos <= maxX }
//        .map { it.first }
//        .any { it in minX..maxX }
//}

fun List<Int>.median(): Float {
    val sorted = this.sorted()
    val i = sorted.size / 2
    return if (this.size % 2 == 0) {
        sorted[i - 1].toFloat() + 0.5f
    } else {
        sorted[i].toFloat()
    }
}