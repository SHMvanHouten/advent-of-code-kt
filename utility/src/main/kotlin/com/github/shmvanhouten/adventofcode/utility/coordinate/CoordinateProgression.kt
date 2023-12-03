package com.github.shmvanhouten.adventofcode.utility.coordinate

import kotlin.math.abs

class CoordinateProgression(private val start: Coordinate, private val end: Coordinate) : Iterable<Coordinate> {

    override fun iterator(): Iterator<Coordinate> {
        return if (start.y == end.y) HorizontalIterator(start.x, end.x, start.y)
        else if(start.x == end.x) VerticalIterator(start.y, end.y, start.x)
        else DiagonalIterator(start, end)
    }

    fun isVertical(): Boolean {
        return start.x == end.x
    }

    fun isHorizontal(): Boolean {
        return start.y == end.y
    }

    private inner class HorizontalIterator(start: Int, end: Int, private val y: Int) : Iterator<Coordinate> {
        private val xrange = intRangeIterator(start, end)

        override fun hasNext(): Boolean = xrange.hasNext()

        override fun next(): Coordinate {
            val x = xrange.next()
            return Coordinate(x, y)
        }
    }

    private inner class VerticalIterator(start: Int, end: Int, private val x: Int) : Iterator<Coordinate> {
        private val yRange = intRangeIterator(start, end)

        override fun hasNext(): Boolean = yRange.hasNext()

        override fun next(): Coordinate {
            val y = yRange.next()
            return Coordinate(x, y)
        }
    }

    private inner class DiagonalIterator(start: Coordinate, end: Coordinate) : Iterator<Coordinate> {
        private val xRange = intRangeIterator(start.x, end.x)
        private val yRange = intRangeIterator(start.y, end.y)

        init {
            if (isNotAt45Degrees(start, end))
                error("Diagonal line is not 45 degrees! from $start to $end")
        }

        override fun hasNext(): Boolean =
            xRange.hasNext() && yRange.hasNext()

        override fun next() = Coordinate(xRange.next(), yRange.next())
    }

    private fun intRangeIterator(start: Int, end: Int) =
        if (start <= end) start.rangeTo(end).iterator()
        else start.downTo(end).iterator()
}

private fun isNotAt45Degrees(start: Coordinate, end: Coordinate): Boolean =
    abs(start.x - end.x) != abs(start.y - end.y)
