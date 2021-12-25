package com.github.shmvanhouten.adventofcode2021.day23

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode2021.day23.AmphipodType.*
import com.github.shmvanhouten.adventofcode2021.day23.LocationType.*
import java.util.*
import kotlin.math.abs

fun shortestPathToBurrowHappiness(input: Burrow): Long {
    val burrows = priorityQueueOf(input.withFinishedAmphisSetToFinished(), 0L)
    val visitedStates = mutableMapOf<Burrow, Long>()
    while (burrows.isNotEmpty()) {
         val (burrowState, energyTaken) = burrows.poll()

        if((!visitedStates.contains(burrowState) || energyTaken < visitedStates[burrowState]!!)) {
            visitedStates += burrowState to energyTaken

            burrowState
                .doPossibleAmphipodMovements()
                .map { Pair(it.first, it.second + energyTaken) }
                .let { burrows.addAll(it) }
        }
    }
    val minByOrNull = visitedStates.entries.filter { it.key.isInDesiredState() }.minByOrNull { it.value }!!
    val path = minByOrNull.value

    return path
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
    val amphipods: Set<Amphipod>, val amphipodsByCoordinate: Map<Coordinate, Amphipod> = amphipods.associateBy { it.location }
) {
    val minimumEnergyStillRequired: Long = 47283

    fun doPossibleAmphipodMovements(): List<Pair<Burrow, Long>> {
        val possibleMoves = amphipods
            .filter { it.type != D || amphipodsByCoordinate[Coordinate(9, 5)]?.type?.equals(D)?:true }
            .filter { it.movesDone < 2 }
            .flatMap { amphipod ->
                accessibleLocationsFrom(amphipod).map { amphipod to it }
            }.filter { (oldAmphipod, newLocation) ->
                oldAmphipod.movesDone < 1 || isAtHome(oldAmphipod, newLocation)
            }
        val finalLocation = possibleMoves.filter { isAtHome(it.first, it.second) }.maxByOrNull { it.second.y }
        if(finalLocation != null &&
            (finalLocation.second.y == roomDepth ||
                    hasCoordinateOfSameTypeBelowDestinationInRoom(finalLocation))
        ) {
            val (oldAmphipod, newLocation) = finalLocation
            return listOf(
                Burrow((amphipods - oldAmphipod) + oldAmphipod.copy(location = newLocation, movesDone = oldAmphipod.movesDone + 1)) to
                        calculateEnergy(oldAmphipod, newLocation)
            )
        }
        return possibleMoves
            .map { (oldAmphipod, newLocation) ->
                Burrow((amphipods - oldAmphipod) + oldAmphipod.copy(location = newLocation, movesDone = oldAmphipod.movesDone + 1)) to
                        calculateEnergy(oldAmphipod, newLocation)
            }
    }

    private fun hasCoordinateOfSameTypeBelowDestinationInRoom(finalLocation: Pair<Amphipod, Coordinate>): Boolean {
        val (amphipod, location) = finalLocation

        return (amphipodsByCoordinate[location.copy(y = location.y + 1)]!!.type.equals(amphipod.type))
    }

    fun isInDesiredState(): Boolean {
        return amphipods
            .all { isAtHome(it) }
    }

    private fun isAtHome(amphipod: Amphipod, location: Coordinate = amphipod.location) =
        BURROW_LAYOUT[location]?.houses(amphipod)?: error("location $location not mapped as a BURROW_LAYOUT location $BURROW_LAYOUT")

    private fun accessibleLocationsFrom(amphipod: Amphipod): List<Coordinate> {
        return BURROW_LAYOUT.entries
            .filter { it.value.allows(amphipod) }
            .map { it.key }
            .filter { it != amphipod.location }
            .filter { it.isNotBlocked(amphipod.location, amphipods) }
    }

    private fun calculateEnergy(oldAmphipod: Amphipod, newLocation: Coordinate): Long {
        return (abs(oldAmphipod.location.x - newLocation.x)
                + oldAmphipod.location.y - 1
                + newLocation.y - 1) * oldAmphipod.type.stepCost
    }

    fun draw(): String {
        return (emptyBurrow.toCoordinateMap() + amphipods.map { it.location to it.type.name[0] }).draw()
    }

    fun withFinishedAmphisSetToFinished(): Burrow {
        return this.copy(amphipods = amphipods.map {
            if(it.location.y == roomDepth && BURROW_LAYOUT[it.location]!!.houses(it)) it.copy(movesDone = 100)
            else it
        }.toSet())
    }
}

private fun Coordinate.isNotBlocked(thisAmphipodLocation: Coordinate, otherAmphipods: Set<Amphipod>): Boolean {
    return otherAmphipods
        .map { it.location }
        .filter { it != thisAmphipodLocation }
        .none { it.isBetween(this, thisAmphipodLocation) }
}

private fun Coordinate.isBetween(destination: Coordinate, origin: Coordinate): Boolean {
    return origin.pathTo(destination).contains(this)
}

private val coordinatesOnPath = mutableMapOf<Pair<Coordinate, Coordinate>, Set<Coordinate>>()

private fun Coordinate.pathTo(destination: Coordinate): Set<Coordinate> {
    return if (coordinatesOnPath.contains(this to destination)) {
        coordinatesOnPath[this to destination]!!
    } else {
        val coordinatesBetween = calculatePath(destination)
        coordinatesOnPath[this to destination] = coordinatesBetween
        coordinatesOnPath[destination to this] = coordinatesBetween
        coordinatesBetween
    }
}

private fun Coordinate.calculatePath(destination: Coordinate): Set<Coordinate> {
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
)

enum class AmphipodType(val stepCost: Long) {
    A(1),
    B(10),
    C(100),
    D(1000);
}

enum class LocationType() {
    HALLWAY,
    SIDE_ROOM_A,
    SIDE_ROOM_B,
    SIDE_ROOM_C,
    SIDE_ROOM_D,
    DOORWAY;

    fun allows(amphipod: Amphipod): Boolean {
        return this == HALLWAY || houses(amphipod)
    }

    fun houses(amphipod: Amphipod): Boolean {
        return when(amphipod.type) {
            A -> this == SIDE_ROOM_A
            B -> this == SIDE_ROOM_B
            C -> this == SIDE_ROOM_C
            D -> this == SIDE_ROOM_D
        }
    }
}

fun priorityQueueOf(burrow: Burrow, energyRequired: Long): PriorityQueue<Pair<Burrow, Long>> {
    val queue = PriorityQueue(BurrowComparator())
    queue.add(Pair(burrow, energyRequired))
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

private val emptyBurrow = """
                |#############
                |#...........#
                |###.#.#.#.###
                |  #.#.#.#.#
                |  #.#.#.#.#
                |  #.#.#.#.#
                |  #########
                |  """.trimMargin()

private val BURROW_LAYOUT = mapOf(
    Coordinate(1, 1) to HALLWAY,
    Coordinate(2, 1) to HALLWAY,
    Coordinate(3, 1) to DOORWAY,
    Coordinate(4, 1) to HALLWAY,
    Coordinate(5, 1) to DOORWAY,
    Coordinate(6, 1) to HALLWAY,
    Coordinate(7, 1) to DOORWAY,
    Coordinate(8, 1) to HALLWAY,
    Coordinate(9, 1) to DOORWAY,
    Coordinate(10, 1) to HALLWAY,
    Coordinate(11, 1) to HALLWAY,
    Coordinate(3, 2) to SIDE_ROOM_A,
    Coordinate(3, 3) to SIDE_ROOM_A,
    Coordinate(3, 4) to SIDE_ROOM_A,
    Coordinate(3, 5) to SIDE_ROOM_A,
    Coordinate(5, 2) to SIDE_ROOM_B,
    Coordinate(5, 3) to SIDE_ROOM_B,
    Coordinate(5, 4) to SIDE_ROOM_B,
    Coordinate(5, 5) to SIDE_ROOM_B,
    Coordinate(7, 2) to SIDE_ROOM_C,
    Coordinate(7, 3) to SIDE_ROOM_C,
    Coordinate(7, 4) to SIDE_ROOM_C,
    Coordinate(7, 5) to SIDE_ROOM_C,
    Coordinate(9, 2) to SIDE_ROOM_D,
    Coordinate(9, 3) to SIDE_ROOM_D,
    Coordinate(9, 4) to SIDE_ROOM_D,
    Coordinate(9, 5) to SIDE_ROOM_D,
)

private val roomDepth = BURROW_LAYOUT.keys.map { it.y }.maxOrNull()!!

val availableLocations = BURROW_LAYOUT.entries
