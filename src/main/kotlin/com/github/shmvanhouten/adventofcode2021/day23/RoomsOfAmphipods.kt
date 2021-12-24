package com.github.shmvanhouten.adventofcode2021.day23

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode2021.day23.AmphipodType.*
import com.github.shmvanhouten.adventofcode2021.day23.LocationType.*
import java.util.*
import kotlin.math.abs

fun shortestPathToBurrowHappiness(input: Burrow): Long {
    val burrows = priorityQueueOf(input, 0L)
    val visitedStates = mutableMapOf<Burrow, Long>()
    var shortestPath = Long.MAX_VALUE
    while (burrows.isNotEmpty()) {
        val (burrowState, energyTaken) = burrows.poll()

        if(energyTaken < shortestPath && (!visitedStates.contains(burrowState) || energyTaken < visitedStates[burrowState]!!)) {
            visitedStates += burrowState to energyTaken
            if(burrowState.isInDesiredState())
                shortestPath = energyTaken

            burrowState
                .doPossibleAmphipodMovements()
                .map { it.first to it.second + energyTaken }
                .let { burrows.addAll(it) }
        }
    }
    return visitedStates.entries.single { it.key.isInDesiredState() }.value
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
}

private fun Coordinate.isNotBlocked(thisAmphipodLocation: Coordinate, otherAmphipods: Set<Amphipod>): Boolean {
    return otherAmphipods.map { it.location }
        .none { it.isBetween(this, thisAmphipodLocation) }
}

private fun Coordinate.isBetween(destination: Coordinate, origin: Coordinate): Boolean {
    return if (this.x !in rangeOf(origin.x, destination.x)) false
    else if (this.y == 1 && destination.y == 1) true
    else if(destination.x == this.x) destination.y >= this.y
    else this.x == origin.x && this.y < origin.y
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Amphipod

        if (location != other.location) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + type.hashCode()
        return result
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

fun priorityQueueOf(burrow: Burrow, energyRequired: Long): PriorityQueue<Pair<Burrow, Long>> {
    val queue = PriorityQueue(BurrowComparator())
    queue.add(burrow to energyRequired)
    return queue
}

class BurrowComparator : Comparator<Pair<Burrow, Long>> {
    override fun compare(one: Pair<Burrow, Long>?, other: Pair<Burrow, Long>?): Int {
        if (one == null || other == null) error("null burrows")
        val (oneBurrow, oneEnergyTaken) = one
        val (otherBurrow, otherEnergyTaken) = other
        return (oneBurrow.minimumEnergyStillRequired + oneEnergyTaken)
            .compareTo(otherBurrow.minimumEnergyStillRequired + otherEnergyTaken)
    }

}


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
