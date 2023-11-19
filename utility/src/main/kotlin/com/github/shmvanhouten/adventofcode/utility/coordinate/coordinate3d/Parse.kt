package com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

fun Collection<Coordinate>.make3D(z: Int = 0): List<Coordinate3d> {
    return map { Coordinate3d(it.x, it.y, z) }
}

fun to3DCoordinateMap(vararg input: String): List<Coordinate3d> {
    return input.flatMapIndexed { index: Int, it: String -> it.toCoordinateMap('#').make3D(index) }
}