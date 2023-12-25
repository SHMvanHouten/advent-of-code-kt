package com.github.shmvanhouten.adventofcode2023.day24

import java.math.BigInteger


fun findRockThatHitsEveryStone(input: String): Rock {
    val lines = input.lines()
    val rocks = lines
        .map { line ->toRock(line)}

    val velX = findLikelyVelocity(rocks.map { it.loc.x to it.velocity.x })
    val velY = findLikelyVelocity(rocks.map { it.loc.y to it.velocity.y })
    val velZ = findLikelyVelocity(rocks.map { it.loc.z to it.velocity.z })

    return rocks.first()
}

fun toRock(line: String): Rock {
    val (loc, vel) = line.split(" @ ")
    return Rock(loc.toCoordinate(), vel.toCoordinate())
}

private fun findLikelyVelocity(rocks: List<Pair<BigInteger, BigInteger>>) {
    val groups = rocks.groupBy { it.second }.filter { it.value.size > 1 }.values
    println(groups)
}



private fun String.toCoordinate(): Coordinate3 {
    val (x, y, z) = this.split(", ").map { it.trim() }
    return Coordinate3(x.toBigInteger(), y.toBigInteger(), z.toBigInteger())
}

data class Rock(val loc: Coordinate3, val velocity: Coordinate3)

data class Coordinate3(val x: BigInteger, val y: BigInteger, val z: BigInteger) {
    operator fun minus(subtractor: Coordinate3): Coordinate3 {
        return Coordinate3(x - subtractor.x, y - subtractor.y, z - subtractor.z)
    }

    operator fun plus(other: Coordinate3): Coordinate3 {
        return Coordinate3(x + other.x, y + other.y, z + other.z)
    }


    operator fun times(number: BigInteger): Coordinate3 {
        return Coordinate3(x * number, y * number, z * number)
    }
}