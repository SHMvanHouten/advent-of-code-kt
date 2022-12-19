package com.github.shmvanhouten.adventofcode2022.day19

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day19Test {

    private val example = """
Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent()
    @Nested
    inner class Part1 {

        @Test
        internal fun parse() {
            val blueprints = toBlueprints(example)
            assertThat(blueprints.size).isEqualTo(2)
            assertThat(blueprints.first()).isEqualTo(
                Blueprint(
                    id = 1,
                    OreRobotCosts(ore = 4),
                    ClayRobotCosts(ore = 2),
                    ObsidianRobotCosts(ore = 3, clay = 14),
                    GeodeRobotCosts(ore = 2, obsidian = 7)
                )
            )
        }

        @Test
        internal fun `the maximum amount of geodes blueprint 1 can produce is 9`() {
            val blueprints = toBlueprints(example)
            val result = bestGeodeProduction(blueprints.first())
            assertThat(result.inventory.geode).isEqualTo(9)
            assertThat(result.qualityLevel()).isEqualTo(9)
        }

        @Test
        internal fun `the maximum amount of geodes blueprint 2 can produce is 12`() {
            val blueprints = toBlueprints(example)
            val result = bestGeodeProduction(blueprints[1])
            assertThat(result.inventory.geode).isEqualTo(12)
            assertThat(result.qualityLevel()).isEqualTo(24)
        }

        @Test
        internal fun example() {
            val bluePrints = toBlueprints(example)
            assertThat(qualityLevelOf(bluePrints)).isEqualTo(33)
        }

        @Test
        internal fun `part 1`() {
            val bluePrints = toBlueprints(input)
            assertThat(qualityLevelOf(bluePrints)).isGreaterThan(1436)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            assertThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day19.txt")}

}
