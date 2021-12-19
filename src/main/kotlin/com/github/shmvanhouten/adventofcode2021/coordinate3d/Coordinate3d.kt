package com.github.shmvanhouten.adventofcode2021.coordinate3d

import kotlin.math.abs

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

    fun manhattanDistanceTo(other: Coordinate3d): Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

}

class Coordinate3dComparator: Comparator<Coordinate3d> {
    override fun compare(c1: Coordinate3d?, c2: Coordinate3d?): Int {
        if(c1 == null || c2 == null) error("null coordinates")
        val compareX = c1.x.compareTo(c2.x)
        if(compareX!= 0) return compareX
        val compareY = c1.y.compareTo(c2.y)
        return if(compareY != 0) compareY
        else c1.z.compareTo(c2.z)
    }

}