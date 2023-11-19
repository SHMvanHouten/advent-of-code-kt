package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.grid.Coord
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.RelativePosition.*

private val SAND_SOURCE = Coord(500, 0)
const val ROCK = '█'
const val AIR = ' '
private const val SAND = 'o'

data class CaveGrid(
    private val cave: MutableGrid<Char>,
    private val floorY: Int
) {

    fun dropSandUntilTopIsReached() {
        while (cave[SAND_SOURCE] != SAND) {
            dropOneGrain()
        }
    }

    fun amountOfSand(): Int {
        return this.cave.count{ c -> c == SAND }
    }

    fun print(): String {
        return this.cave.toString().trimIndent()
    }

    private fun dropOneGrain() {
        var grain = SAND_SOURCE
        while (true) {
            val south = grain.move(SOUTH)
            val (southWest, southEast) = eitherSideFrom(south)
            grain = when {
                locationIsNotOccupied(south) -> south
                locationIsNotOccupied(southWest) -> southWest
                locationIsNotOccupied(southEast) -> southEast
                else -> {
                    cave[grain] = SAND; return
                }
            }

        }
    }

    private fun locationIsNotOccupied(location: Coord) =
        location.y < floorY && (!this.cave.hasElementAt(location) || this.cave[location] == AIR)

    private fun eitherSideFrom(location: Coord): Pair<Coord, Coord> {
        return Pair(location.move(WEST), location.move(EAST))
    }

}
