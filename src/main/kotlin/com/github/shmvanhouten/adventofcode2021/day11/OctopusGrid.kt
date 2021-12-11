package com.github.shmvanhouten.adventofcode2021.day11

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

fun findFirstTimeAllOctopusesFlash(octopusGrid: OctopusGrid): Int {
    var i = 0
    while (!octopusGrid.octopuses.values.all { it.energy == 0 }) {
        octopusGrid.tick()
        i++
    }
    return i
}

class OctopusGrid(val octopuses: Map<Coordinate, Octopus>, val flashes: Long = 0) {
    constructor(input: String):
            this(
                input.toCoordinateMap { c, location ->
                    Octopus(location, c.digitToInt())
                }
            )

    fun tick(): OctopusGrid {
        octopuses.forEach { it.value.charge() }
        while (octopuses.values.any{it.isReadyToFlash()}) {
            octopuses.values.filter { it.isReadyToFlash() }
                .forEach { flashy ->
                    flashy.flash()
                    flashy.location.getSurrounding()
                        .mapNotNull { octopuses[it] }
                        .filter { !it.flashedThisTick }
                        .forEach { it.charge() }
                }
        }
        val flashedThisTick = octopuses.count { it.value.flashedThisTick }
        octopuses.values.filter { it.flashedThisTick }.forEach { it.reset() }
        return OctopusGrid(octopuses, flashes + flashedThisTick);
    }

    fun  drawGrid(): String {
        return octopuses.mapValues { it.value.energy.digitToChar() }.draw()
    }

    fun tick(i: Int): OctopusGrid {
        return 0.until(i).fold(this) { grid, _ ->
            grid.tick()
        }
    }

}
