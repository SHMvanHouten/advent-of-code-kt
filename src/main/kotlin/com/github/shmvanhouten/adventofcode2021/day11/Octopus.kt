package com.github.shmvanhouten.adventofcode2021.day11

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

data class Octopus(val location: Coordinate, var energy: EnergyLevel, var flashedThisTick: Boolean = false) {
    fun charge() {
        this.energy = energy + 1
    }

    fun isReadyToFlash() = energy >= 10
    fun flash() {
        energy = 0
        flashedThisTick = true
    }

    fun reset() {
        if(energy != 0) error("flashed octopus should have an energy level of 0, not $energy")
        flashedThisTick = false
    }

}

typealias EnergyLevel = Int
