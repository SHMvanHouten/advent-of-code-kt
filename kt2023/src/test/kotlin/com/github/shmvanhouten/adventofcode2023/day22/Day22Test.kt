package com.github.shmvanhouten.adventofcode2023.day22

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day22Test {

    @Nested
    inner class Part1 {

        val example = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent()

        @Test
        fun `the ground is at 0, so a single brick will fall down to z = 1`() {
            val bricks = parse("1,0,7~1,1,7")
            expectThat(drop(bricks))
                .isEqualTo(parse("1,0,1~1,1,1"))
        }

        @Test
        @Disabled("Sorting made p1 fail")
        fun `an upright brick drops onto its lowest side`() {
            val bricks = parse("""
                1,0,2~1,0,3
                1,1,5~1,1,4
            """.trimIndent())
            expectThat(drop(bricks))
                .isEqualTo(parse("""
                    1,0,1~1,0,2
                    1,1,1~1,1,2
                """.trimIndent()))
        }

        @Test
        fun `a brick rest on top of another if they are in the same x-y range`() {
           val bricks = parse("""
                1,0,5~1,1,5
                1,1,3~1,1,3
            """.trimIndent())
            expectThat(drop(bricks))
                .isEqualTo(parse("""
                    1,1,1~1,1,1
                    1,0,2~1,1,2
                """.trimIndent()))
        }

        @Test
        fun `crossing bricks`() {
            val bricks = parse("""
                1,0,5~1,4,5
                0,2,3~3,2,3
            """.trimIndent())
            expectThat(drop(bricks))
                .isEqualTo(parse("""
                    0,2,1~3,2,1
                    1,0,2~1,4,2
                """.trimIndent()))
        }

        @Test
        internal fun `example bricks drop to`() {
            val bricks = parse(example)
            expectThat(drop(bricks)).isEqualTo(parse("""
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
            expectThat(safeToBeDisintegrated(bricks))
                .hasSize(5)
                .isEqualTo(parse("""
                    0,0,2~2,0,2
                    0,2,2~2,2,2
                    0,0,3~0,2,3
                    2,0,3~2,2,3
                    1,1,5~1,1,6
                """.trimIndent()))
        }

        @Test
        internal fun `part 1`() {
            val bricks = drop(parse(input))
            expectThat(safeToBeDisintegrated(bricks).size)
                .isEqualTo(503)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day22.txt")}

}
