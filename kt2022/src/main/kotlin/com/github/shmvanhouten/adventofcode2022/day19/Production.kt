package com.github.shmvanhouten.adventofcode2022.day19

import java.util.*

fun qualityLevelOf(bluePrints: List<Blueprint>): Int {
    return bluePrints.sumOf { findMaximumGeodeProduction(it).qualityLevel() }
}

fun findMaximumGeodeProduction(
    blueprint: Blueprint,
    start: Production = Production(blueprint.id)
): Production {
    val productions = priorityQueueOf(start)
    var bestProduction: Production? = null
    while (productions.isNotEmpty()) {
        val production = productions.poll()
        if (production.isFinished()) {
            val finishedProduction = production.produce()
            if (bestProduction == null || finishedProduction.inventory.geode > bestProduction.inventory.geode) {
                println("best production: ${production.inventory.geode}")
                bestProduction = finishedProduction
            }
        } else if(!production.hasAChance(bestProduction?.inventory?.geode?:0)) {
            //drop it
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
    fun canProduceOreBot(blueprint: Blueprint): Boolean {
        return blueprint.oreRobot.ore <= ore
    }

    fun canProduceClayBot(blueprint: Blueprint): Boolean {
        return blueprint.clayRobot.ore <= ore
    }

    fun canProduceObsidianBot(blueprint: Blueprint): Boolean {
        return blueprint.obsidianRobot.ore <= ore && blueprint.obsidianRobot.clay <= clay
    }

    fun canProduceGeodeBot(blueprint: Blueprint): Boolean {
        return blueprint.geodeRobot.ore <= ore && blueprint.geodeRobot.obsidian <= obsidian
    }

    fun minus(botCosts: RobotCosts): Inventory {
        return this.copy(
            ore = ore - botCosts.ore,
            clay = clay - botCosts.clay,
            obsidian = obsidian - botCosts.obsidian
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

    fun produce(
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
        if (blueprint.minOreRequirement > oreRobots && inventory.canProduceOreBot(blueprint)) {
            permutations += this.produce(
                inventory = inventory.minus(blueprint.oreRobot),
                extraOreBot = 1
            )
        }
        if (blueprint.obsidianRobot.clay > clayRobots && inventory.canProduceClayBot(blueprint)) {
            permutations += this.produce(
                inventory = inventory.minus(blueprint.clayRobot),
                extraClayBot = 1
            )
        }
        if (blueprint.geodeRobot.obsidian > obsidianRobots && inventory.canProduceObsidianBot(blueprint)) {
            permutations += this.produce(
                inventory = inventory.minus(blueprint.obsidianRobot),
                extraObsBot = 1
            )
        }
        if (inventory.canProduceGeodeBot(blueprint)) {
            permutations += this.produce(
                inventory = inventory.minus(blueprint.geodeRobot),
                extraGeoBot = 1
            )
        }

        permutations += produce()
        return permutations
    }

    fun isFinished(): Boolean {
        return this.minute == 24
    }

    fun qualityLevel(): Int {
        return id * inventory.geode
    }

    fun hasAChance(geode: Int): Boolean {
        return true
//        val minutesLeft = (25 - this.minute)
//        return (minutesLeft * (geodeRobots + 2)) + inventory.geode >= geode
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
