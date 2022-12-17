package com.github.shmvanhouten.adventofcode2022.day16

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2022.day16.part1.findBestPath
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
        internal fun example() {
            val valves = parse(exampleInput)
            val bestPath = findBestPath(valves)
            println(bestPath.log)
            assertThat(bestPath.pressureReleased).isEqualTo(1651L)
        }

        @Test
        internal fun `part 1`() {
            val valves = parse(input)
            val bestPath = findBestPath(valves)
            println(bestPath.log)
            assertThat(bestPath.pressureReleased).isEqualTo(1906L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val valves = parse(exampleInput)
            val bestPath = findMostEfficientReleasePath(valves)
            println(bestPath.log)
            assertThat(bestPath.pressureReleased).isEqualTo(1707L)
        }

        @Test
        @Disabled("slow")
        internal fun `part 1`() {
            val valves = parse(input)
            val bestPath = findMostEfficientReleasePath(valves)
            assertThat(bestPath.pressureReleased).isEqualTo(2548L)
            println(bestPath.log)
//            1: turning on IJ with flow 16 at minute 4
//            2: turning on AW with flow 20 at minute 4
//            1: turning on ZH with flow 11 at minute 7
//            2: turning on FJ with flow 24 at minute 7
//            1: turning on RO with flow 5 at minute 10
//            2: turning on QN with flow 23 at minute 11
//            1: turning on FL with flow 18 at minute 14
//            2: turning on PY with flow 19 at minute 14
//            1: turning on EB with flow 8 at minute 17
//            1: turning on CQ with flow 17 at minute 21
//            1: turning on MK with flow 25 at minute 24
//            2: turning on FY with flow 15 at minute 25
        }
    }

    private val input by lazy { readFile("/input-day16.txt")}

}
