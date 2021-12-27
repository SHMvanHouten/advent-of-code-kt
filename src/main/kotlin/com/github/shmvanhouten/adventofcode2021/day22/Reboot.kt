package com.github.shmvanhouten.adventofcode2021.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3d

fun countAmountOfCubesThatWouldBeOn(rebootSteps: List<RebootStep>): Long {
    return 1
}

fun runReboot(rebootSteps: List<RebootStep>): Set<Coordinate3d> {
    return rebootSteps
        .filter { isInBoundsOf50(it) }
        .fold(setOf<Coordinate3d>()) { coordinates, rebootstep ->
            val newCoords = to3dCoordinates(rebootstep.range.xRange, rebootstep.range.yRange, rebootstep.range.zRange)
            when (rebootstep.toggle) {
                Toggle.ON -> coordinates + newCoords
                Toggle.OFF -> coordinates - newCoords.toSet()
            }
        }
}

fun isInBoundsOf50(step: RebootStep): Boolean {
    return listOf(step.range.xRange, step.range.yRange, step.range.zRange)
        .all { it.isInBounds(-50, 50) }
}

private fun IntRange.isInBounds(min: Int, max: Int): Boolean {
    return this.first >= min && this.last <= max
}

private fun to3dCoordinates(xRange: IntRange, yRange: IntRange, zRange: IntRange) =
    xRange.flatMap { x ->
        yRange.flatMap { y ->
            zRange.map { z ->
                Coordinate3d(x, y, z)
            }
        }
    }

data class RebootStep(val toggle: Toggle, val range: Cuboid)

enum class Toggle {
    ON,
    OFF
}

