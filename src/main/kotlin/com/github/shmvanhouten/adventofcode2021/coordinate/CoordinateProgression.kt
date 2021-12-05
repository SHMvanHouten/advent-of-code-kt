package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import kotlin.math.abs

class CoordinateProgression(private val start: Coordinate, private val end: Coordinate) : Iterable<Coordinate> {
    override fun iterator(): Iterator<Coordinate> = CoordinateIterator(start, end)

    fun isVertical(): Boolean {
        return start.x == end.x
    }

    fun isHorizontal(): Boolean {
        return start.y == end.y
    }

    inner class CoordinateIterator(start: Coordinate, private val end: Coordinate) : Iterator<Coordinate> {

        private val xStep: Int = end.x.compareTo(start.x)
        private val yStep: Int = end.y.compareTo(start.y)

        private var next: Coordinate = start
        private var hasNext: Boolean = if (xStep > 0) next.x <= end.x else next.x >= end.x
                && if (yStep > 0) next.y <= end.y else next.y >= end.y

        init {
            if (
                !isHorizontal() && !isVertical() && isNotAt45Degrees(start, end)
            ) error("Diagonal line is not 45 degrees! from $start to $end")
        }

        override fun hasNext(): Boolean {
            return hasNext
        }

        override fun next(): Coordinate {
            val value = next

            if (value == end) {
                if (!hasNext) throw kotlin.NoSuchElementException()
                hasNext = false

            } else {
                next = Coordinate(next.x + xStep, next.y + yStep)
            }

            return value
        }

    }

}

private fun isNotAt45Degrees(start: Coordinate, end: Coordinate): Boolean {

    return abs(start.x - end.x) != abs(start.y - end.y)
}
