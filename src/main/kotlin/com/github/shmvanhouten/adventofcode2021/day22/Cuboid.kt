package com.github.shmvanhouten.adventofcode2021.day22

import kotlin.math.abs

data class Cuboid(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    val size: Long by lazy { xRange.length.toLong() * yRange.length * zRange.length }

    fun minus(cuboidToDetract: Cuboid): List<Cuboid> {
        if (cuboidToDetract.containsEntirely(this)) return emptyList() // not necessary but faster
        if (!this.sharesCoordinatesWith(cuboidToDetract)) return listOf(this)
        val cuboidAbove = if (this.hasCubesAbove(cuboidToDetract)) this.copy(
            yRange = this.yRange.first.until(cuboidToDetract.yRange.first)
        ) else null

        val cuboidBelow = if (this.hasCubesBelow(cuboidToDetract)) this.copy(
            yRange = (cuboidToDetract.yRange.last + 1)..this.yRange.last
        ) else null

        val cuboidBehind = if (this.hasCubesBehind(cuboidToDetract)) this.copy(
            zRange = this.zRange.first.until(cuboidToDetract.zRange.first)
        ).minus(cuboidAbove).minus(cuboidBelow) else null

        val cuboidInFront = if (this.hasCubesInFront(cuboidToDetract)) this.copy(
            zRange = (cuboidToDetract.zRange.last + 1)..this.zRange.last
        ).minus(cuboidAbove).minus(cuboidBelow) else null

        val cuboidToTheLeft = if (this.hasCubesToTheLeft(cuboidToDetract)) this.copy(
            xRange = this.xRange.first.until(cuboidToDetract.xRange.first)
        ).minus(cuboidAbove).minus(cuboidBelow).minus(cuboidBehind).minus(cuboidInFront) else null

        val cuboidToTheRight = if (this.hasCubesToTheRight(cuboidToDetract)) this.copy(
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

    fun containsEntirely(other: Cuboid) = other.xRange.isContainedBy(xRange) &&
            other.yRange.isContainedBy(yRange) &&
            other.zRange.isContainedBy(zRange)

    fun sharesCoordinatesWith(other: Cuboid): Boolean {
        return overlap(xRange, other.xRange)
                && overlap(yRange, other.yRange)
                && overlap(zRange, other.zRange)
    }

    private fun overlap(oneRange: IntRange, otherRange: IntRange): Boolean {
        return oneRange.first in otherRange || oneRange.last in otherRange
                || otherRange.first in oneRange || otherRange.last in oneRange
    }

}

fun Collection<Cuboid>.minus(cubeToDetract: Cuboid): Set<Cuboid> {
    return this.flatMap { it.minus(cubeToDetract) }.toSet()
}

fun Collection<Cuboid>.add(cuboidToAdd: Cuboid): Set<Cuboid> {
    if(this.any { it.containsEntirely(cuboidToAdd) }) return this.toSet()
    if(this.none { it.sharesCoordinatesWith(cuboidToAdd) }) return this.toSet() + cuboidToAdd

    val cuboidsThatAreContainedByNewCuboid = this.filter { cuboidToAdd.containsEntirely(it) }.toSet()
    if(cuboidsThatAreContainedByNewCuboid.isNotEmpty()) return (this - cuboidsThatAreContainedByNewCuboid).add(cuboidToAdd)

    val remainingCuboids = this.fold(setOf(cuboidToAdd)) { remaining, cuboid ->
        remaining.flatMap { it.minus(cuboid) }.toSet()
    }
    return remainingCuboids + this
}

private val IntRange.length: Int
    get() {
        return abs(this.last - this.first) + 1 // /this.step??
    }

private fun IntRange.isContainedBy(maybeContaining: IntRange): Boolean {
    return maybeContaining.first <= this.first && maybeContaining.last >= this.last
}
