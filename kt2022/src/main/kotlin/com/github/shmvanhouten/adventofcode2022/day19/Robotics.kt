package com.github.shmvanhouten.adventofcode2022.day19

data class Blueprint(
    val id: Int,
    val oreRobot: OreRobotCosts,
    val clayRobot: ClayRobotCosts,
    val obsidianRobot: ObsidianRobotCosts,
    val geodeRobot: GeodeRobotCosts
) {
    val minOreRequirement: Int by lazy {
        listOf(clayRobot, obsidianRobot, geodeRobot).maxOf { it.oreCost }
    }

}

sealed interface RobotCosts {
    val oreCost: Int
    val clayCost: Int
    val obsidianCost: Int
}

data class OreRobotCosts(
    override val oreCost: Int,
    override val clayCost: Int = 0,
    override val obsidianCost: Int = 0
    ) : RobotCosts

data class ClayRobotCosts(
    override val oreCost: Int,
    override val clayCost: Int = 0,
    override val obsidianCost: Int = 0
): RobotCosts

data class ObsidianRobotCosts(
    override val oreCost: Int,
    override val clayCost: Int,
    override val obsidianCost: Int = 0
): RobotCosts

data class GeodeRobotCosts(
    override val oreCost: Int,
    override val clayCost: Int = 0,
    override val obsidianCost: Int
): RobotCosts

