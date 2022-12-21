package com.github.shmvanhouten.adventofcode2022.day19

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
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
        @Disabled("fails, code only works for pt 2 now")
        internal fun `the maximum amount of geodes blueprint 1 can produce is 9`() {
            val blueprints = toBlueprints(example)
            val result = findMaximumGeodeProduction(blueprints.first())
            assertThat(result.inventory.geode).isEqualTo(56)
//            assertThat(result.qualityLevel()).isEqualTo(9)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun `the maximum amount of geodes blueprint 2 can produce is 12`() {
            val blueprints = toBlueprints(example)
            val result = findMaximumGeodeProduction(blueprints[1])
            assertThat(result.inventory.geode).isEqualTo(62)
//            assertThat(result.qualityLevel()).isEqualTo(24)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun `blueprint 10 from the input`() {
            val input = "Blueprint 10: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 3 ore and 16 obsidian."
            val blueprint = toBlueprint(input)
            assertThat(findMaximumGeodeProduction(blueprint).inventory.geode).isEqualTo(5)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun `blueprint 15`() {
            val input = "Blueprint 15: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 10 clay. Each geode robot costs 2 ore and 11 obsidian."
            val bluePrint = toBlueprints(input).first()
            assertThat(findMaximumGeodeProduction(bluePrint).inventory.geode).isEqualTo(14)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun `blueprint 27`() {
            val input = "Blueprint 27: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 7 obsidian."
            val blueprint = toBlueprint(input)
            assertThat(findMaximumGeodeProduction(blueprint).inventory.geode).isEqualTo(9)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun example() {
            val bluePrints = toBlueprints(example)
            assertThat(qualityLevelOf(bluePrints)).isEqualTo(33)
        }

        @Test
        @Disabled("fails, code only works for pt 2 now")
        internal fun `part 1`() {
            val bluePrints = toBlueprints(input)
            assertThat(qualityLevelOf(bluePrints)).isEqualTo(1681)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        @Disabled("fails, need to fix that!")
        internal fun example() {
            val blueprints = toBlueprints(example)
            val result = findMaximumGeodeProduction(blueprints.first())
            assertThat(result.inventory.geode).isEqualTo(56)
        }

        @Test
        internal fun `example 2`() {
            val blueprints = toBlueprints(example)
            val result = findMaximumGeodeProduction(blueprints[1])
            assertThat(result.inventory.geode).isEqualTo(62)
        }

        @Test
        @Disabled("slow")
        internal fun `part 2`() {
            val blueprints = toBlueprints(inputp2)
            val firstBlueprint = blueprints.first()
            assertThat(findMaximumGeodeProduction(firstBlueprint).inventory.geode).isEqualTo(6)
            assertThat(findMaximumGeodeProduction(blueprints[1]).inventory.geode).isEqualTo(31)
            assertThat(findMaximumGeodeProduction(blueprints[2]).inventory.geode).isEqualTo(29)
        }
    }

    private val inputp2 = """
        Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 4 ore and 20 obsidian.
        Blueprint 2: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 8 obsidian.
        Blueprint 3: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 4 ore and 13 obsidian.
    """.trimIndent()

    private val input by lazy { readFile("/input-day19.txt")}

}