package com.github.shmvanhouten.adventofcode2022.day16

import java.util.*

fun findMostEfficientReleasePath(valves: Map<String, Valve>): ValvePath {
    val paths = priorityQueueOf(ValvePath(valves["AA"]!!))
    var bestPath: ValvePath? = null

    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if(path.minute > 8 && (path.pressureReleased/(path.minute * path.minute) < 1)) {
            // drop it
        }
        else if(path.minute > 15 && (path.pressureReleased/(path.minute.toDouble() * path.minute) < 1.5)) {
            // drop it
        }
        else if(path.minute > 20 && (path.pressureReleased/(path.minute.toDouble() * path.minute) < 1.7)) {
            // drop it
        }
        else if(path.hasHitMinute(30)) {
            if(bestPath == null || path.pressureReleased > bestPath.pressureReleased) {
                bestPath = path
                println(bestPath.pressureReleased)
            }
        } else {

            val currentValve = path.currentValve

            if (path.currentCanFlow) paths.add(path.turnOnCurrent())
            currentValve.connectedValves(valves)
                .map { path.moveTo(it) }
                .forEach { paths.add(it) }
        }
    }
    return bestPath?: error("no path found")
}

data class ValvePath(
    private val startingValve: Valve,
    val currentValve: Valve = startingValve,
    val onValves: Set<Valve> = emptySet(),
    val minute: Int = 1,
    val pressurePerMinute: Long = 0,
    val pressureReleased: Long = 0
) {
    val currentCanFlow: Boolean = currentValve.canFlow && !onValves.contains(currentValve)

    fun turnOnCurrent(): ValvePath {
        return this.copy(
            onValves = onValves + currentValve,
            pressurePerMinute = pressurePerMinute + currentValve.flowRate,
            pressureReleased = pressureReleased + pressurePerMinute,
            minute = minute + 1
        )
    }

    fun moveTo(valve: Valve): ValvePath {
        return this.copy(currentValve = valve, pressureReleased = pressureReleased + pressurePerMinute, minute = minute + 1)
    }

    fun hasHitMinute(minute: Int): Boolean = this.minute == minute
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
        return if(one.minute == other.minute) (other.pressureReleased / other.minute * other.minute).compareTo(one.pressureReleased / one.minute * one.minute)
        else (one.minute).compareTo(other.minute)
    }

}