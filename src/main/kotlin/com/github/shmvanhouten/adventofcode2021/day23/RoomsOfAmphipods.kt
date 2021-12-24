package com.github.shmvanhouten.adventofcode2021.day23

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode2021.day23.AmphipodType.*
import com.github.shmvanhouten.adventofcode2021.day23.LocationType.*
import java.util.*
import kotlin.math.abs

fun shortestPathToBurrowHappiness(input: Burrow): Pair<String, Long> {
    val burrows = priorityQueueOf(input, 0L)
    val visitedStates = mutableMapOf<Burrow, Pair<Burrow?,Long>>()
    var shortestPath = Long.MAX_VALUE
    while (burrows.isNotEmpty()) {
        val (burrowState, previousBurrow, energyTaken) = burrows.poll()

        if(energyTaken < shortestPath && (!visitedStates.contains(burrowState) || energyTaken < visitedStates[burrowState]!!.second)) {
            visitedStates += burrowState to Pair(previousBurrow, energyTaken)
            if(burrowState.isInDesiredState())
                shortestPath = energyTaken

            burrowState
                .doPossibleAmphipodMovements()
                .map { Triple(it.first ,burrowState, it.second + energyTaken) }
                .let { burrows.addAll(it) }
        }
    }
    val (burrow, path) = visitedStates.entries.single { it.key.isInDesiredState() }

    return createChain(burrow, visitedStates) to path.second
}

fun createChain(burrow: Burrow, visitedStates: MutableMap<Burrow, Pair<Burrow?, Long>>): String {
    var previousBurrow = visitedStates[burrow]?.first
    var string = burrow.draw()
    while (previousBurrow != null) {
        string += "\n"
        string += previousBurrow.draw()
        previousBurrow = visitedStates[previousBurrow]?.first

    }
    return string
}

fun toAmphipodBurrow(input: String): Burrow {
    val amphipods = input.toCoordinateMap { c, coordinate ->
        if (c.isLetter()) {
            Amphipod(coordinate, AmphipodType.valueOf(c.toString()))
        } else null
    }.values.filterNotNull()
    return Burrow(amphipods = amphipods.toSet())
}

data class Burrow(
    val amphipods: Set<Amphipod>
) {
    val minimumEnergyStillRequired: Long = 12000
//        amphipods.sumOf { it.distanceFrom(it.type.finalDestinations.first()) * it.type.stepCost }

    fun doPossibleAmphipodMovements(): List<Pair<Burrow, Long>> {
        return amphipods
            .filter { it.movesDone < 2 }
            .flatMap { amphipod ->
                accessibleLocationsFrom(amphipod).map { amphipod to it }
            }.filter { (oldAmphipod, newLocation) ->
                oldAmphipod.movesDone < 1 || isAtHome(oldAmphipod, newLocation)
            }
            .map { (oldAmphipod, newLocation) ->
                Burrow((amphipods - oldAmphipod) + oldAmphipod.copy(location = newLocation, movesDone = oldAmphipod.movesDone + 1)) to
                        calculateEnergy(oldAmphipod, newLocation)
            }
    }

    fun isInDesiredState(): Boolean {
        return amphipods
            .all { isAtHome(it) }
    }

    private fun isAtHome(amphipod: Amphipod, location: Coordinate = amphipod.location) =
        BURROW_LAYOUT[location]!!.houses(amphipod)

    private fun accessibleLocationsFrom(amphipod: Amphipod): List<Coordinate> {
        val otherAmphipods = amphipods - amphipod
        val accessibleFinalDestinations = amphipod.type.finalDestinations
            .filter { it.isNotBlocked(amphipod.location, otherAmphipods) }
        return if(accessibleFinalDestinations.isNotEmpty() && amphipod.type.finalDestinations.none { dest -> otherAmphipods.any { it.location == dest && it.type != amphipod.type } }) {
            listOf(accessibleFinalDestinations.maxByOrNull { it.y }!!)
        } else BURROW_LAYOUT.entries
            .filter { it.value.allows(amphipod) }
            .map { it.key }
            .filter { it != amphipod.location }
            .filter { it.isNotBlocked(amphipod.location, otherAmphipods) }
    }

    private fun calculateEnergy(oldAmphipod: Amphipod, newLocation: Coordinate): Long {
        return (abs(oldAmphipod.location.x - newLocation.x)
                + oldAmphipod.location.y - 1
                + newLocation.y - 1) * oldAmphipod.type.stepCost
    }

    fun draw(): String {
        return (emptyBurrow.toCoordinateMap() + amphipods.map { it.location to it.type.name[0] }).draw()
    }
}

private fun Coordinate.isNotBlocked(thisAmphipodLocation: Coordinate, otherAmphipods: Set<Amphipod>): Boolean {
    return otherAmphipods.map { it.location }
        .none { it.isBetween(this, thisAmphipodLocation) }
}

private fun Coordinate.isBetween(destination: Coordinate, origin: Coordinate): Boolean {
    return origin.pathTo(destination).contains(this)
}

private fun Coordinate.pathTo(destination: Coordinate): Set<Coordinate> {
    return if(this.x != destination.x) {
        val xRange = rangeOf(this.x, destination.x).map { Coordinate(it, 1) }
        val originYrange = if(this.y == 1) emptySet() else (2..this.y).map { this.copy(y = it) }
        val destinationRange = if(destination.y == 1) emptySet() else (2..destination.y).map { destination.copy(y = it) }
        (xRange + originYrange + destinationRange).toSet()
    } else {
        rangeOf(this.y, destination.y).map { Coordinate(this.x, it) }.toSet()
    }
}

fun rangeOf(one: Int, other: Int): IntRange {
    return if (one > other) other..one
    else one..other
}

data class Amphipod(
    val location: Coordinate,
    val type: AmphipodType,
    val movesDone: Int = 0
) {
    fun distanceFrom(other: Coordinate): Int {
        val absx = abs(this.location.x - other.x)
        return if(absx == 0) absx
        else absx + this.location.y + other.y - 2
    }

}

enum class AmphipodType(val stepCost: Long, val finalDestinations: List<Coordinate>) {
    A(1, listOf(Coordinate(3,2), Coordinate(3,3))),
    B(10, listOf(Coordinate(5,2), Coordinate(5,3))),
    C(100, listOf(Coordinate(7,2), Coordinate(7,3))),
    D(1000, listOf(Coordinate(9,2), Coordinate(9,3)));
}

enum class LocationType(private val allowedAmphipods: Set<AmphipodType>) {
    HALLWAY(AmphipodType.values().toSet()),
    SIDEROOM_A(setOf(A)),
    SIDEROOM_B(setOf(B)),
    SIDEROOM_C(setOf(C)),
    SIDEROOM_D(setOf(D)),
    DOORWAY(emptySet());

    fun allows(amphipod: Amphipod): Boolean {
        return this.allowedAmphipods.contains(amphipod.type)
    }

    fun houses(amphipod: Amphipod): Boolean {
        return this.allowedAmphipods.size == 1 && this.allows(amphipod)
    }
}

fun priorityQueueOf(burrow: Burrow, energyRequired: Long, previousBurrow: Burrow? = null): PriorityQueue<Triple<Burrow, Burrow?, Long>> {
    val queue = PriorityQueue(BurrowComparator())
    queue.add(Triple(burrow, previousBurrow, energyRequired))
    return queue
}

class BurrowComparator : Comparator<Triple<Burrow, Burrow?, Long>> {
    override fun compare(one: Triple<Burrow, Burrow?, Long>?, other: Triple<Burrow, Burrow?, Long>?): Int {
        if (one == null || other == null) error("null burrows")
        val (oneBurrow, _, oneEnergyTaken) = one
        val (otherBurrow, _, otherEnergyTaken) = other
        return (oneBurrow.minimumEnergyStillRequired + oneEnergyTaken)
            .compareTo(otherBurrow.minimumEnergyStillRequired + otherEnergyTaken)
    }

}

private val emptyBurrow = """
                |#############
                |#...........#
                |###.#.#.#.###
                |  #.#.#.#.#
                |  #########
                |  """.trimMargin()

private val BURROW_LAYOUT = mapOf(
//    Coordinate(1, 1) to HALLWAY,
    Coordinate(2, 1) to HALLWAY,
    Coordinate(3, 1) to DOORWAY,
    Coordinate(4, 1) to HALLWAY,
    Coordinate(5, 1) to DOORWAY,
    Coordinate(6, 1) to HALLWAY,
    Coordinate(7, 1) to DOORWAY,
    Coordinate(8, 1) to HALLWAY,
    Coordinate(9, 1) to DOORWAY,
    Coordinate(10, 1) to HALLWAY,
//    Coordinate(11, 1) to HALLWAY,
    Coordinate(3, 2) to SIDEROOM_A,
    Coordinate(3, 3) to SIDEROOM_A,
    Coordinate(5, 2) to SIDEROOM_B,
    Coordinate(5, 3) to SIDEROOM_B,
    Coordinate(7, 2) to SIDEROOM_C,
    Coordinate(7, 3) to SIDEROOM_C,
    Coordinate(9, 2) to SIDEROOM_D,
    Coordinate(9, 3) to SIDEROOM_D,
)
