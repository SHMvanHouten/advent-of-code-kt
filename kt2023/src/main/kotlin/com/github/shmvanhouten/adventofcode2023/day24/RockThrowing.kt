package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.collections.combineAllWith
import com.github.shmvanhouten.adventofcode.utility.compositenumber.greatestCommonDivisor
import java.math.BigInteger
import java.math.BigInteger.ZERO

fun findRockThatHitsAllHailstones(input: String): Rock {
    val hailStones = input.lines().map { line -> toRock(line) }
    val velocity = findVelocityForRock(hailStones)
    val location = hailStones.intersectAtGivenVelocityDifference(velocity)
    return Rock(location, velocity)
}

fun findVelocityForRock(input: String): Big3dCoordinate {
    val hailStones = input.lines()
        .map { line -> toRock(line) }

    return findVelocityForRock(hailStones)
}

private fun findVelocityForRock(rocks: List<Rock>): Big3dCoordinate {
    val velX = rocks.map { it.loc.x to it.velocity.x }.findVelocityDifferenceNecessaryToConverge()
    val velY = rocks.map { it.loc.y to it.velocity.y }.findVelocityDifferenceNecessaryToConverge()
    val velZ = rocks.map { it.loc.z to it.velocity.z }.findVelocityDifferenceNecessaryToConverge()

    return Big3dCoordinate(velX, velY, velZ)
}

fun List<Pair<BigInteger, BigInteger>>.findVelocityDifferenceNecessaryToConverge(): BigInteger {
    val maxAllowedNewVelocityForEachVelocity = this.groupBy { it.second }.mapValues { it.value.map { (loc, _) -> loc } }
        .filter { it.value.size > 1 }
        .map { (vel, positions) -> vel to positions.combineAllWith(BigInteger::minus) }
        .map { (vel, diffs) -> vel to diffs.greatestCommonDivisor() }
    return (1..1000).asSequence().map { it.toBigInteger() }.flatMap{ listOf(it, it.negate()) }
        .first { offset ->
            maxAllowedNewVelocityForEachVelocity.all { (oldVelocity, commonDiff) ->
                val newVelocity = oldVelocity - offset
                newVelocity != ZERO && commonDiff % newVelocity == ZERO
            }
    }
}

fun List<Rock>.intersectAtGivenVelocityDifference(velocity: Big3dCoordinate): Big3dCoordinate {
    val adjustedForVelocity = this.map { it.copy(velocity = it.velocity - velocity) }
    val stationaryCoordinateInOneDimension = adjustedForVelocity
        .first { it.velocity.x == ZERO || it.velocity.y == ZERO || it.velocity.z == ZERO }
    val otherRock = adjustedForVelocity.first {it != stationaryCoordinateInOneDimension}
    when {
        stationaryCoordinateInOneDimension.velocity.x == ZERO -> {
            val x = stationaryCoordinateInOneDimension.loc.x
            val steps = stepsToGetTo(x, otherRock.loc.x, otherRock.velocity.x)
            return otherRock.loc + (otherRock.velocity * steps)
        }
        stationaryCoordinateInOneDimension.velocity.y == ZERO -> {
            val y = stationaryCoordinateInOneDimension.loc.y
            val steps = stepsToGetTo(y, otherRock.loc.y, otherRock.velocity.y)
            return otherRock.loc + (otherRock.velocity * steps)
        }
        stationaryCoordinateInOneDimension.velocity.z == ZERO -> {
            val z = stationaryCoordinateInOneDimension.loc.z
            val steps = stepsToGetTo(z, otherRock.loc.z, otherRock.velocity.z)
            return otherRock.loc + (otherRock.velocity * steps)
        }
        else -> error("did not find location for velocity $velocity")
    }
}

fun toRock(line: String): Rock {
    val (loc, vel) = line.split(" @ ")
    return Rock(loc.toBig3dCoordinate(), vel.toBig3dCoordinate())
}

private fun String.toBig3dCoordinate(): Big3dCoordinate {
    val (x, y, z) = this.split(", ").map { it.trim() }
    return Big3dCoordinate(x.toBigInteger(), y.toBigInteger(), z.toBigInteger())
}

data class Rock(val loc: Big3dCoordinate, val velocity: Big3dCoordinate)

data class Big3dCoordinate(val x: BigInteger, val y: BigInteger, val z: BigInteger) {

    operator fun minus(subtractor: Big3dCoordinate): Big3dCoordinate {
        return Big3dCoordinate(x - subtractor.x, y - subtractor.y, z - subtractor.z)
    }

    operator fun plus(other: Big3dCoordinate): Big3dCoordinate {
        return Big3dCoordinate(x + other.x, y + other.y, z + other.z)
    }
    operator fun times(number: BigInteger): Big3dCoordinate {
        return Big3dCoordinate(x * number, y * number, z * number)
    }
}

private fun stepsToGetTo(target: BigInteger, loc: BigInteger, vel: BigInteger): BigInteger {
    check((target-loc) % vel == ZERO) {"Expected target - loc to be divisible by velocity, but instead remainder was ${(target-loc) % vel}"}
    return (target - loc) / vel
}