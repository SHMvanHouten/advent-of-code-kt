package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3d

fun countExposedSides(cubes: Set<Coordinate3d>): Int {
    return cubes.sumOf { c1 -> 6 - (cubes - c1).count { c2 -> c1.manhattanDistanceTo(c2) == 1 } }
}




fun parse(input: String): Set<Coordinate3d> {
    return input.lines().map { toCoordinate3d(it) }.toSet()
}

private fun toCoordinate3d(input: String): Coordinate3d {
    val split = input.split(',')
    return Coordinate3d(split[0].toInt(), split[1].toInt(), split[2].toInt())
}
