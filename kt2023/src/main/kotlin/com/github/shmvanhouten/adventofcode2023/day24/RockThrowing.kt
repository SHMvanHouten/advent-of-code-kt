package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.compositenumber.greatestCommonDivisor
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.math.BigInteger

fun main() {
    println(readFile("/input-day24.txt"))
}

fun solve(map: List<HailStone3d>): BigCoordinate3d {
    val range = (-306..306).filter { it != 0 }.map { it.toBigDecimal() }
    return BigCoordinate3d(ONE, ONE, ONE)
//    return  range.asSequence().flatMap { z ->
//        range.asSequence().flatMap { y ->
//            range.asSequence().mapNotNull { x ->
//                map.intersectAtGivenVelocityDifference(BigCoordinate3d(x, y, z))?.let { BigCoordinate3d(x, y, z) to it }
//            }.filter { (first, it) ->
//                it.x.toString().substringAfter(".").all { it == '0' }
//                        && it.y.toString().substringAfter(".").all { it == '0' }
//                        && it.z.toString().substringAfter(".").all { it == '0' }
//            }
//                .onEach { println(it) }
//                .map { it.second }
//        }
//    }.first()
}

fun solve(stones: List<HailStone2d>): Sequence<BigCoordinate> {
    val range = (-1000..1000).filter { it != 0 }.map { it.toBigDecimal() }
    return range.asSequence().flatMap { y ->
            range.asSequence().mapNotNull { x ->
                val velocity = BigCoordinate(x, y)
                stones.intersectAtGivenVelocityDifference(velocity)?.let { it to velocity }
            }.filter { (loc, _) ->
                loc.x.toString().substringAfter(".").all { it == '0' }
                        && loc.y.toString().substringAfter(".").all { it == '0' }
            }.filter { (loc, vel) ->
                val hailStone2d = HailStone2d(loc, vel)
                if(vel == BigCoordinate((-3).toBigDecimal(), (1).toBigDecimal())) {
                    println("stop")
                }
                val crossPoint = stones.first().crossPoint(hailStone2d)
                if(crossPoint == null) false
                else stones.count {
                    val crossPoint1 = it.crossPoint(hailStone2d)
                    if(crossPoint1 == null) false
                    else crossPoint1.y.toString().substringAfter(".").all { it == '0' }&&crossPoint1.x.toString().substringAfter(".").all { it == '0' }
                } > 10
            }
//                .onEach { println(it) }
                .map { it.second }

    }
}

fun List<HailStone2d>.intersectAtGivenVelocityDifference(velocity: BigCoordinate): BigCoordinate? {
    val adjustedStones = this.map { it.copy(velocity = it.velocity - velocity) }
    return adjustedStones.first().crossPoint(adjustedStones[1])
}

fun List<Rock>.intersectAtGivenVelocityDifference(velocity: Coordinate3): Coordinate3 {
    val adjustedForVelocity = this.map { it.copy(velocity = it.velocity - velocity) }
    val coordinateThatStartsOnSameDimensionAsTarget = adjustedForVelocity
        .first { it.velocity.x == BigInteger.ZERO || it.velocity.y == BigInteger.ZERO || it.velocity.z == BigInteger.ZERO }
    if(coordinateThatStartsOnSameDimensionAsTarget.velocity.x == BigInteger.ZERO) {
        val x = coordinateThatStartsOnSameDimensionAsTarget.loc.x
        val otherRock = (adjustedForVelocity - coordinateThatStartsOnSameDimensionAsTarget).first()
        val steps = stepsToGetTo(x, otherRock.loc.x, otherRock.velocity.x)
        return otherRock.loc + (otherRock.velocity * steps)
    }
    else if(coordinateThatStartsOnSameDimensionAsTarget.velocity.y == BigInteger.ZERO) {
        val y = coordinateThatStartsOnSameDimensionAsTarget.loc.y
        val otherRock = (adjustedForVelocity - coordinateThatStartsOnSameDimensionAsTarget).first()
        val steps = stepsToGetTo(y, otherRock.loc.y, otherRock.velocity.y)
        return otherRock.loc + (otherRock.velocity * steps)
    }
    else if(coordinateThatStartsOnSameDimensionAsTarget.velocity.z == BigInteger.ZERO) {
        val z = coordinateThatStartsOnSameDimensionAsTarget.loc.z
        val otherRock = (adjustedForVelocity - coordinateThatStartsOnSameDimensionAsTarget).first()
        val steps = stepsToGetTo(z, otherRock.loc.z, otherRock.velocity.z)
        return otherRock.loc + (otherRock.velocity * steps)
    }

    return Coordinate3(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO)

}

fun stepsToGetTo(target: BigInteger, loc: BigInteger, vel: BigInteger): BigInteger {
    return (target - loc) / vel
}

private fun List<Pair<BigInteger, BigInteger>>.findIntersection(): BigInteger {
    // x1 = a + tb
    // x2 = c + td
    // find t where x1==x2
    // a + tb = c + td
    // tb−td=c−a
    // t(b−d)=c−a
    // t = (c-a)/(b-d)
    // possible points = any point where r1.loc

    println(this.filter { it.second < BigInteger.ZERO }.minOf { it.first })
    println(this.filter { it.second < BigInteger.ZERO }.maxOf { it.first })
    println(this.filter { it.second > BigInteger.ZERO }.minOf { it.first })
    println(this.filter { it.second > BigInteger.ZERO }.maxOf { it.first })
    return this.first().first
}


//
//fun <T> permuteCombinations(items: List<T>): List<Pair<T, T>> {
//    return items.flatMapIndexed() { index, item ->
//        items.subList(index, items.size).map { item to it }
//    }
//}

fun <T, R> mapCombinations(items:List<T>, transform: (T, T) -> R): List<R> {
    return items.flatMapIndexed() { index, item ->
        items.subList(index, items.size).map { transform(item, it) }
    }
}

fun solveByStoppingRock(stones: List<HailStone3d>) {
    /*
        if we reduce the velocity by the same amount for every dimension we can
        treat the problem as if our rock is standing still

        then we could say some things:
        given 2 rocks of the same dimension with the same velocity:
        for each prime, there needs to be a prime where it % prime is the same for both
        otherwise the two rocks would never intersect,
        which means that for our example, where we have
        {x = 19, vx = -2}, {x = 20, vx = -2} we know that vx must be reduced to either
        1 or -1, because otherwise the two numbers could never end up in the same place.
     */
    println(greatestCommonDivisor(listOf(19L, 20L)))
}


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

data class HailStone3d(val location: BigCoordinate3d, val velocity: BigCoordinate3d) {
    fun extrapolateZFrom(result2d: BigCoordinate): BigCoordinate3d {
        val xDiff = location.x - result2d.x
        val steps = xDiff / velocity.x
        val z = if((velocity.x * velocity.z).isNegative()) location.z - velocity.z * steps
        else location.z + velocity.z * steps
        return location.copy(x = result2d.x, y = result2d.y, z = z)
    }
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
        // todo: this cannot handle 1/3's, etc.
        if (velocity.x.setScale(0) == ZERO || other.velocity.x.setScale(0) == ZERO) {
            return null
        } else {

            if (velocity.y / velocity.x == other.velocity.y / other.velocity.x) return null

            val intersectionX =
                (other.location.y - other.velocity.y / other.velocity.x * other.location.x - (location.y - velocity.y / velocity.x * location.x)) / (velocity.y / velocity.x - other.velocity.y / other.velocity.x)
            val intersectionY =
                velocity.y / velocity.x * intersectionX + (location.y - velocity.y / velocity.x * location.x)
            return BigCoordinate(intersectionX, intersectionY)
        }
    }
}

private fun BigDecimal.isNegative(): Boolean {
    return this < 0L.toBigDecimal()
}

fun toHailStone2d(string: String): HailStone2d {
    val (loc, v) = string.split(" @ ")
    return HailStone2d(toCoordinate(loc), toCoordinate(v))
}

fun toHailStone3d(string: String): HailStone3d {
    val (loc, v) = string.split(" @ ")
    return HailStone3d(toCoordinate3d(loc), toCoordinate3d(v))
}

fun toCoordinate(input: String): BigCoordinate {
    val (x, y) = input.split(", ").map { it.trim() }
        .map { BigDecimal(it).setScale(4) }
    return BigCoordinate(x, y)
}


fun toCoordinate3d(input: String): BigCoordinate3d {
    val (x, y, z) = input.split(", ").map { it.trim() }
        .map { BigDecimal(it).setScale(4) }
    return BigCoordinate3d(x, y, z)
}

data class BigCoordinate(val x: BigDecimal, val y: BigDecimal) {
//    constructor(xBi: BigInteger, yBi: BigInteger): this(xBi.toBigDecimal(), yBi.toBigDecimal())

    operator fun minus(subtract: BigCoordinate): BigCoordinate {
        return BigCoordinate(x - subtract.x, y - subtract.y)
    }
}

data class BigCoordinate3d(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
    operator fun minus(subtract: BigCoordinate3d): BigCoordinate3d {
        return BigCoordinate3d(x - subtract.x, y - subtract.y, z - subtract.z)
    }

    operator fun plus(add: BigCoordinate3d): BigCoordinate3d {
        return BigCoordinate3d(x + add.x, y + add.y, z + add.z)
    }

    fun justxy(): BigCoordinate {
        return BigCoordinate(x, y)
    }
}
