package com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d

class Coordinate3DProgression(val start: Coordinate3d, val end: Coordinate3d) : Iterable<Coordinate3d> {

    val size by lazy { this.count() }

    override fun iterator(): Iterator<Coordinate3d> {
        val expandsHorizontally = start.x != end.x
        val expandsVertically = start.y != end.y
        val expandsInDepth = start.z != end.z
        check(!(expandsHorizontally && expandsVertically) && !(expandsVertically && expandsInDepth) && !(expandsHorizontally && expandsInDepth))

        return if (expandsHorizontally) HorizontalIterator(start.x, end.x, start.y, start.z)
        else {
            if(expandsVertically) VerticalIterator(start.y, end.y, start.x, start.z)
            else {
                if(expandsInDepth) DepthIterator(start.z, end.z, start.x, start.y)
                else HorizontalIterator(start.x, start.x, start.y, end.y)
            }
        }
    }

    private inner class HorizontalIterator(start: Int, end: Int, private val y: Int, private val z: Int) : Iterator<Coordinate3d> {
        private val xrange = intRangeIterator(start, end)

        override fun hasNext(): Boolean = xrange.hasNext()

        override fun next(): Coordinate3d {
            val x = xrange.next()
            return Coordinate3d(x, y, z)
        }
    }

    private inner class VerticalIterator(start: Int, end: Int, private val x: Int, private val z: Int) : Iterator<Coordinate3d> {
        private val yRange = intRangeIterator(start, end)

        override fun hasNext(): Boolean = yRange.hasNext()

        override fun next(): Coordinate3d {
            val y = yRange.next()
            return Coordinate3d(x, y, z)
        }
    }

    private inner class DepthIterator(start: Int, end: Int, private val x: Int, private val y: Int) : Iterator<Coordinate3d> {
        private val zRange = intRangeIterator(start, end)

        override fun hasNext(): Boolean = zRange.hasNext()

        override fun next(): Coordinate3d {
            val z = zRange.next()
            return Coordinate3d(x, y, z)
        }
    }

    private fun intRangeIterator(start: Int, end: Int) =
        if (start <= end) start.rangeTo(end).iterator()
        else start.downTo(end).iterator()

    override fun toString(): String {
        return "$start..$end)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate3DProgression

        return start == other.start && end == other.end
    }

    override fun hashCode(): Int {
        return 31 * start.hashCode() + end.hashCode()
    }


}