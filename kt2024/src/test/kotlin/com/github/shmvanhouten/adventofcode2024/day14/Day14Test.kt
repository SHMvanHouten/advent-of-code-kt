package com.github.shmvanhouten.adventofcode2024.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day14Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val robots = example.lines().map { toRobot(it) }
            val moved = moveRobots(times = 100, width = 11, height = 7, robots = robots)
            expectThat(calculateSafetyFactor(moved, 11, 7)).isEqualTo(12)
        }

        @Test
        internal fun `part 1`() {
            val robots = input.lines().map { toRobot(it) }
            val moved = moveRobots(times = 100, width = 101, height = 103, robots = robots)
            expectThat(calculateSafetyFactor(moved, 101, 103)).isEqualTo(219150360)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            val robots = input.lines().map { toRobot(it) }
            expectThat(moveBotsUntilChristmas(robots, 101, 103)).isEqualTo(8053)
        }
    }

    private val input by lazy { readFile("/input-day14.txt")}
    private val example = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent()
}
