package com.github.shmvanhouten.adventofcode2022.day19

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun toBlueprints(input: String): List<Blueprint> {
    return input.lines().map { toBlueprint(it) }
}

fun toBlueprint(input: String): Blueprint {
    val words = input.words()
    val id = words[1].substringBefore(':').toInt()
    val oreRobot = OreRobotCosts(oreCost = words[6].toInt())
    val clayRobot = ClayRobotCosts(oreCost = words[12].toInt())
    val obsidianRobot = ObsidianRobotCosts(
        oreCost = words[18].toInt(),
        clayCost = words[21].toInt()
    )
    val geodeRobot = GeodeRobotCosts(
        oreCost = words[27].toInt(),
        obsidianCost = words[30].toInt(),
    )
    return Blueprint(id, oreRobot, clayRobot, obsidianRobot, geodeRobot)
}
