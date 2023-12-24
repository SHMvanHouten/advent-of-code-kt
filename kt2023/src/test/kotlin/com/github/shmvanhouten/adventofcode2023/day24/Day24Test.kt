package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.compositenumber.greatestCommonDivisor
import com.github.shmvanhouten.adventofcode.utility.compositenumber.leastCommonMultiple
import com.github.shmvanhouten.adventofcode.utility.compositenumber.primeFactors
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.DescribeableBuilder
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import java.math.BigDecimal

class Day24Test {


    @Nested
    inner class Part1 {

        @Test
        fun `1 and 3`() {
            val stones = """
                0, 0 @ 2, 2
                3, 2 @ 1, 2
            """.trimIndent().lines().map { toHailStone2d(it) }
            val result = stones.first().crosses(stones[1])
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
                that(stones.first().crosses(stones[1])?.toFloatCoordinate())
                    .isRouglyEqual(FloatCoordinate(14.333f, 15.333f))
                that(stones.first().crosses(stones[2])?.toFloatCoordinate())
                    .isRouglyEqual(FloatCoordinate(11.666f, 16.666f))
            }
        }

        @Test
        fun `hailstones crossed in the past`() {
            val stones = """
                19, 13, 30 @ -2, 1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
            expectThat(stones.first().crossPoint(stones[1])?.toFloatCoordinate())
                .isRouglyEqual(FloatCoordinate(x=21.444f, y=11.777f))
        }

        @Test
        fun `since hailstones crossed in the past, they have no crosspoint`() {
            val stones = """
                19, 13, 30 @ -2, 1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
            expectThat(stones.first().crosses(stones[1])?.toFloatCoordinate())
                .isNull()
        }

        @Test
        fun `another crossed in the past test`() {
            val stones = """
                18, 19, 22 @ -1, -1, -2
                20, 19, 15 @ 1, -5, -3
            """.trimIndent().lines().map { toHailStone2d(it) }
            expectThat(stones.first().crosses(stones[1]))
                .isNull()
        }

        @Test
        fun `parallel paths`() {
            val stones = """
                18, 19, 22 @ -1, -1, -2
                20, 25, 34 @ -2, -2, -4
            """.trimIndent().lines().map { toHailStone2d(it) }
            expectThat(stones.first().crosses(stones[1]))
                .isNull()
        }

        @Test
        fun `hailstone crosses`() {
            val stone = HailStone2d(location=BigCoordinate(x=24.0000.toBigDecimal(), y=13.0000.toBigDecimal()), velocity=BigCoordinate(x=(-3.000).toBigDecimal(), y=1.0000.toBigDecimal()))
            val other = HailStone2d(location=BigCoordinate(x=20.0000.toBigDecimal(), y=19.0000.toBigDecimal()), velocity=BigCoordinate(x=(1.0000).toBigDecimal(), y=(-5.0000).toBigDecimal()))
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

    private val answer = """
        24, 13, 10 @ -3, 1, 2""".trimIndent()

    private val example = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Nested
    inner class Part2 {

        @Test
        fun `trying some stuff`() {
            val resultStone = toHailStone3d(answer)
            val stones = example.lines().map { toHailStone3d(it) }
            stones.forEach {
                println("x: ${leastCommonMultiple(it.velocity.x.toBigInteger(), resultStone.velocity.x.toBigInteger())}" +
                        "y: ${leastCommonMultiple(it.velocity.y.toBigInteger(), resultStone.velocity.y.toBigInteger())}" +
                        "z: ${leastCommonMultiple(it.velocity.z.toBigInteger(), resultStone.velocity.z.toBigInteger())}")
            }
        }

        @Test
        internal fun `trying some other stuff`() {
            val lines = example.lines().map { line -> line.split(" @ ").map { part -> part.split(", ").map { it.trim() } } }
//                .map { it[1] }

            // step 1
//            lines
//                .map { it[0][1] to it[1][1] }
//                .groupBy { it.second }
//                .filter { it.value.size > 3 }
//                .onEach { println(it) }
//                .map { (key, value) -> mapCombinations(value.map { it.first.toBigInteger()}) { a, b -> (a - b).abs() } }
//                .onEach { println(it.greatestCommonDivisor()) }

            lines
                .map { it[0][1] to it[1][1] }
                .groupBy { it.second }
                .filter { it.value.size > 1 }
                .asSequence()
                .onEach { println(it) }
                .map { (key, value) -> mapCombinations(value.map { it.first.toBigInteger()}) { a, b -> (b - a) } }
//                .onEach { println(it) }
                .forEach { println(it.greatestCommonDivisor()) }

            println(primeFactors(318))
            println(primeFactors(227))
            println(primeFactors(332))
            println(primeFactors(644))

            val velocity = BigCoordinate3d((-44).toBigDecimal(), 305.toBigDecimal(), (75).toBigDecimal())


            // diff = 6
            // diff = 8
            // diff = 1
        }

        @Test
        fun `given we know the velocities of our stone, the location must be where all others intersect`() {
            val velocity = BigCoordinate3d((-3).toBigDecimal(), 1.toBigDecimal(), 2.toBigDecimal())
            expectThat(example.lines().map { toHailStone3d(it) }
                .intersectAtGivenVelocityDifference(velocity))
                .isEqualTo(BigCoordinate3d(24.toBigDecimal(), 13.toBigDecimal(), 10.toBigDecimal()))
        }

        @Test
        fun `given some values of x, and velocities of x`() {
            solveByStoppingRock(emptyList())
        }

        @Test
        fun `another idea`() {
            solve(input.lines().map { toHailStone2d(it) })
                .forEach { println(it) }
//            println(solve(input.lines().map { toHailStone3d(it) }))
        }

        @Test
        internal fun `part 2`() {
//            val range = (-1000..1000).filter { it != 0 }.map { it.toBigDecimal() }
//            range.flatMap { z ->
//                range.flatMap { y ->
//                    range.map { x ->
//                        BigCoordinate3d(x, y, z)
//                    }
//                }
//            }
//            range.forEach {
//                val velocity = BigCoordinate3d((-44).toBigDecimal(), it, (75).toBigDecimal())
//                println(input.lines().map { toHailStone3d(it) }
//                    .intersectAtGivenVelocityDifference(velocity))
//            }
            val velocity = BigCoordinate3d((-44).toBigDecimal(), (305).toBigDecimal(), (75).toBigDecimal())
            expectThat(input.lines().map { toHailStone3d(it) }
                .intersectAtGivenVelocityDifference(velocity))
                .isEqualTo(BigCoordinate3d(1.toBigDecimal(), 1.toBigDecimal(), 1.toBigDecimal()))
        }
    }

    private val input by lazy { readFile("/input-day24.txt")}

}

private fun DescribeableBuilder<FloatCoordinate?>.isRouglyEqual(expected: FloatCoordinate) {
    expectThat(this.subject).isNotNull()
    compose("x and y are within 0.01 of each other") {subject ->
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
