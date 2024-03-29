package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.github.shmvanhouten.adventofcode.utility.coordinate.Degree.D90

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;


    fun turnLeft(): Direction {
        return if (this == NORTH) {
            WEST
        } else {
            values()[this.ordinal - 1]
        }
    }

    fun turnRight(): Direction {
        return if (this == WEST) {
            NORTH
        } else {
            values()[this.ordinal + 1]
        }
    }

    fun turnBack(): Direction {
        return when(this){
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
    }

    fun turn(turn: Turn, by: Degree = D90): Direction {
        return turn(turn.by(by))
    }

    private fun turn(turn: Turn): Direction {
        return when (turn) {
            Turn.LEFT -> this.turnLeft()
            Turn.RIGHT -> this.turnRight()
            Turn.FORWARD -> this
            Turn.BACK -> this.turnBack()
        }
    }

    fun opposite(): Direction {
        return this.turnBack()
    }
}

enum class RelativePosition(val coordinate: Coordinate) {
    NORTH(Coordinate(0, -1)),
    NORTH_EAST(Coordinate(1, -1)),
    EAST(Coordinate(1, 0)),
    SOUTH_EAST(Coordinate(1, 1)),
    SOUTH(Coordinate(0, 1)),
    SOUTH_WEST(Coordinate(-1, 1)),
    WEST(Coordinate(-1, 0)),
    NORTH_WEST(Coordinate(-1, -1))
}
