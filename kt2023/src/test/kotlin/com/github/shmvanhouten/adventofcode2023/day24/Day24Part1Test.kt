package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.DescribeableBuilder
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import java.math.BigDecimal

class Day24Part1Test {

    private val example = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Test
    fun `1 and 3`() {
        val stones = """
                0, 0 @ 2, 2
                3, 2 @ 1, 2
            """.trimIndent().lines().map { toHailStone2d(it) }
        val result = stones.first().willCollide(stones[1])
        expectThat(result?.x?.toInt()).isEqualTo(4)
        expectThat(result?.y?.toInt()).isEqualTo(4)
    }

    @Test
    internal fun `hailstones will cross`() {
        val stones = """
                19, 13, 30 @ -2,  1, -2
                18, 19, 22 @ -1, -1, -2
                20, 25, 34 @ -2, -2, -4
            """.trimIndent().lines().map { toHailStone2d(it) }
        expect {
            that(stones.first().willCollide(stones[1])?.toFloatCoordinate())
                .isRoughlyEqual(FloatCoordinate(14.333f, 15.333f))
            that(stones.first().willCollide(stones[2])?.toFloatCoordinate())
                .isRoughlyEqual(FloatCoordinate(11.666f, 16.666f))
        }
    }

    @Test
    fun `hailstones crossed in the past`() {
        val stones = """
                19, 13, 30 @ -2, 1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
        expectThat(stones.first().crossPoint(stones[1])?.toFloatCoordinate())
            .isRoughlyEqual(FloatCoordinate(x = 21.444f, y = 11.777f))
    }

    @Test
    fun `since hailstones crossed in the past, they have no crosspoint`() {
        val stones = """
                19, 13, 30 @ -2, 1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
        expectThat(stones.first().willCollide(stones[1])?.toFloatCoordinate())
            .isNull()
    }

    @Test
    fun `another crossed in the past test`() {
        val stones = """
                18, 19, 22 @ -1, -1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
        expectThat(stones.first().willCollide(stones[1]))
            .isNull()
    }

    @Test
    fun `parallel paths`() {
        val stones = """
                18, 19, 22 @ -1, -1, -2
                20, 25, 34 @ -2, -2, -4
            """.trimIndent().lines().map { toHailStone2d(it) }
        expectThat(stones.first().willCollide(stones[1]))
            .isNull()
    }

    @Test
    @Disabled("It is not exact because of floating points and fractures like 1/3")
    fun `hailstone crosses`() {
        val stone = HailStone2d(
            location = BigCoordinate(x = 24.0000.toBigDecimal(), y = 13.0000.toBigDecimal()),
            velocity = BigCoordinate(x = (-3.000).toBigDecimal(), y = 1.0000.toBigDecimal())
        )
        val other = HailStone2d(
            location = BigCoordinate(x = 20.0000.toBigDecimal(), y = 19.0000.toBigDecimal()),
            velocity = BigCoordinate(x = (1.0000).toBigDecimal(), y = (-5.0000).toBigDecimal())
        )
        expectThat(stone.crossPoint(other)?.toFloatCoordinate())
            .isEqualTo(FloatCoordinate(21f, 14f))
    }

    @Test
    fun `example 1`() {
        val stones = example.lines().map { toHailStone2d(it) }
        expectThat(countIntersections(stones, 7L.toBigDecimal(), 27L.toBigDecimal()))
            .isEqualTo(2)
    }

    @Test
    internal fun `part 1`() {
        val stones = input.lines().map { toHailStone2d(it) }
        expectThat(countIntersections(stones, BigDecimal("200000000000000"), BigDecimal("400000000000000")))
            .isEqualTo(19523)
    }
}

private val input by lazy { readFile("/input-day24.txt") }


private fun DescribeableBuilder<FloatCoordinate?>.isRoughlyEqual(expected: FloatCoordinate) {
    expectThat(this.subject).isNotNull()
    compose("x and y are within 0.01 of each other") { subject ->
        get("%f") { subject!!.x }.isEqualTo(expected.x, 0.01)
        get("%f") { subject!!.y }.isEqualTo(expected.y, 0.01)
    } then {
        if (allPassed) pass() else fail()
    }
}

private fun BigCoordinate.toFloatCoordinate(): FloatCoordinate {
    return FloatCoordinate(x.toFloat(), y.toFloat())
}

data class FloatCoordinate(val x: Float, val y: Float)
