package com.github.shmvanhouten.adventofcode2021.day22

import com.github.shmvanhouten.adventofcode2021.day22.Direction.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Coordinate3dRange(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    val size: Long by lazy { xRange.length.toLong() * yRange.length * zRange.length }
    fun isContainedBy(
        other: Coordinate3dRange
    ) = xRange.isContainedBy(other.xRange) &&
            yRange.isContainedBy(other.yRange) &&
            zRange.isContainedBy(other.zRange)

    fun sharesCoordinatesWith(other: Coordinate3dRange): Boolean {
        return overlap(xRange, other.xRange)
                && overlap(yRange, other.yRange)
                && overlap(zRange, other.zRange)
    }

    private fun overlap(oneRange: IntRange, otherRange: IntRange): Boolean {
        return oneRange.first in otherRange || oneRange.last in otherRange
                || otherRange.first in oneRange || otherRange.last in oneRange
    }

    fun differencesInDirections(other: Coordinate3dRange): Map<Direction, Int> {
        return this.zip(other)
            .mapIndexed { i, rangePair ->
                val (onePair, otherPair) = rangePair
                listOf(
                    values()[i * 2] to otherPair.first - onePair.first,
                    values()[1 + i * 2] to otherPair.last - onePair.last,
                )
            }
            .flatten()
            .toMap()
    }

    private fun zip(other: Coordinate3dRange): List<Pair<IntRange, IntRange>> {
        return listOf(
            this.xRange to other.xRange,
            this.yRange to other.yRange,
            this.zRange to other.zRange
        )
    }

}

fun List<Coordinate3dRange>.add(rangeToAdd: Coordinate3dRange): List<Coordinate3dRange> {
    val first = this.first()
    return if (first.isContainedBy(rangeToAdd)) listOf(rangeToAdd)
    else if (rangeToAdd.isContainedBy(first)) listOf(first)
    else if (first.sharesCoordinatesWith(rangeToAdd)) combineRanges(this.first(), rangeToAdd)
    else this + rangeToAdd
}

fun combineRanges(one: Coordinate3dRange, other: Coordinate3dRange): List<Coordinate3dRange> {

    val differencesInDirection = one.differencesInDirections(other)
    val differencesPerDimension =
        differencesInDirection.entries.windowed(2, 2).associate { (n, m) -> n.key to abs(n.value) + abs(m.value) }
    return if(differencesPerDimension.values.count { it < 0 } <= 1 && differencesPerDimension.values.count{it > 0} <= 1) {
        val xRange = one.xRange + other.xRange
        val yRange = one.yRange + other.yRange
        val zRange = one.zRange + other.zRange
        listOf(Coordinate3dRange(xRange, yRange, zRange))
    } else if(differencesPerDimension[Y_NEG]!! > 0 && differencesPerDimension[Z_NEG]!! > 0) {
        // todo: > 3 ranges needed
        emptyList()
    } else if (differencesPerDimension[Y_NEG]!! > 0){
        listOf(
            Coordinate3dRange(
                one.xRange + other.xRange,
                other.yRange,
                one.zRange
            ),
            Coordinate3dRange(
                one.xRange,
                one.yRange.minus(other.yRange),
                one.zRange
            )
        )
    } else {
        // todo: Z
        emptyList()
    }
}

public operator fun IntRange.plus(other: IntRange): IntRange {
    return min(this.first, other.first)..max(this.last, other.last)
}

fun IntRange.minus(other: IntRange): IntRange {
    // for now going
    return min(this.last, other.last + 1)..this.last
}

private fun IntRange.extendBy(
    left: Int,
    right: Int
) =
    (this.first - left)..(this.last + right)

private val IntRange.length: Int
    get() {
        return abs(this.last - this.first) + 1 // /this.step??
    }

private fun IntRange.isContainedBy(maybeContaining: IntRange): Boolean {
    return maybeContaining.first <= this.first && maybeContaining.last >= this.last
}

enum class Direction {
    X_NEG,
    X_POS,
    Y_NEG,
    Y_POS,
    Z_NEG,
    Z_POS
}