package com.github.shmvanhouten.adventofcode2022.day19

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun toBlueprints(input: String): List<Blueprint> {
    return input.lines().map { toBlueprint(it) }
}

fun toBlueprint(input: String): Blueprint {
    val words = input.words()
    val id = words[1].substringBefore(':').toInt()
    val oreRobot = OreRobotCosts(ore = words[6].toInt())
    val clayRobot = ClayRobotCosts(ore = words[12].toInt())
    val obsidianRobot = ObsidianRobotCosts(
        ore = words[18].toInt(),
        clay = words[21].toInt()
    )
    val geodeRobot = GeodeRobotCosts(
        ore = words[27].toInt(),
        obsidian = words[30].toInt(),
    )
    return Blueprint(id, oreRobot, clayRobot, obsidianRobot, geodeRobot)
}
