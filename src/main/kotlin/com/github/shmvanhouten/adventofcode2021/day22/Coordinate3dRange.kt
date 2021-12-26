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

    // this is larger than the other in negative direction: - x
    // this is smaller than the other in negative direction: + x
    // this is larger than the other in positive direction: - x
    // this is smaller than the other in positive direction: + x
    fun differencesInDirections(other: Coordinate3dRange): Map<Direction, Int> {
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

    private fun zipRanges(other: Coordinate3dRange): List<Pair<IntRange, IntRange>> {
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
    } else if(differencesInYRange(differencesPerDimension) && differencesInZRange(differencesPerDimension)) {
        // todo: > 3 ranges needed
        emptyList()
    } else if (differencesInYRange(differencesPerDimension)){
        if(differencesInDirection[Y_POS]!! != 0 && differencesInDirection[Y_NEG]!! != 0) {
            if (other.yRange.overlapsEntirely(one.yRange)) {
                rangesWhereOneYRangeOverlapsOtherEntirely(other, one)
            } else if(one.yRange.overlapsEntirely(other.yRange)) {
                rangesWhereOneYRangeOverlapsOtherEntirely(one, other)
            }else {
                touchingRanges(one, other)
            }
        } else if(differencesInDirection[Y_POS]!! < 0 || differencesInDirection[Y_NEG]!! > 0) {
            splitRangesForY(one, other)
        } else {
            splitRangesForY(other, one)
        }
    } else {
        // todo: Z
        emptyList()
    }
}

fun touchingRanges(one: Coordinate3dRange, other: Coordinate3dRange): List<Coordinate3dRange> {
    return if(one.xRange.last > other.xRange.last) oneBelowRightOther(one, other)
    else oneBelowRightOther(other, one)
}

/*
    .##
    .#X0
    ..00
 */
private fun oneBelowRightOther(
    one: Coordinate3dRange,
    other: Coordinate3dRange
): List<Coordinate3dRange> {
    return listOf(
        other,
        one.copy(
            xRange = one.xRange.after(other.xRange)
        ),
        one.copy(
            one.xRange.first..other.xRange.last,
            yRange = one.yRange.after(other.yRange)
        )
    )
}

/**
 * .#.    .#.    .#.
 * 0X0    .X0    0X.
 * .#.    .#.    .#.
 */
private fun rangesWhereOneYRangeOverlapsOtherEntirely(
    one: Coordinate3dRange,
    other: Coordinate3dRange
): List<Coordinate3dRange> {
    val ranges = mutableListOf(
        one,
        Coordinate3dRange(
            other.xRange.after(one.xRange),
            other.yRange,
            other.zRange
        )
    )
    if(other.xRange.overlapsEntirely(one.xRange)) {
        ranges.add(
            Coordinate3dRange(
                other.xRange.before(one.xRange),
                other.yRange,
                other.zRange
            )
        )
    }
    return ranges
}

/**
    ###
    00000
 */
private fun splitRangesForY(
    one: Coordinate3dRange,
    other: Coordinate3dRange
) = listOf(
    Coordinate3dRange(
        one.xRange + other.xRange,
        other.yRange,
        one.zRange
    ),
    Coordinate3dRange(
        one.xRange,
        one.yRange.after(other.yRange),
        one.zRange
    )
)

private fun differencesInZRange(differencesPerDimension: Map<Direction, Int>) =
    differencesPerDimension[Z_NEG]!! > 0

private fun differencesInYRange(differencesPerDimension: Map<Direction, Int>) =
    differencesPerDimension[Y_NEG]!! > 0

operator fun IntRange.plus(other: IntRange): IntRange {
    return min(this.first, other.first)..max(this.last, other.last)
}

fun IntRange.afterOrBefore(other: IntRange): IntRange {
    return if(other.contains(this.first)) after(other)
    else this.before(other)
}

private fun IntRange.after(other: IntRange) = (other.last + 1)..this.last

private fun IntRange.before(other: IntRange) = this.first until other.first

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