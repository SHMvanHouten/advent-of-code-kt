package com.github.shmvanhouten.adventofcode.utility.coordinate

import kotlin.math.abs

data class Coordinate3d(val x: Int, val y: Int, val z: Int): Coord {
    val on2dPlane: Coordinate by lazy { Coordinate(x, y) }

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

    fun getSurroundingManhattan(): Set<Coordinate3d> {
        return setOf(
            this + Coordinate3d(-1, 0, 0),
            this + Coordinate3d(1, 0, 0),
            this + Coordinate3d(0, -1, 0),
            this + Coordinate3d(0, 1, 0),
            this + Coordinate3d(0, 0, -1),
            this + Coordinate3d(0, 0, 1),
        )
    }

    fun onSameAxis(axis: Axis, other: Coordinate3d): Boolean {
        return when(axis) {
            Axis.X -> this.y == other.y && this.z == other.z
            Axis.Y -> this.x == other.x && this.z == other.z
            Axis.Z -> this.x == other.x && this.y == other.y
        }
    }

    companion object {
        fun parse(s: String): Coordinate3d {
            val (x, y, z) = s.split(',')
            return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
        }
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

enum class Axis {
    X,
    Y,
    Z;

    fun valueOf(it: Coordinate3d): Int {
        return when(this) {
            X -> it.x
            Y -> it.y
            Z -> it.z
        }
    }
}