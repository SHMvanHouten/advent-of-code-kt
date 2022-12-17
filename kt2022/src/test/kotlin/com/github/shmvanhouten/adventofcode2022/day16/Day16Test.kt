package com.github.shmvanhouten.adventofcode2022.day16

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day16Test {

    private val exampleInput = """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `parse works`() {
            val valves = parse(exampleInput)
            val valveAA = valves["AA"]!!
            assertThat(valveAA.flowRate).isEqualTo(0)
            assertThat(valveAA.connectedTo).contains("DD", "II", "BB")
            assertThat(valves["BB"]!!.flowRate).isEqualTo(13)
            assertThat(valves.size).isEqualTo(exampleInput.lines().size)
        }

        @Test
        @Disabled("slow")
        internal fun example() {
            val valves = parse(exampleInput)
            val bestPath = findMostEfficientReleasePath(valves)
            assertThat(bestPath.pressureReleased).isEqualTo(1707L)
        }

        @Test
        @Disabled("slow")
        internal fun `part 1`() {
            val valves = parse(input)
            val bestPath = findMostEfficientReleasePath(valves)
            assertThat(bestPath.pressureReleased).isEqualTo(2548L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        @Disabled("slow")
        internal fun example() {
            val valves = parse(exampleInput)
            val bestPath = findMostEfficientReleasePath(valves)
            assertThat(bestPath.pressureReleased).isEqualTo(1707L)
        }

        @Test
        @Disabled("slow")
        internal fun `part 1`() {
            val valves = parse(input)
            val bestPath = findMostEfficientReleasePath(valves)
            assertThat(bestPath.pressureReleased).isEqualTo(2548L)
        }
    }

    private val input by lazy { readFile("/input-day16.txt")}

}
