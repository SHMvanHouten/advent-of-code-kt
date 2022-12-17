package com.github.shmvanhouten.adventofcode2022.day16.part1

import com.github.shmvanhouten.adventofcode.utility.collections.arrayDequeOf
import com.github.shmvanhouten.adventofcode2022.day16.Valve
import com.github.shmvanhouten.adventofcode2022.day16.calculateQuickestPaths

fun findBestPath(valves: Map<String, Valve>): Path {
    val paths = arrayDequeOf(Path(valves["AA"]!!))
    var bestPath: Path? = null
    val flowableValves = valves.filter { it.value.canFlow }.values
    val nrOfValvesThatCanFlow = flowableValves.size
    val pathsBetweenValves: Map<Valve, Map<Valve, Int>> =
        calculateQuickestPaths(flowableValves + valves["AA"]!!, valves).mapValues { it.value.filter { it.first.name != "AA" }.toMap() }
    val maxTime = 30

    while (paths.isNotEmpty()) {
        val path = paths.removeFirst()
        if(path.hasHitMinute(maxTime + 1)) {
            if(bestPath == null || path.pressureReleased > bestPath.pressureReleased) {
                bestPath = path
                println("${bestPath.pressureReleased} with ${bestPath.valvesOn.size} valves on")
            }
        }
        else if(path.valvesOn.size == nrOfValvesThatCanFlow) {
            paths.add(path.finish(maxTime + 1))

        } else {
            if(path.hasHitTargetValve()) {
                paths.add(path.turnOnValve())

            } else if(path.needsANewTarget()) {
                val availableNextValves = flowableValves - path.valvesOn
                val map = pathsBetweenValves[path.currentValve]!!
//                availableNextValves.map { path.setGoalFor() }
            }
        }
    }

    return bestPath?: error("No path found")
}

data class Path(
    val currentValve: Valve,
    val valvesOn: Set<Valve> = emptySet(),
    val targetValve: Valve? = null,
    val distanceFromTargetValve: Int = -1,
    val minute: Int = 1,
    val pressureReleased: Long = 0,
    val pressureReleasePm: Long = 0,
    val log: String = ""
) {
    fun hasHitMinute(i: Int): Boolean = this.minute == minute
    fun finish(maxTime: Int): Path {

        return this.copy(
            minute = maxTime,
            pressureReleased = pressureReleased + (pressureReleasePm * (maxTime - minute))
        )
    }

    fun hasHitTargetValve(): Boolean {
        return targetValve != null && distanceFromTargetValve == 0
    }

    fun turnOnValve(): Path {
        return this.copy(
            valvesOn = valvesOn + targetValve!!,
            pressureReleasePm = pressureReleased + targetValve.flowRate,
            targetValve = null,
            log = log + "\n turning on ${targetValve.name} with flow ${targetValve.flowRate} at minute $minute"
        )
    }

    fun needsANewTarget(): Boolean {
        return targetValve == null
    }
}