package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collections.combineAllWith
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

fun main() {
    println(readFile("/input-day24.txt"))
}

fun countIntersections(stones: List<HailStone2d>, min: BigDecimal, max: BigDecimal): Int {
    val range = min..max
    return stones
        .combineAllWith(HailStone2d::willCollide)
        .filterNotNull()
        .count { it.x in range && it.y in range }
}

data class HailStone2d(val location: BigCoordinate, val velocity: BigCoordinate) {
    fun willCollide(other: HailStone2d): BigCoordinate? {
        val crossPoint = crossPoint(other)
        return if(crossPoint == null || !crossesInTheFuture(crossPoint, other)) null
        else crossPoint
    }

    /**
     * this cannot handle 1/3's, etc.
     */
    fun crossPoint(other: HailStone2d): BigCoordinate? {
        if (velocity.x.setScale(0) == ZERO || other.velocity.x.setScale(0) == ZERO) {
            return null
        } else {

            val thisSlope = velocity.y / velocity.x
            val otherSlope = other.velocity.y / other.velocity.x
            if (thisSlope == otherSlope) return null

            val intersectionX =
                (other.location.y - otherSlope * other.location.x - (location.y - thisSlope * location.x)) / (thisSlope - otherSlope)
            val intersectionY =
                thisSlope * intersectionX + (location.y - thisSlope * location.x)
            return BigCoordinate(intersectionX, intersectionY)
        }
    }

    private fun crossesInTheFuture(crossPoint: BigCoordinate, other: HailStone2d): Boolean {
        return crossPointIsInFuture(crossPoint) && other.crossPointIsInFuture(crossPoint)
    }

    private fun crossPointIsInFuture(crossPoint: BigCoordinate): Boolean {
        val xIsInFuture = (crossPoint.x - location.x).sign == velocity.x.sign
        val yIsInFuture = (crossPoint.y - location.y).sign == velocity.y.sign
        return xIsInFuture && yIsInFuture
    }
}

fun toHailStone2d(string: String): HailStone2d {
    val (loc, v) = string.split(" @ ")
    return HailStone2d(toCoordinate(loc), toCoordinate(v))
}

fun toCoordinate(input: String): BigCoordinate {
    val (x, y) = input.split(", ").map { it.trim() }
        .map { BigDecimal(it).setScale(4) }
    return BigCoordinate(x, y)
}

data class BigCoordinate(val x: BigDecimal, val y: BigDecimal) {

    operator fun minus(subtract: BigCoordinate): BigCoordinate {
        return BigCoordinate(x - subtract.x, y - subtract.y)
    }
}

private val BigDecimal.sign: Int
    get() {
        return when {
            this < ZERO -> -1
            this > ZERO -> 1
            else -> 0
        }
    }