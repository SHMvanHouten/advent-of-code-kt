package com.github.shmvanhouten.adventofcode2022.day19

import java.util.*

private var maxMinutes: Int = 32

fun qualityLevelOf(bluePrints: List<Blueprint>): Int {
    maxMinutes = 24
    return bluePrints.sumOf { findMaximumGeodeProduction(it).qualityLevel() }
}

fun findMaximumGeodeProduction(
    blueprint: Blueprint,
    start: Production = Production(blueprint.id),
    timeConstraint: Int = 24
): Production {
    maxMinutes = timeConstraint
    val productions = priorityQueueOf(start)
    var bestProduction: Production? = null
    while (productions.isNotEmpty()) {
        val production = productions.poll()
        if(production.inventory.geode < (bestProduction?.inventory?.geode?: 0)
            && (production.geodeRobots + (maxMinutes - production.minute) < (bestProduction?.geodeRobots?: 0))
        ) {
            continue
        }
        if (production.isFinished(blueprint)) {
            val finishedProduction = production.finish(blueprint)
            if (bestProduction == null || finishedProduction.inventory.geode > bestProduction.inventory.geode) {
                println("best production: ${production.inventory.geode}")
                bestProduction = finishedProduction
            }
        } else {
            productions += production.createAllPossibleProductionPermutations(blueprint)
        }

    }
    println("blueprint: ${blueprint.id} gave ${bestProduction?.inventory?.geode} geodes")
    return bestProduction ?: error("no production found")
}

data class Inventory(
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geode: Int = 0
) {
    fun canProduceOreBotSinceThisTurn(blueprint: Blueprint, oreRobots: Int): Boolean {
        return blueprint.oreRobot.oreCost <= ore
                && blueprint.oreRobot.oreCost > (ore - oreRobots)
    }

    fun canProduceClayBotSinceThisTurn(blueprint: Blueprint, oreRobots: Int): Boolean {
        return blueprint.clayRobot.oreCost <= ore
                && blueprint.clayRobot.oreCost >= (ore - oreRobots)
    }

    fun canProduceObsidianBotSinceThisTurn(blueprint: Blueprint, oreRobots: Int, clayRobots: Int): Boolean {
        return blueprint.obsidianRobot.oreCost <= ore && blueprint.obsidianRobot.clayCost <= clay
                && (blueprint.obsidianRobot.oreCost >= (ore - oreRobots) || blueprint.obsidianRobot.clayCost >= (clay - clayRobots - 3))
    }

    fun canProduceGeodeBot(blueprint: Blueprint): Boolean {
        return blueprint.geodeRobot.oreCost <= ore && blueprint.geodeRobot.obsidianCost <= obsidian
    }

    fun minus(botCosts: RobotCosts): Inventory {
        return this.copy(
            ore = ore - botCosts.oreCost,
            clay = clay - botCosts.clayCost,
            obsidian = obsidian - botCosts.obsidianCost
        )
    }

    fun receive(ores: Int, clays: Int, obsidians: Int, geodes: Int): Inventory {
        return Inventory(
            ore = ore + ores,
            clay = clay + clays,
            obsidian = obsidian + obsidians,
            geode = geode + geodes
        )
    }

}

data class Production(
    val id: Int,
    val minute: Int = 1,
    val inventory: Inventory = Inventory(),
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0
) {
    fun createAllPossibleProductionPermutations(blueprint: Blueprint): List<Production> {
        return this.permuteProductions(blueprint)
    }

    fun finish(blueprint: Blueprint): Production {
        return if(this.minute < maxMinutes && this.obsidianRobots >= blueprint.geodeRobot.obsidianCost) {
            generateSequence(this) {
                this.produce()
            }.first { it.minute == maxMinutes + 1 }
        } else produce()
    }

    private fun produce(
        inventory: Inventory = this.inventory,
        extraOreBot: Int = 0,
        extraClayBot: Int = 0,
        extraObsBot: Int = 0,
        extraGeoBot: Int = 0
    ): Production {
        return this.copy(
            minute = minute + 1,
            inventory = inventory.receive(this.oreRobots, this.clayRobots, this.obsidianRobots, this.geodeRobots),
            oreRobots = this.oreRobots + extraOreBot,
            clayRobots = this.clayRobots + extraClayBot,
            obsidianRobots = this.obsidianRobots + extraObsBot,
            geodeRobots = this.geodeRobots + extraGeoBot
        )
    }

    private fun permuteProductions(blueprint: Blueprint): List<Production> {
        val permutations = mutableListOf<Production>()
        if (inventory.canProduceGeodeBot(blueprint)) {
            permutations += this.produce(
                inventory = inventory.minus(blueprint.geodeRobot),
                extraGeoBot = 1
            )
        } else {
            if (blueprint.geodeRobot.obsidianCost > obsidianRobots && inventory.canProduceObsidianBotSinceThisTurn(blueprint, oreRobots, clayRobots) && minute < (maxMinutes - 2)) {
                permutations += this.produce(
                    inventory = inventory.minus(blueprint.obsidianRobot),
                    extraObsBot = 1
                )
            }
            if (blueprint.minOreRequirement > oreRobots && inventory.canProduceOreBotSinceThisTurn(blueprint, oreRobots) && minute < (maxMinutes - 12)) {
                permutations += this.produce(
                    inventory = inventory.minus(blueprint.oreRobot),
                    extraOreBot = 1
                )
            }
            if (blueprint.obsidianRobot.clayCost > clayRobots && inventory.canProduceClayBotSinceThisTurn(blueprint, oreRobots) && minute < (maxMinutes - 4)) {
                permutations += this.produce(
                    inventory = inventory.minus(blueprint.clayRobot),
                    extraClayBot = 1
                )
            }

            permutations += produce()
        }

        return permutations
    }

    fun isFinished(blueprint: Blueprint): Boolean {
        return this.minute == maxMinutes || this.obsidianRobots >= blueprint.geodeRobot.obsidianCost
    }

    fun qualityLevel(): Int {
        return id * inventory.geode
    }

}

fun priorityQueueOf(production: Production): PriorityQueue<Production> {
    val queue = PriorityQueue(ProductionComparator())
    queue.add(production)
    return queue
}

class ProductionComparator : Comparator<Production> {
    override fun compare(one: Production?, other: Production?): Int {
        if (one == null || other == null) error("null productions")
        val invOne = one.inventory
        val invOther = other.inventory
        if (invOne.geode != invOther.geode) return invOther.geode.compareTo(invOne.geode)
        else if (one.geodeRobots != other.geodeRobots) return other.geodeRobots.compareTo(one.geodeRobots)
        else return invOther.obsidian.compareTo(invOne.obsidian)
    }

}
