package com.github.shmvanhouten.adventofcode2023.day24

import java.math.BigDecimal

fun countIntersections(stones: List<HailStone2d>, min: BigDecimal, max: BigDecimal): Int {
    val range = min..max
    return stones.mapIndexed { index, stone ->
        stones.subList(index, stones.size)
            .mapNotNull { other ->
                stone.crosses(other)
            }
            .count { it.x in range && it.y in range }
    }.sum()
}

data class HailStone2d(val location: BigCoordinate, val velocity: BigCoordinate) {
    fun  crosses(other: HailStone2d): BigCoordinate? {
        val crossPoint = crossPoint(other)
        return if(crossPoint == null || !crossesInTheFuture(crossPoint, other)) null
        else crossPoint//.also { println("$this crosses ${other} at $it") }
    }

    private fun crossesInTheFuture(crossPoint: BigCoordinate, other: HailStone2d): Boolean {
        return crossPointIsInFuture(crossPoint) && other.crossPointIsInFuture(crossPoint)
    }

    private fun crossPointIsInFuture(crossPoint: BigCoordinate): Boolean {
        val xIsInFuture = if (velocity.x.isNegative()) crossPoint.x < location.x
        else crossPoint.x > location.x
        val yIsInFuture = if (velocity.y.isNegative()) crossPoint.y < location.y
        else crossPoint.y > location.y
        return xIsInFuture && yIsInFuture
    }

    fun crossPoint(other: HailStone2d): BigCoordinate? {
        // todo: either has velocity 0 in either direction
        val slopeThis = velocity.y / velocity.x
        val slopeOther = other.velocity.y / other.velocity.x

        if(slopeThis == slopeOther) return null

        val yInterceptThis = location.y - slopeThis * location.x
        val yInterceptOther = other.location.y - slopeOther * other.location.x

        val intersectionX = (yInterceptOther - yInterceptThis) / (slopeThis - slopeOther)
        val intersectionY = slopeThis * intersectionX + yInterceptThis
        return BigCoordinate(intersectionX, intersectionY)
    }
}

private fun BigDecimal.isNegative(): Boolean {
    return this < 0L.toBigDecimal()
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

data class BigCoordinate(val x: BigDecimal, val y: BigDecimal)
