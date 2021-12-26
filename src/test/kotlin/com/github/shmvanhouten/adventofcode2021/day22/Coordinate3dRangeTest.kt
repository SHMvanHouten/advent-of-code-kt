package com.github.shmvanhouten.adventofcode2021.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource

internal class Coordinate3dRangeTest {
    @Test
    internal fun `adding two of the same coordinate ranges together returns the same coordinate range`() {
        val range = Coordinate3dRange(0..1, 0..1, 0..1)
        assertThat(
            listOf(range).add(range),
            equalTo(listOf(range))
        )
    }

    @Test
    internal fun `adding a coordinate range that overlaps another the other range returns a list of that first range`() {
        val smallerRange = Coordinate3dRange(0..1, 0..1, 0..1)
        val largerRange = Coordinate3dRange(-1..1, 0..1, 0..1)
        assertThat(
            listOf(smallerRange).add(largerRange),
            equalTo(listOf(largerRange))
        )
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "0..1, 0..1, 0..1, -2..1, 0..1, 0..1",
            "0..1, 0..1, 0..1, 0..1, -1..1, 0..1",
            "0..1, 0..1, 0..1, 0..2, 0..1, 0..1",
            "0..1, 0..1, 0..1, 0..1, 0..1, 0..2",
        ]
    )
    internal fun `adding a coordinate range that is contained by a larger range returns that larger range`(
        @AggregateWith(Coordinate3dRangePairAggregator::class) ranges: Pair<Coordinate3dRange, Coordinate3dRange>
    ) {
        val (smallerRange: Coordinate3dRange, largerRange: Coordinate3dRange) = ranges
        assertThat(
            listOf(largerRange).add(smallerRange),
            equalTo(listOf(largerRange))
        )
        assertThat(
            listOf(smallerRange).add(largerRange),
            equalTo(listOf(largerRange))
        )
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "2..3, 0..1, 0..1, 0..1, 0..1, 0..1",
            "0..1, 0..1, 0..1, 0..1, 2..3, 0..1",
            "0..1, 0..1, -2..-1, 0..1, 0..1, 0..1",
        ]
    )
    internal fun `two not overlapping ranges are individually represented in the result`(
        @AggregateWith(Coordinate3dRangePairAggregator::class) ranges: Pair<Coordinate3dRange, Coordinate3dRange>
    ) {
        val (range1: Coordinate3dRange, range2: Coordinate3dRange) = ranges
        assertThat(
            listOf(range1).add(range2),
            equalTo(listOf(range1, range2))
        )
    }

    @Test
    internal fun `overlapping in one direction leads to one big range`() {
        val range1 = """
                .........
                ...#####.
                ...#####.
                .........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ..........
                ......###.
                ......###.
                ..........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        assertThat(range1.size, equalTo(5 * 2 * 1))
        assertThat(range2.size, equalTo(3 * 2 * 1))

        val combined = listOf(range1).add(range2)
        assertThat(combined, hasSize(equalTo(1)))
        assertThat(combined.first().size, equalTo(6 * 2 * 1))
        assertThat(listOf(range2).add(range1).first().size, equalTo(12))
    }

    @Test
    internal fun `overlapping in one direction but only partially in another direction leads to two ranges being created`() {
        val range1 = """
                ...........
                ...#####...
                ...#####...
                ...#####...
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                ......####.
                ......####.
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined = listOf(range1).add(range2)
        assertThat(combined, hasSize(equalTo(2)))
        assertThat(combined.first().size + combined[1].size, equalTo(19))
        val combined2 = listOf(range2).add(range1)
        assertThat(combined2, hasSize(equalTo(2)))
        assertThat(combined2.first().size + combined2[1].size, equalTo(19))
    }

    @Test
    internal fun `overlapping in the other direction also works`() {
        val range1 = """
                ...#####...
                ...#####...
                ...#####...
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                ...........
                ......####.
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined2 = listOf(range1).add(range2)
        assertThat(combined2, hasSize(equalTo(2)))
        assertThat(combined2.first().size + combined2[1].size, equalTo(17))
        val combined = listOf(range2).add(range1)
        assertThat(combined, hasSize(equalTo(2)))
        assertThat(combined.first().size + combined[1].size, equalTo(17))
    }


    @Test
    internal fun `overlapping but matching in other direction`() {
        val range1 = """
                ...#####...
                ...#####...
                ...#####...
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                ...........
                ...#######.
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined2 = listOf(range1).add(range2)
        assertThat(combined2, hasSize(equalTo(2)))
        assertThat(combined2.first().size + combined2[1].size, equalTo(17))
        val combined = listOf(range2).add(range1)
        assertThat(combined, hasSize(equalTo(2)))
        assertThat(combined.first().size + combined[1].size, equalTo(17))
    }

    @Test
    internal fun `overlapping in both directions`() {
        val range1 = """
                ...#####...
                ...#####...
                ...#####...
                ...#####...
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                ......####.
                ......####.
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined = listOf(range1).add(range2)
        assertThat(combined, hasSize(equalTo(2)))
        assertThat(combined.first().size + combined[1].size, equalTo(24))
        val combined2 = listOf(range2).add(range1)
        assertThat(combined2, hasSize(equalTo(2)))
        assertThat(combined2.first().size + combined2[1].size, equalTo(24))
    }

    @Test
    internal fun `makes a cross`() {
        val range1 = """
                ...#####...
                ...#####...
                ...#####...
                ...#####...
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                .#########.
                .#########.
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined = listOf(range1).add(range2)
        println(drawIn2D(combined))
        assertThat(combined, hasSize( equalTo(3)))
        assertThat(combined.sumOf { it.size }, equalTo(28))

        val combined2 = listOf(range2).add(range1)
        println(drawIn2D(combined2))
        assertThat(combined2, hasSize(equalTo(3)))
        assertThat(combined2.sumOf { it.size }, equalTo(28))
    }

    @Test
    internal fun `each overlaps the other in either direction`() {
        val range1 = """
                ...#####...
                ...#####...
                ...#####...
                ...........
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()
        val range2 = """
                ...........
                ......####.
                ......####.
                ......####.
                ......####.
            """.trimIndent().toCoordinateMap('#').expandInThirdDimension(0..0).toCoordinate3dRange()

        val combined = listOf(range1).add(range2)
        println(drawIn2D(combined))
        assertThat(combined, hasSize( equalTo(3)))
        assertThat(combined.sumOf { it.size }, equalTo(27))
        val combined2 = listOf(range2).add(range1)
        println(drawIn2D(combined2))
        assertThat(combined2, hasSize(equalTo(3)))
        assertThat(combined2.sumOf { it.size }, equalTo(27))
    }

}

private fun drawIn2D(combined2: List<Coordinate3dRange>) =
    draw(
        runReboot(combined2.map {
            RebootStep(
                Toggle.ON,
                it
            )
        }).map { Coordinate(it.x, it.y) },
        c = '#'
    )

class Coordinate3dRangePairAggregator : ArgumentsAggregator {
    override fun aggregateArguments(
        accessor: ArgumentsAccessor?,
        context: ParameterContext?
    ): Pair<Coordinate3dRange, Coordinate3dRange> {
        val smallerRange = Coordinate3dRange(
            toIntRange(accessor?.getString(0)),
            toIntRange(accessor?.getString(1)),
            toIntRange(accessor?.getString(2))
        )
        val largerRange = Coordinate3dRange(
            toIntRange(accessor?.getString(3)),
            toIntRange(accessor?.getString(4)),
            toIntRange(accessor?.getString(5))
        )
        return smallerRange to largerRange
    }

    private fun toIntRange(raw: String?) =
        raw?.toRange() ?: error("Not enough arguments for Pair of Coordinate ranges")
}

private fun Set<Coordinate3d>.toCoordinate3dRange(): Coordinate3dRange {
    val xes = this.map { it.x }
    val xRange = xes.minOrNull()!!..xes.maxOrNull()!!
    val ys = this.map { it.y }
    val yRange = ys.minOrNull()!!..ys.maxOrNull()!!
    val zs = this.map { it.z }
    val zRange = zs.minOrNull()!!..zs.maxOrNull()!!
    return Coordinate3dRange(xRange, yRange, zRange)
}

private fun Set<Coordinate>.expandInThirdDimension(zRange: IntRange): Set<Coordinate3d> {
    return zRange.flatMap { z -> this.map { (x, y) -> Coordinate3d(x, y, z) } }.toSet()
}