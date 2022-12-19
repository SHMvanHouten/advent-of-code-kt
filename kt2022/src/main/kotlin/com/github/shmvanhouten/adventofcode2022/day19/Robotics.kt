package com.github.shmvanhouten.adventofcode2022.day19

private const val TIME_ALLOWED: Int = 24

data class Blueprint(
    val id: Int,
    val oreRobot: OreRobotCosts,
    val clayRobot: ClayRobotCosts,
    val obsidianRobot: ObsidianRobotCosts,
    val geodeRobot: GeodeRobotCosts
) {
    val minOreRequirement: Int by lazy {
        listOf(clayRobot, obsidianRobot, geodeRobot).maxOf { it.ore }
    }

    fun canProduce(i: Int): Boolean {
        // 1 geode would require 2 ore and 7 obsidian at day 24
        // 2 ore and 7 obsidian at day 24 would require 3 * 1 = 21 + 2 ore and 14 * 1 = 98 clay at day 23
        //     or
        // 14 clay would require 14 * 2 ore at day 22
        return false
    }
}

sealed interface RobotCosts {
    val ore: Int
    val clay: Int
    val obsidian: Int
}

data class OreRobotCosts(
    override val ore: Int,
    override val clay: Int = 0,
    override val obsidian: Int = 0
    ) : RobotCosts

data class ClayRobotCosts(
    override val ore: Int,
    override val clay: Int = 0,
    override val obsidian: Int = 0
): RobotCosts

data class ObsidianRobotCosts(
    override val ore: Int,
    override val clay: Int,
    override val obsidian: Int = 0
): RobotCosts

data class GeodeRobotCosts(
    override val ore: Int,
    override val clay: Int = 0,
    override val obsidian: Int
): RobotCosts

