package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun String.toCoordinateMap(): Map<Coordinate, Int> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coordinate(x,y) to c.digitToInt() }
    }.flatten().toMap()
}