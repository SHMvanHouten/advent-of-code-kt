package com.github.shmvanhouten.adventofcode2021.day22

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Cuboid(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    val size: Long by lazy { xRange.length.toLong() * yRange.length * zRange.length }
    fun isContainedBy(
        other: Cuboid
    ) = xRange.isContainedBy(other.xRange) &&
            yRange.isContainedBy(other.yRange) &&
            zRange.isContainedBy(other.zRange)

    fun sharesCoordinatesWith(other: Cuboid): Boolean {
        return overlap(xRange, other.xRange)
                && overlap(yRange, other.yRange)
                && overlap(zRange, other.zRange)
    }

    private fun overlap(oneRange: IntRange, otherRange: IntRange): Boolean {
        return oneRange.first in otherRange || oneRange.last in otherRange
                || otherRange.first in oneRange || otherRange.last in oneRange
    }

    // this is larger than the other in negative direction: - x
    // this is smaller than the other in negative direction: + x
    // this is larger than the other in positive direction: - x
    // this is smaller than the other in positive direction: + x
    fun differencesInDirections(other: Cuboid): Map<Direction, Int> {
        return this.zipRanges(other)
            .mapIndexed { i, rangePair ->
                val (onePair, otherPair) = rangePair
                listOf(
                    Direction.values()[i * 2] to otherPair.first - onePair.first,
                    Direction.values()[1 + i * 2] to otherPair.last - onePair.last,
                )
            }
            .flatten()
            .toMap()
    }

    private fun zipRanges(other: Cuboid): List<Pair<IntRange, IntRange>> {
        return listOf(
            this.xRange to other.xRange,
            this.yRange to other.yRange,
            this.zRange to other.zRange
        )
    }

    fun minus(cuboidToDetract: Cuboid): List<Cuboid> {
        val cuboidAbove = if(this.hasCubesAbove(cuboidToDetract)) this.copy(
            yRange = this.yRange.first.until(cuboidToDetract.yRange.first)
        ) else null

        val cuboidBelow = if(this.hasCubesBelow(cuboidToDetract)) this.copy(
            yRange = (cuboidToDetract.yRange.last + 1)..this.yRange.last
        ) else null

        val cuboidBehind = if(this.hasCubesBehind(cuboidToDetract)) this.copy(
            zRange = this.zRange.first.until(cuboidToDetract.zRange.first)
        ).minus(cuboidAbove).minus(cuboidBelow) else null

        val cuboidInFront = if(this.hasCubesInFront(cuboidToDetract)) this.copy(
            zRange = (cuboidToDetract.zRange.last + 1)..this.zRange.last
        ).minus(cuboidAbove).minus(cuboidBelow) else null

        val cuboidToTheLeft = if(this.hasCubesToTheLeft(cuboidToDetract)) this.copy(
            xRange = this.xRange.first.until(cuboidToDetract.xRange.first)
        ).minus(cuboidAbove).minus(cuboidBelow).minus(cuboidBehind).minus(cuboidInFront) else null

        val cuboidToTheRight = if(this.hasCubesToTheRight(cuboidToDetract)) this.copy(
            xRange = (cuboidToDetract.xRange.last + 1)..this.xRange.last
        ).minus(cuboidAbove).minus(cuboidBelow).minus(cuboidBehind).minus(cuboidInFront) else null

        return listOfNotNull(
            cuboidAbove,
            cuboidBelow,
            cuboidBehind,
            cuboidInFront,
            cuboidToTheLeft,
            cuboidToTheRight
        )
    }

    private fun minus(cuboidToDetract: Cuboid?): Cuboid {
        return if(cuboidToDetract != null) this.minus(cuboidToDetract).single()
        else this
    }

    private fun hasCubesToTheLeft(other: Cuboid): Boolean {
        return this.xRange.first < other.xRange.first
    }

    private fun hasCubesToTheRight(other: Cuboid): Boolean {
        return this.xRange.last > other.xRange.last
    }

    private fun hasCubesBehind(other: Cuboid): Boolean {
        return this.zRange.first < other.zRange.first
    }

    private fun hasCubesInFront(other: Cuboid): Boolean {
        return this.zRange.last > other.zRange.last
    }

    private fun hasCubesAbove(other: Cuboid): Boolean {
        return this.yRange.first < other.yRange.first
    }

    private fun hasCubesBelow(other: Cuboid): Boolean {
        return this.yRange.last > other.yRange.last
    }

}

fun List<Cuboid>.minus(cubeToDetract: Cuboid): List<Cuboid> {
    val first = this.first()
    return if (first.isContainedBy(cubeToDetract)) emptyList() // not necessary but faster
    else if (first.sharesCoordinatesWith(cubeToDetract)) first.minus(cubeToDetract)
    else listOf(first)
}

fun List<Cuboid>.add(rangeToAdd: Cuboid): List<Cuboid> {
    val first = this.first()
    return if (first.isContainedBy(rangeToAdd)) listOf(rangeToAdd) // not necessary but faster
    else if (rangeToAdd.isContainedBy(first)) listOf(first) // not necessary but faster
    else if (first.sharesCoordinatesWith(rangeToAdd)) combineRanges(this.first(), rangeToAdd)
    else this + rangeToAdd
}

fun combineRanges(one: Cuboid, other: Cuboid): List<Cuboid> {

    return if(one.size >= other.size) other.minus(one) + one
    else one.minus(other) + other
}

operator fun IntRange.plus(other: IntRange): IntRange {
    return min(this.first, other.first)..max(this.last, other.last)
}

private val IntRange.length: Int
    get() {
        return abs(this.last - this.first) + 1 // /this.step??
    }

private fun IntRange.isContainedBy(maybeContaining: IntRange): Boolean {
    return maybeContaining.first <= this.first && maybeContaining.last >= this.last
}

private fun IntRange.overlapsEntirely(maybeOverlapping: IntRange): Boolean {
    return this.first < maybeOverlapping.first && this.last > maybeOverlapping.last
}

enum class Direction {
    X_NEG,
    X_POS,
    Y_NEG,
    Y_POS,
    Z_NEG,
    Z_POS
}