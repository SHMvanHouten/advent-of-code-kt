package com.github.shmvanhouten.adventofcode2022.day16

import java.util.*

fun findMostEfficientReleasePath(valves: Map<String, Valve>): ValvePath {
    val paths = priorityQueueOf(ValvePath(valves["AA"]!!))
    var bestPath: ValvePath? = null
    val nrOfValvesThatCanFlow = valves.filter { it.value.canFlow }.size
    val maxTime = 26

    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if(path.minute > 5 && (path.pressureReleased/(path.minute * path.minute) < 1)) {
            // drop it
        }
        else if(path.minute > 10 && (path.pressureReleased/(path.minute.toDouble() * path.minute) < 2.5)) {
            // drop it
        }
        else if(path.minute > 15 && (path.pressureReleased/(path.minute.toDouble() * path.minute) < 3.2)) {
            // drop it
        }
        else if(path.hasHitMinute(maxTime)) {
            val tick = path.tick()
            if(bestPath == null || tick.pressureReleased > bestPath.pressureReleased) {
                bestPath = tick
                println("${bestPath.pressureReleased} with ${bestPath.onValves.size} valves on")
                bestPath.onValves.map { it.name }.forEach { print("$it, ") }
                println()
            }
        } else if(path.onValves.size == nrOfValvesThatCanFlow) {
            println("all valves turned on at minute ${path.minute}")
            paths.add(path.finish(maxTime))
        } else {

            val (valve1, valve2) = path.currentValve1 to path.currentValve2
            val possibleValve1Changes = mutableSetOf<ValvePath>()
            val ticked = path.tick()
            if(ticked.firstValveCanFlow) possibleValve1Changes += ticked.turnOnCurrent1()
            possibleValve1Changes.addAll(valve1.connectedValves(valves)
                .map { ticked.setGoalFor1(it) })
            possibleValve1Changes.flatMap { newPath ->
                val possibleValve2Changes = mutableSetOf<ValvePath>()
                if(newPath.secondValveCanFlow) possibleValve2Changes.add(newPath.turnOnCurrent2())
                possibleValve2Changes.addAll(valve2.connectedValves(valves)
                    .map { newPath.setGoalFor2(it) })
                possibleValve2Changes
            }.forEach { paths.add(it) }

        }
    }
    return bestPath?: error("no path found")
}

data class ValvePath(
    private val startingValve: Valve,
    val currentValve1: Valve = startingValve,
    val currentValve2: Valve = startingValve,
    val onValves: Set<Valve> = emptySet(),
    val minute: Int = 1,
    val pressurePerMinute: Long = 0,
    val pressureReleased: Long = 0
) {
    val firstValveCanFlow: Boolean = currentValve1.canFlow && !onValves.contains(currentValve1)
    val secondValveCanFlow: Boolean = currentValve2.canFlow && !onValves.contains(currentValve2)

    fun turnOnCurrent1(): ValvePath {
        return this.copy(
            onValves = onValves + currentValve1,
            pressurePerMinute = pressurePerMinute + currentValve1.flowRate
        )
    }

    fun turnOnCurrent2(): ValvePath {
        return this.copy(
            onValves = onValves + currentValve2,
            pressurePerMinute = pressurePerMinute + currentValve2.flowRate
        )
    }

    fun setGoalFor1(valve: Valve): ValvePath {
        return this.copy(currentValve1 = valve)
    }

    fun setGoalFor2(valve: Valve): ValvePath {
        return this.copy(currentValve2 = valve)
    }

    fun tick(): ValvePath {
        return copy(
            pressureReleased = pressureReleased + pressurePerMinute,
            minute = minute + 1
        )
    }

    fun hasHitMinute(minute: Int): Boolean = this.minute == minute
    fun finish(maxTime: Int): ValvePath {
        return this.copy(pressureReleased = pressureReleased + (pressurePerMinute * (maxTime - minute)), minute = maxTime)
    }
}

data class Valve(
    val name: String,
    val flowRate: Int,
    val connectedTo: Set<String>
) {
    fun connectedValves(valves: Map<String, Valve>): List<Valve> {
        return connectedTo.map { valves[it]!! }
    }

    val canFlow: Boolean = flowRate > 0
}

private fun priorityQueueOf(valvePath: ValvePath): PriorityQueue<ValvePath> {
    val queue = PriorityQueue(PathComparator())
    queue.add(valvePath)
    return queue
}

class PathComparator: Comparator<ValvePath> {
    override fun compare(one: ValvePath?, other: ValvePath?): Int {
        if (one == null || other == null) error("null paths")
        return (other.pressureReleased / other.minute * other.minute).compareTo(one.pressureReleased / one.minute * one.minute)
    }

}