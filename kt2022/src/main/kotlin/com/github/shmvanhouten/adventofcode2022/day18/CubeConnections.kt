package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.collections.removeAfterIndex
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Axis
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3d

fun countExposedSides(cubes: Set<Coordinate3d>): Int {
    return Droplet(cubes).countExposedSides()
}

class Droplet(private val occupiedSpace: Set<Coordinate3d>) {
    private val airPockets: MutableList<Coordinate3d> = mutableListOf()

    fun countExposedSides(): Int {
        return occupiedSpace.sumOf { countConnectedAndAirPockets(it) }
    }

    private fun countConnectedAndAirPockets(location: Coordinate3d): Int {
        val unattached = location.getSurroundingManhattan()
            .filter { !occupiedSpace.contains(it) }
            .filter { !isPartOfAnAirPocket(it) }
        return unattached.size
    }

    private fun isPartOfAnAirPocket(bubble: Coordinate3d): Boolean {
        airPockets += bubble
        val isSurrounded = isSurroundedInVerticalSpace(bubble) &&
                isSurroundedInHorizontalSpace(bubble) &&
                isSurroundedInDeepSpace(bubble)
        if (!isSurrounded) {
            airPockets.removeAfterIndex(airPockets.indexOf(bubble))
        }
        return isSurrounded
    }

    private fun isSurroundedInVerticalSpace(bubble: Coordinate3d): Boolean {
        val (top, bottom) = closestCubeEitherSide(bubble, Axis.Y)
        return top != null && (bubble.y == top + 1 || isPartOfAnAirPocket(bubble.copy(y = bubble.y - 1)))
                && bottom != null && (bubble.y == bottom - 1 || isPartOfAnAirPocket(bubble.copy(y = bubble.y + 1)))
    }

    private fun isSurroundedInHorizontalSpace(bubble: Coordinate3d): Boolean {
        val (left, right) = closestCubeEitherSide(bubble, Axis.X)
        return left != null && (bubble.x == left + 1 || isPartOfAnAirPocket(bubble.copy(x = bubble.x - 1)))
                && right != null && (bubble.x == right - 1 || isPartOfAnAirPocket(bubble.copy(x = bubble.x + 1)))
    }

    private fun isSurroundedInDeepSpace(bubble: Coordinate3d): Boolean {
        val (closest, farthest) = closestCubeEitherSide(bubble, Axis.Z)
        return closest != null && (bubble.z == closest + 1 || isPartOfAnAirPocket(bubble.copy(z = bubble.z - 1)))
                && farthest != null && (bubble.z == farthest - 1 || isPartOfAnAirPocket(bubble.copy(z = bubble.z + 1)))
    }

    private fun closestCubeEitherSide(bubble: Coordinate3d, axis: Axis): Pair<Int?, Int?> {
        val onAxis = (occupiedSpace + airPockets)
            .filter { it.onSameAxis(axis, bubble) }
            .map { axis.valueOf(it) }

        return onAxis.filter { it < axis.valueOf(bubble) }.maxOrNull() to
                onAxis.filter { it > axis.valueOf(bubble) }.minOrNull()
    }
}

// PART 1
fun countExposedSidesNoBubble(cubes: Set<Coordinate3d>): Int {
    return cubes.sumOf { c1 -> 6 - (cubes - c1).count { c2 -> c1.manhattanDistanceTo(c2) == 1 } }
}

fun parse(input: String): Set<Coordinate3d> {
    return input.lines().map { toCoordinate3d(it) }.toSet()
}

private fun toCoordinate3d(input: String): Coordinate3d {
    val split = input.split(',')
    return Coordinate3d(split[0].toInt(), split[1].toInt(), split[2].toInt())
}
