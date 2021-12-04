package com.github.shmvanhouten.adventofcode2021.depth.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.negate


    fun Coordinate.move(direction: Direction, distance: Int = 1): Coordinate {
        return when (direction) {
            Direction.UP -> this + Coordinate(0, distance.negate())
            Direction.DOWN -> this + Coordinate(0, distance)
            Direction.FORWARD -> this + Coordinate(distance, 0)
        }
    }

