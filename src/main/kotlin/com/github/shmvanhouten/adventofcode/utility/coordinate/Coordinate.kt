package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.github.shmvanhouten.adventofcode.utility.coordinate.Degree.*
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition.*
import com.github.shmvanhouten.adventofcode2020.coordinate.ClockDirection
import com.github.shmvanhouten.adventofcode2020.coordinate.ClockDirection.CLOCKWISE
import com.github.shmvanhouten.adventofcode2020.coordinate.ClockDirection.COUNTER_CLOCKWISE
import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) {
    fun getSurrounding(): Set<Coordinate> {
        return setOf(
            this + TOP.coordinate,
            this + TOP_RIGHT.coordinate,
            this + RIGHT.coordinate,
            this + BOTTOM_RIGHT.coordinate,
            this + BOTTOM.coordinate,
            this + BOTTOM_LEFT.coordinate,
            this + LEFT.coordinate,
            this + TOP_LEFT.coordinate
        )
    }

    fun getNeighbour(directionPointed: Direction): Coordinate {
        return when (directionPointed) {
            Direction.NORTH -> this.inDirection(TOP)
            Direction.EAST -> this.inDirection(RIGHT)
            Direction.SOUTH -> this.inDirection(BOTTOM)
            Direction.WEST -> this.inDirection(LEFT)
        }
    }

    fun move(direction: Direction, distance: Int = 1): Coordinate {
        return when (direction) {
            Direction.NORTH -> this + Coordinate(0, distance.negate())
            Direction.EAST -> this + Coordinate(distance, 0)
            Direction.SOUTH -> this + Coordinate(0, distance)
            Direction.WEST -> this + Coordinate(distance.negate(), 0)
        }
    }

    fun inDirection(direction: RelativePosition): Coordinate {
        return this + direction.coordinate
    }

    operator fun plus(otherCoordinate: Coordinate): Coordinate {
        val x = this.x + otherCoordinate.x
        val y = this.y + otherCoordinate.y
        return Coordinate(x, y)
    }

    operator fun minus(otherCoordinate: Coordinate): Coordinate {
        return this.plus(otherCoordinate.negate())
    }

    private fun negate(): Coordinate {
        return this.copy(x = -x, y = -y)
    }

    fun isInBounds(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean =
        this.x in minX..maxX
                && this.y in minY..maxY

    fun distanceFrom(other: Coordinate): Int {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }

    fun times(amount: Int): Coordinate {
        return Coordinate(this.x * amount, this.y * amount)
    }

    fun turnRelativeToOrigin(direction: ClockDirection, degrees: Degree): Coordinate {
        return when {
            shouldRotateASemiCircle(degrees) -> {
                mirror(this)

            }
            shouldRotateClockWise(direction, degrees) -> {
                rotateClockwiseRelativeToOrigin()

            }
            shouldRotateCounterClockwise(direction, degrees) -> {
                rotateCounterClockwiseRelativeToOrigin()

            }
            else -> {
                error("unsupported turn")
            }
        }
    }

    private fun rotateCounterClockwiseRelativeToOrigin() = mirror(rotateClockwiseRelativeToOrigin())

    private fun rotateClockwiseRelativeToOrigin(): Coordinate {
        return when {
            this.x >= 0 && this.y >= 0 -> {
                this.copy(x = y.negate(), y = x)

            }
            this.x < 0 && this.y >= 0 -> {
                this.copy(x = y.negate(), y = x)

            }
            this.x >= 0 && this.y < 0 -> {
                this.copy(x = y.negate(), y = x)

            }
            this.x < 0 && this.y < 0 -> {
                this.copy(x = y.negate(), y = x)

            }
            else -> {
                error("Down is up and up is down, what is going on?")
            }
        }
    }

    private fun mirror(start: Coordinate) =
        Coordinate(start.x.negate(), start.y.negate())

    private fun shouldRotateASemiCircle(degrees: Degree) = degrees == D180

    private fun shouldRotateClockWise(
        direction: ClockDirection,
        degrees: Degree
    ) = direction == CLOCKWISE && degrees == D90
            || direction == COUNTER_CLOCKWISE && degrees == D270

    private fun shouldRotateCounterClockwise(
        direction: ClockDirection,
        degrees: Degree
    ) = direction == COUNTER_CLOCKWISE && degrees == D90
            || direction == CLOCKWISE && degrees == D270

}

fun Int.negate(): Int {
    return this * -1
}

/**
 * returns a set of coordinates for wherever the char is found in the string
 */
fun String.toCoordinateMap(targetChar: Char = '#'): Set<Coordinate> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if(c == targetChar) {
                Coordinate(x, y)
            } else {
                null
            }
        }.filterNotNull()
    }.flatten().toSet()
}

fun draw(coordinates: Collection<Coordinate>): String {
    val ys = coordinates.map { it.y }
    val xes = coordinates.map { it.x }
    return (ys.minOrNull()!!..ys.maxOrNull()!!).joinToString("\n") { y ->
        (xes.minOrNull()!!..xes.maxOrNull()!!).map { x ->
            if (coordinates.contains(Coordinate(x, y))) {
                '\u2591'
            } else {
                '\u2588'
            }
        }.joinToString("")
    }
}

fun Set<Coordinate>.orientFromTopLeftMostCoordinate(): Set<Coordinate> {
    val topLeftMost = this.top().minByOrNull { it.x }!!
    return this.map { it.minus(topLeftMost) }.toSet()
}

fun Set<Coordinate>.top(): Set<Coordinate> {
    return this.filter { it.y == findMinimumY() }.toSet()
}

fun <T> Map<Coordinate, T>.top(): Map<Coordinate, T> {
    return this.filter { it.key.y == this.keys.findMinimumY() }
}

fun <T> Map<Coordinate, T>.bottom(): Map<Coordinate, T> {
    return this.filter { it.key.y == this.keys.findMaxY() }
}

fun <T> Map<Coordinate, T>.left(): T {
    return this.minByOrNull { it.key.x }?.value?: error("minBy called on empty collection")
}

fun <T> Map<Coordinate, T>.right(): T {
    return this.maxByOrNull { it.key.x }?.value?: error("maxBy called on empty collection")
}

private fun Set<Coordinate>.findMaxY() =
    this.map { it.y }.maxOrNull()

private fun Set<Coordinate>.findMinimumY() =
    this.map { it.y }.minOrNull()
