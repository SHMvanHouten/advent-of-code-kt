package com.github.shmvanhouten.adventofcode2022.day16

import com.github.shmvanhouten.adventofcode.utility.collections.arrayDequeOf
import java.util.*
import kotlin.collections.ArrayDeque

fun findMostEfficientReleasePath(valves: Map<String, Valve>): ValvePath {
    val paths = priorityQueueOf(ValvePath(valves["AA"]!!))
    var bestPath: ValvePath? = null
    val flowableValves = valves.filter { it.value.canFlow }.values
    val nrOfValvesThatCanFlow = flowableValves.size
    val pathsBetweenValves: Map<Valve, Map<Valve, Int>> =
        calculateQuickestPaths(flowableValves + valves["AA"]!!, valves).mapValues { it.value.filter { it.first.name != "AA" }.toMap() }
    val maxTime = 26

    while (paths.isNotEmpty()) {
        val path = paths.poll()

        if(path.minute > 5 && (path.pressureReleased/(path.minute * path.minute) < 1)) {
            // drop it
        }
        else if(path.minute > 10 && (path.pressureReleased/(path.minute.toDouble() * path.minute) < 2.2)) {
            // drop it
        }
        if (path.hasHitMinute(maxTime + 1)) {

            if (bestPath == null || path.pressureReleased > bestPath.pressureReleased) {
                bestPath = path
                println("${bestPath.pressureReleased} with ${bestPath.onValves.size} valves on")
            }
        } else if (path.onValves.size == nrOfValvesThatCanFlow) {
            paths.add(path.finish(maxTime + 1))
        } else {

            val target1 = path.goalFor1
            val target2 = path.goalFor2
            if (target1 == null) {
                if (target2 == null) {
                    val availableNextValves = flowableValves - path.onValves
                    val distancesFrom1 = pathsBetweenValves[path.current1]!!
                    val distancesFrom2 = pathsBetweenValves[path.current2]!!
                    availableNextValves.forEach { nextValve1 ->
                        val newPath = path.setGoalFor1(nextValve1, distancesFrom1[nextValve1]!!)
                        (availableNextValves - nextValve1).forEach { nextValve2 ->
                            paths.add(newPath.setGoalFor2(nextValve2, distancesFrom2[nextValve2]!!).tick())
                        }
                    }
                } else {
                    val distancesFrom1 = pathsBetweenValves[path.current1]!!
                    paths += ((flowableValves - path.onValves) - target2.first)
                        .map { path.setGoalFor1(it, distancesFrom1[it]!!).tick() }
                }
            } else if (target2 == null && (flowableValves.size - path.onValves.size) > 1) {
                val distancesFrom2 = pathsBetweenValves[path.current2]!!
                paths += ((flowableValves - path.onValves) - target1.first)
                    .map { path.setGoalFor2(it, distancesFrom2[it]!!).tick() }
            } else {
                paths += path.tick()
            }


        }
    }
    return bestPath ?: error("no path found")
}

fun calculateQuickestPaths(valves: List<Valve>, valvesMap: Map<String, Valve>): Map<Valve, List<Pair<Valve, Int>>> {
    val remainingValves = ArrayDeque(valves)
    val vectors = mutableListOf<ValveVector>()
    while (remainingValves.isNotEmpty()) {
        val valve = remainingValves.removeFirst()
        remainingValves.map { quickestPath(it, valve, valvesMap) }
            .forEach {
                vectors += it
                vectors += it.copy(origin = it.destination, destination = it.origin)
            }
    }
    return vectors.groupBy({ it.origin }, { v -> v.destination to v.steps })
}

fun quickestPath(valve1: Valve, valve2: Valve, valvesMap: Map<String, Valve>): ValveVector {
    val unfinishedPaths = arrayDequeOf(setOf(valve1))
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.removeFirst()
        if (path.last() == valve2) {
            return ValveVector(valve1, valve2, path.size - 1)
        }
        path.last()
            .connectedValves(valvesMap)
            .filter { !path.contains(it) }
            .forEach { unfinishedPaths.addLast(path + it) }
    }
    error("no path found!")
}

data class ValveVector(
    val origin: Valve,
    val destination: Valve,
    val steps: Int
)

data class ValvePath(
    val current1: Valve,
    val current2: Valve,
    val goalFor1: Pair<Valve, Int>? = null,
    val goalFor2: Pair<Valve, Int>? = null,
    val onValves: Set<Valve> = emptySet(),
    val minute: Int = 1,
    val pressurePerMinute: Long = 0,
    val pressureReleased: Long = 0,
    val log: String = ""
) {

    constructor(valve: Valve): this(valve, valve)

    private fun turnOnCurrent1(): ValvePath {
        return this.copy(
            onValves = onValves + goalFor1!!.first,
            pressurePerMinute = pressurePerMinute + goalFor1.first.flowRate,
            goalFor1 = null,
            current1 = goalFor1.first,
            log = log + "\n 1: turning on ${goalFor1.first.name} with flow ${goalFor1.first.flowRate} at minute $minute"
        )
    }

    private fun turnOnCurrent2(): ValvePath {
        return this.copy(
            onValves = onValves + goalFor2!!.first,
            pressurePerMinute = pressurePerMinute + goalFor2.first.flowRate,
            goalFor2 = null,
            current2 = goalFor2.first,
            log = log + "\n 2: turning on ${goalFor2.first.name} with flow ${goalFor2.first.flowRate} at minute $minute"
        )
    }

    fun setGoalFor1(valve: Valve, distance: Int): ValvePath {
        return this.copy(goalFor1 = valve to distance)
    }

    fun setGoalFor2(valve: Valve, distance: Int): ValvePath {
        return this.copy(goalFor2 = valve to distance)
    }

    fun tick(): ValvePath {
        val tick1 = if (goalFor1?.second == 0) turnOnCurrent1()
        else tick1()
        val tick2 = if(goalFor2?.second == 0) tick1.turnOnCurrent2()
        else tick1.tick2()
        return tick2.copy(
            pressureReleased = pressureReleased + pressurePerMinute,
            minute = minute + 1
        )
    }

    private fun tick1(): ValvePath {
        return this.copy(goalFor1 = goalFor1!!.first to goalFor1.second - 1)
    }

    private fun tick2(): ValvePath {
        return if(goalFor2 != null) {
            this.copy(goalFor2 = goalFor2.first to goalFor2.second - 1)
        } else this
    }

    fun hasHitMinute(minute: Int): Boolean = this.minute == minute
    fun finish(maxTime: Int): ValvePath {
        return this.copy(
            pressureReleased = pressureReleased + (pressurePerMinute * (maxTime - minute)),
            minute = maxTime
        )
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

class PathComparator : Comparator<ValvePath> {
    override fun compare(one: ValvePath?, other: ValvePath?): Int {
        if (one == null || other == null) error("null paths")
        return (other.pressureReleased / other.minute * other.minute).compareTo(one.pressureReleased / one.minute * one.minute)
    }

}