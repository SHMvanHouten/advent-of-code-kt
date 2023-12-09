package com.github.shmvanhouten.adventofcode.utility.ranges

import kotlin.math.min

fun IntRange.contains(other: IntRange): Boolean {
    return this.first <= other.first
            && this.last >= other.last
}

fun LongRange.overlaps(other: LongRange): Boolean =
    this.first in other || this.last in other || other.first in this || other.last in this

/**
 * Returns
 * - a ( empty or size 1) list of overlapping where this range and the other overlap
 * - a (size 0 - 2) list of ranges where this range does _not_ overlap the other
 *
 * **ignores where the other range does not overlap this range!**
 */
fun LongRange.splitOverlapsOn(other: LongRange): OverlapSplit {
    return if (!this.overlaps(other)) OverlapSplit(emptyList(), listOf(this))
    else {
        val notOverlapping = mutableListOf<LongRange>()
        val overlapping = mutableListOf<LongRange>()
        if (this.first < other.first) {
            notOverlapping += this.first.until(min(this.last + 1, other.first))
            overlapping += other.first..minOf(this.last, other.last)
            if (other.last <= this.last) {//this completely contains other
                notOverlapping += (other.last + 1)..this.last
            }
        } else {
            overlapping += this.first..min(other.last, this.last)
            if(this.last > other.last) {
                notOverlapping += (other.last + 1)..this.last
            }
        }
        OverlapSplit(overlapping.toList(), notOverlapping.toList())
    }
}

fun LongRange.splitOverlapsOnAll(others: List<LongRange>): OverlapSplit {
    return others.fold(OverlapSplit(emptyList(), listOf(this))) { acc, other ->
        val (overlapping, notOverlapping) = acc.notOverlapping.map { it.splitOverlapsOn(other) }.flatten()
        OverlapSplit(
            overlapping = acc.overlapping + overlapping,
            notOverlapping = notOverlapping
        )
    }
}

data class OverlapSplit(val overlapping: List<LongRange>, val notOverlapping: List<LongRange>)

private fun List<OverlapSplit>.flatten(): OverlapSplit {
    return OverlapSplit(
        this.flatMap { it.overlapping },
        this.flatMap { it.notOverlapping }
    )
}

fun emptyOverlapSplit() = OverlapSplit(emptyList(), emptyList())