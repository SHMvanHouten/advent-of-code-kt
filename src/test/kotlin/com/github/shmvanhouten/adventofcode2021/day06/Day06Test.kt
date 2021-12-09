package com.github.shmvanhouten.adventofcode2021.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day06Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `no new fishes are created if no fishes exist with 0 days left`() {
            val school = SchoolOfFish("4")
            assertThat(school.tick().totalFish(), equalTo(1L))

            val biggerSchool = SchoolOfFish("4,1")
            assertThat(biggerSchool.tick().totalFish(), equalTo(2L))
        }

        @Test
        internal fun `a new fish is created if a fish has 0 days left`() {
            val school = SchoolOfFish("0,4")
            assertThat(school.tick().totalFish(), equalTo(3L))
        }

        @Test
        internal fun `a new fish is created after 2 days if a fish has 1 day left`() {
            val school = SchoolOfFish("1,4")
            assertThat(school.tick(2).totalFish(), equalTo(3L))
        }

        @Test
        internal fun `a fertile fish and a fish with 7-fertility both become 6-fertilitycount fish`() {
            val school = SchoolOfFish("0,7")
            val after7Ticks = school.tick(7)
            // after 1 tick: 6,6,8
            // after 6 more ticks: 0,0,2
            assertThat(after7Ticks.totalFish(), equalTo(3L))
            // after 1 more tick: 5,5,1,7,7
            assertThat(after7Ticks.tick(1).totalFish(), equalTo(5L))
        }

        @Test
        internal fun `example 1`() {
            val school = SchoolOfFish(exampleInput)
            assertThat(school.tick(18).totalFish(), equalTo(26L))
            assertThat(school.tick(80).totalFish(), equalTo(5934L))
        }

        @Test
        internal fun `part 1`() {
            val school = SchoolOfFish(input)
            assertThat(school.tick(80).totalFish(), equalTo(380758L))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val school = SchoolOfFish(exampleInput)
            assertThat(school.tick(256).totalFish(), equalTo(26984457539L))
        }

        @Test
        internal fun `part 2`() {
            val school = SchoolOfFish(input)
            assertThat(school.tick(256).totalFish(), equalTo(1710623015163L))
        }
    }

    private val input by lazy { readFile("/input-day06.txt") }
    private val exampleInput = "3,4,3,1,2"

}
