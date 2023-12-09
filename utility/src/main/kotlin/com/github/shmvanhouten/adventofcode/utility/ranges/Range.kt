package com.github.shmvanhouten.adventofcode.utility.ranges

import kotlin.math.max

fun IntRange.contains(other: IntRange): Boolean =
    this.first <= other.first
        && this.last >= other.last

operator fun LongRange.contains(other: LongRange): Boolean =
    this.first <= other.first
        && this.last >= other.last

/**
 * Returns
 * - ( empty or size 1) list of overlapping where this range and the other overlap
 * - (size 0 - 2) list of ranges where this range does _not_ overlap the other
 *
 * **ignores where the right range does not overlap this range!**
 */
fun LongRange.leftPartitionOverlapping(rightRange: LongRange) = when {
    this in rightRange -> OverlapPartition(overlapping = listOf(this))
    this.first in rightRange -> OverlapPartition(
        overlapping = this.first..rightRange.last,
        notOverlapping = rightRange.last + 1..this.last
    )
    this.last in rightRange -> OverlapPartition(
        overlapping = rightRange.first..this.last,
        notOverlapping = this.first..<rightRange.first
    )
    rightRange in this -> OverlapPartition(
        overlapping = listOf(rightRange),
        notOverlapping = listOf(this.first..<rightRange.first, rightRange.last + 1..this.last)
    )
    else -> OverlapPartition(notOverlapping = listOf(this))
}

fun LongRange.leftPartitionOverlapping(others: List<LongRange>): OverlapPartition {
    return others.fold(OverlapPartition(notOverlapping = listOf(this))) { acc, other ->
        val (overlapping, notOverlapping) = acc.notOverlapping.map { it.leftPartitionOverlapping(other) }.flatten()
        OverlapPartition(
            overlapping = acc.overlapping + overlapping,
            notOverlapping = notOverlapping
        )
    }
}

data class OverlapPartition(val overlapping: List<LongRange> = emptyList(), val notOverlapping: List<LongRange> = emptyList()) {

    constructor(overlapping: LongRange, notOverlapping: LongRange):
            this(listOf(overlapping), listOf(notOverlapping))
}

fun List<LongRange>.merge(): List<LongRange> =
    this.sortedBy { it.first }.mergeSorted()

private fun List<LongRange>.mergeSorted(): List<LongRange> {
    val resultingRanges = mutableListOf(this.first())
    this.drop(1).forEach { range ->
        val highestRange = resultingRanges.last()
        resultingRanges += if (range.first - 1 in highestRange) {
            resultingRanges.removeLast()
            highestRange.first..max(range.last, highestRange.last)
        } else {
            range
        }
    }
    return resultingRanges.toList()
}

private fun List<OverlapPartition>.flatten(): OverlapPartition {
    return OverlapPartition(
        this.flatMap { it.overlapping },
        this.flatMap { it.notOverlapping }
    )
}
