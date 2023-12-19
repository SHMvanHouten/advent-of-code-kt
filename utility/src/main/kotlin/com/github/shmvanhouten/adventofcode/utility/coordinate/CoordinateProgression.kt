package com.github.shmvanhouten.adventofcode.utility.coordinate

import kotlin.math.abs

class CoordinateProgression(val start: Coordinate, val end: Coordinate) : Iterable<Coordinate> {

    val direction: Boolean by lazy {
        this.isVertical() && this.start.y < this.end.y || this.start.x < end.x
    }
    val size by lazy { this.count() }

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

    fun abs(): CoordinateProgression {
        return if(start.x < end.x || start.y < end.y) this
        else return this.invert()
    }

    private fun invert(): CoordinateProgression {
        return CoordinateProgression(this.end, this.start)
    }

    fun tail(): CoordinateProgression {
        return this.take(2).last()..this.end
    }
}

private fun isNotAt45Degrees(start: Coordinate, end: Coordinate): Boolean =
    abs(start.x - end.x) != abs(start.y - end.y)
