package com.github.shmvanhouten.adventofcode2021.coordinate3d

import com.github.shmvanhouten.adventofcode.utility.coordinate.negate
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Direction.*

data class Coordinate3d(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Coordinate3d): Coordinate3d {
        return Coordinate3d(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Coordinate3d): Coordinate3d {
        return this.plus(other.negate())
    }

    private fun negate(): Coordinate3d {
        return this.copy(x = -x, y = -y, z = -z)
    }

    fun turn(direction: Direction): Coordinate3d {
        val (a, b, c) = this
        return when (direction) {
            POS_X -> this // forward POS_X, up POS_Y, right POS_Z
            NEG_X -> Coordinate3d(a.negate(), b, c.negate()) // forward NEG_X, up POS_Y, right NEG_Z
            POS_Y -> Coordinate3d(b, a.negate(), c) // forward POS_Y, up NEG_X
            NEG_Y -> Coordinate3d(b.negate(), a, c) // forward NEG_Y, up POS_X
            POS_Z -> Coordinate3d(c, b, a.negate())
            NEG_Z -> Coordinate3d(c.negate(), b, a)
        }
    }

    fun rotate90LeftAlongXAxis(): Coordinate3d {
        return this.copy(y = z.negate(), z = y)
    }

    fun rotate90LeftAlongYAxis(): Coordinate3d {
        return this.copy(x = z, z = x.negate())
    }

    fun rotate90LeftAlongZAxis(): Coordinate3d {
        return this.copy(x = y.negate(), y = x)
    }
}


enum class Direction {
    POS_X,
    NEG_X,
    POS_Y,
    NEG_Y,
    POS_Z,
    NEG_Z;

    fun negate(): Direction {
        return if (this.ordinal % 2 == 0) {
            values()[ordinal + 1]
        } else {
            values()[ordinal - 1]
        }
    }
}