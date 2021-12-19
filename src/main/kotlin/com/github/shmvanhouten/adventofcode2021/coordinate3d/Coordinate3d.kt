package com.github.shmvanhouten.adventofcode2021.coordinate3d

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
}