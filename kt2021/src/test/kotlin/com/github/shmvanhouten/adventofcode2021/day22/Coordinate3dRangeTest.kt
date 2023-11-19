package com.github.shmvanhouten.adventofcode2021.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Nested
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
        val range = Cuboid(0..1, 0..1, 0..1)
        assertThat(
            listOf(range).add(range),
            equalTo(setOf(range))
        )
    }

    @Test
    internal fun `adding a coordinate range that overlaps another the other range returns a list of that first range`() {
        val smallerRange = Cuboid(0..1, 0..1, 0..1)
        val largerRange = Cuboid(-1..1, 0..1, 0..1)
        assertThat(
            listOf(smallerRange).add(largerRange),
            equalTo(setOf(largerRange))
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
        @AggregateWith(Coordinate3dRangePairAggregator::class) ranges: Pair<Cuboid, Cuboid>
    ) {
        val (smallerRange: Cuboid, largerRange: Cuboid) = ranges
        assertThat(
            setOf(largerRange).add(smallerRange),
            equalTo(setOf(largerRange))
        )
        assertThat(
            setOf(smallerRange).add(largerRange),
            equalTo(setOf(largerRange))
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
        @AggregateWith(Coordinate3dRangePairAggregator::class) ranges: Pair<Cuboid, Cuboid>
    ) {
        val (range1: Cuboid, range2: Cuboid) = ranges
        assertThat(
            listOf(range1).add(range2),
            equalTo(setOf(range1, range2))
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
        assertThat(combined.sumOf { it.size }, equalTo(6 * 2 * 1))
        assertThat(listOf(range2).add(range1).sumOf { it.size }, equalTo(12))
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
        val combined2 = listOf(range2).add(range1)
        assertThat(combined2.sumOf { it.size }, equalTo(19))
        assertThat(drawIn2D(combined), equalTo(drawIn2D(combined2)))
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
        assertThat(combined2.sumOf { it.size }, equalTo(17))
        val combined = listOf(range2).add(range1)
        assertThat(combined.sumOf { it.size }, equalTo(17))
        assertThat(drawIn2D(combined), equalTo(drawIn2D(combined2)))
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
        assertThat(combined2.sumOf { it.size }, equalTo(17))
        val combined = listOf(range2).add(range1)
        assertThat(combined.sumOf { it.size }, equalTo(17))
        assertThat(drawIn2D(combined), equalTo(drawIn2D(combined2)))
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
        assertThat(combined.sumOf { it.size }, equalTo(24))
        val combined2 = listOf(range2).add(range1)
        assertThat(combined2.sumOf { it.size }, equalTo(24))
        val drawn = drawIn2D(combined)
        println(drawn)
        assertThat(drawn, equalTo(drawIn2D(combined2)))
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
        assertThat(combined.sumOf { it.size }, equalTo(28))

        val combined2 = listOf(range2).add(range1)
        println(drawIn2D(combined2))
        assertThat(combined2.sumOf { it.size }, equalTo(28))

        assertThat(drawIn2D(combined), equalTo(drawIn2D(combined2)))
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
        assertThat(combined.sumOf { it.size }, equalTo(27))
        val combined2 = listOf(range2).add(range1)
        println()
        println(drawIn2D(combined2))
        assertThat(combined2.sumOf { it.size }, equalTo(27))

        assertThat(drawIn2D(combined), equalTo(drawIn2D(combined2)))
    }

    @Nested
    inner class DetractingRanges {

        @Test
        internal fun `if a cuboid is contained entirely by the cuboid that is detracted from it, the result is an empty list`() {
            val cuboid1 = Cuboid(
                1..5,
                1..5,
                1..5
            )
            val cuboidToDetract = Cuboid(
                0..6,
                0..6,
                0..6
            )
            assertThat(listOf(cuboid1).minus(cuboidToDetract), hasSize(equalTo(0)))
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "2..3, 0..1, 0..1, 0..1, 0..1, 0..1",
                "0..1, 0..1, 0..1, 0..1, 2..3, 0..1",
                "0..1, 0..1, -2..-1, 0..1, 0..1, 0..1",
            ]
        )
        internal fun `if the cuboid to detract does not overlap the original cuboid at all, the original cuboid is returned`(
            @AggregateWith(Coordinate3dRangePairAggregator::class) ranges: Pair<Cuboid, Cuboid>
        ) {
            val (cuboid1, cuboidToDetract) = ranges
            val result = listOf(cuboid1).minus(cuboidToDetract)
            assertThat(result, hasSize(equalTo(1)))
            assertThat(result.first(), equalTo(cuboid1))
        }

        @Test
        internal fun `if the cuboid to detract overlaps, the result is cuboids, before and after it in each direction`() {
            val cuboid1 = Cuboid(
                0..6,
                0..6,
                0..6
            )
            val cuboidToDetract = Cuboid(
                1..5,
                1..5,
                1..5
            )
            val result = listOf(cuboid1).minus(cuboidToDetract)
            assertThat(result.sumOf { it.size }, equalTo(cuboid1.size - cuboidToDetract.size))
            assertThat(result, hasSize(equalTo(6)))
            val expectedCuboidAbove = Cuboid(
                0..6,
                0..0,
                0..6
            )
            assertThat(result.filter { it == expectedCuboidAbove }, hasSize(equalTo(1)))
            val expectedCuboidBelow = Cuboid(
                0..6,
                6..6,
                0..6
            )
            assertThat(result.filter { it == expectedCuboidBelow }, hasSize(equalTo(1)))
            val expectedCuboidBehind = Cuboid(
                0..6,
                1..5,
                0..0
            )
            assertThat(result.filter { it == expectedCuboidBehind }, hasSize(equalTo(1)))
            val expectedCuboidInFront = Cuboid(
                0..6,
                1..5,
                6..6
            )
            assertThat(result.filter { it == expectedCuboidInFront }, hasSize(equalTo(1)))
            val expectedCuboidToTheLeft = Cuboid(
                0..0,
                1..5,
                1..5
            )
            assertThat(result.filter { it == expectedCuboidToTheLeft }, hasSize(equalTo(1)))
            val expectedCuboidToTheRight = Cuboid(
                6..6,
                1..5,
                1..5
            )
            assertThat(result.filter { it == expectedCuboidToTheRight }, hasSize(equalTo(1)))

        }

        @Test
        internal fun `partially overlapping`() {
            val cuboid1 = Cuboid(
                7..9,
                0..2,
                0..2
            )
            val cuboidToDetract = Cuboid(
                7..9,
                1..5,
                1..5
            )
            val result = listOf(cuboid1).minus(cuboidToDetract)
            assertThat(
                result.sumOf { it.size }, equalTo(
                    1 * 3 * 3 /* top sliver */ + 1 * 2 * 3 /* side sliver with top cut off */
                )
            )
        }

        @Test
        internal fun `detracting from multiple ranges`() {
            val cuboids = listOf(
                Cuboid(
                    0..6,
                    0..6,
                    0..6
                ),
                Cuboid(
                    7..9,
                    0..2,
                    0..2
                )
            )
            val cuboidToDetract = Cuboid(
                1..9,
                1..5,
                1..5
            )
            val result = cuboids.minus(cuboidToDetract)
            assertThat(
                result.sumOf { it.size }, equalTo(
                    (cuboids.first().size - cuboidToDetract.copy(xRange = 1..6).size) +
                            1 * 3 * 3 /* top sliver */ + 1 * 2 * 3 /* side sliver with top cut off */
                )
            )
        }

        @Test
        internal fun `adding to multiple ranges`() {
            val cuboids = listOf(
                Cuboid(
                    0..2,
                    0..2,
                    0..2
                ),
                Cuboid(
                    4..6,
                    4..6,
                    4..6
                )
            )
            val cuboidToAdd = Cuboid(
                2..4,
                2..4,
                2..4
            )
            val result = cuboids.add(cuboidToAdd)
            assertThat(
                result.sumOf { it.size },
                equalTo(cuboids.sumOf { it.size } + cuboidToAdd.size - 2 /* overlapping cubes*/)
            )
        }
    }

}

private fun drawIn2D(combined: Set<Cuboid>) =
    draw(
        runReboot(combined.map {
            RebootStep(
                Toggle.ON,
                it
            )
        }).map { Coordinate(it.x, it.y) },
        hit = '#',
        miss = ' '
    )

class Coordinate3dRangePairAggregator : ArgumentsAggregator {
    override fun aggregateArguments(
        accessor: ArgumentsAccessor?,
        context: ParameterContext?
    ): Pair<Cuboid, Cuboid> {
        val smallerRange = Cuboid(
            toIntRange(accessor?.getString(0)),
            toIntRange(accessor?.getString(1)),
            toIntRange(accessor?.getString(2))
        )
        val largerRange = Cuboid(
            toIntRange(accessor?.getString(3)),
            toIntRange(accessor?.getString(4)),
            toIntRange(accessor?.getString(5))
        )
        return smallerRange to largerRange
    }

    private fun toIntRange(raw: String?) =
        raw?.toRange() ?: error("Not enough arguments for Pair of Coordinate ranges")
}

private fun Set<Coordinate3d>.toCoordinate3dRange(): Cuboid {
    val xes = this.map { it.x }
    val xRange = xes.minOrNull()!!..xes.maxOrNull()!!
    val ys = this.map { it.y }
    val yRange = ys.minOrNull()!!..ys.maxOrNull()!!
    val zs = this.map { it.z }
    val zRange = zs.minOrNull()!!..zs.maxOrNull()!!
    return Cuboid(xRange, yRange, zRange)
}

private fun Set<Coordinate>.expandInThirdDimension(zRange: IntRange): Set<Coordinate3d> {
    return zRange.flatMap { z -> this.map { (x, y) -> Coordinate3d(x, y, z) } }.toSet()
}