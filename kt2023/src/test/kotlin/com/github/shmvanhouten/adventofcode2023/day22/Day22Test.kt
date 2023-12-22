package com.github.shmvanhouten.adventofcode2023.day22

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3DProgression
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isLessThan

class Day22Test {

    private val example = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent()


    @Nested
    inner class Part1 {

        @Test
        fun `the ground is at 0, so a single brick will fall down to z = 1`() {
            val bricks = parse("1,0,7~1,1,7")
            expectThat(drop(bricks))
                .isEqualTo(
                    listOf(DroppedBrick(
                        0,
                        Coordinate3DProgression("1,0,1".coordinate3d(), "1,1,1".coordinate3d()),
                        emptyList()
                    ))
                )
        }

        @Test
        fun `an upright brick drops onto its lowest side`() {
            val bricks = parse("""
                1,0,2~1,0,3
                1,1,4~1,1,5
            """.trimIndent())
            expectThat(drop(bricks))
                .isEqualTo(listOf(
                    DroppedBrick(0, to3dRange("1,0,1~1,0,2"), emptyList()),
                    DroppedBrick(1, to3dRange("1,1,1~1,1,2"), emptyList())
                ))
        }

        @Test
        fun `a brick rest on top of another if they are in the same x-y range`() {
           val bricks = parse("""
                1,0,5~1,1,5
                1,1,3~1,1,3
            """.trimIndent())
            val dropped = drop(bricks)
            expectThat(dropped.map { it.toSimpleBrick() })
                .isEqualTo(parse("""
                    1,0,2~1,1,2
                    1,1,1~1,1,1
                """.trimIndent()).sortedBy { it.locations.start.z })
            expectThat(dropped.first().supportedBy).hasSize(0)
            expectThat(dropped[1].supportedBy).hasSize(1)
        }

        @Test
        fun `crossing bricks`() {
            val bricks = parse("""
                1,0,5~1,4,5
                0,2,3~3,2,3
            """.trimIndent())
            expectThat(drop(bricks).map { it.toSimpleBrick() })
                .isEqualTo(parse("""
                    1,0,2~1,4,2
                    0,2,1~3,2,1
                """.trimIndent()).sortedBy { it.locations.start.z })
        }

        @Test
        internal fun `example bricks drop to`() {
            val bricks = parse(example)
            val dropped = drop(bricks)
            expectThat(dropped.map { it.toSimpleBrick() }).isEqualTo(parse("""
                1,0,1~1,2,1
                0,0,2~2,0,2
                0,2,2~2,2,2
                0,0,3~0,2,3
                2,0,3~2,2,3
                0,1,4~2,1,4
                1,1,5~1,1,6
            """.trimIndent()))
        }

        @Test
        fun `example 1`() {
            val bricks = drop(parse(example))
            val safe = safeToBeDisintegrated(bricks)
            expect {
                that(safe).hasSize(5)
                that(safe.map { it.id }).isEqualTo(listOf(1,2,3,4,6))
            }
        }

        @Test
        internal fun `part 1`() {
            val bricks = drop(parse(input))
            expectThat(safeToBeDisintegrated(bricks).size)
                .isEqualTo(503)
        }
    }

    private fun to3dRange(s: String) = toBrick(s, -1).locations

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 1`() {
            expectThat(countTotalFalling(drop(parse(example)))).isEqualTo(7)
        }

        @Test
        internal fun `part 2`() {
            expectThat(countTotalFalling(drop(parse(input))))
                .isLessThan(113922)
                .isGreaterThan(51379)
                .isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day22.txt")}

}

private fun DroppedBrick.toSimpleBrick(): Brick {
    return Brick(this.id, this.locations)
}
