package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction

private val SAND_SOURCE = Coordinate(500, 0)
data class Cave(
    private val rockLocations: Set<Coordinate>,
    val sandLocations: Set<Coordinate> = emptySet(),
    private val lowestRockY: Int = rockLocations.maxOf { it.y }
) {

    fun plusBedRock(): Cave {
        val bedRockRange = Coordinate(rockLocations.minOf { it.x } - 1000, lowestRockY + 2)..Coordinate(rockLocations.maxOf { it.x } + 1000, lowestRockY + 2)
        return this.copy(rockLocations = rockLocations + bedRockRange, lowestRockY = Int.MAX_VALUE)
    }

    fun tickUntilTopIsReached(): Cave {
        return generateSequence(this) {it.tick()}
            .first { it.sandLocations.contains(SAND_SOURCE) }
    }

    fun tickUntilSaturated(): Cave {
        var cave = this
        while (true) {
            val newCave = cave.tick()
            if(newCave.sandLocations.size == cave.sandLocations.size) return cave
            else cave = newCave
        }
    }

    fun tick(count: Int): Cave {
        return 0.until(count).fold(this) { cave, _ ->
            cave.tick()
        }
    }

    fun tick(): Cave {
        return this.copy(
            sandLocations = dropOneGrainOfSand()
        )
    }

    private fun dropOneGrainOfSand(): Set<Coordinate> {
        var grain = SAND_SOURCE
        while (true) {
            if(grain.y >= lowestRockY) return sandLocations
            val south = grain.move(Direction.SOUTH)
            val (southWest, southEast) = eitherSideFrom(south)
            grain = when {
                locationIsNotOccupied(south) -> south
                locationIsNotOccupied(southWest) -> southWest
                locationIsNotOccupied(southEast) -> southEast
                else -> return sandLocations + grain
            }

        }
    }

    private fun locationIsNotOccupied(location: Coordinate) =
        !rockLocations.contains(location) && !sandLocations.contains(location)

    private fun eitherSideFrom(location: Coordinate): Pair<Coordinate, Coordinate> {
        return Pair(location.move(Direction.WEST), location.move(Direction.EAST))
    }
}

