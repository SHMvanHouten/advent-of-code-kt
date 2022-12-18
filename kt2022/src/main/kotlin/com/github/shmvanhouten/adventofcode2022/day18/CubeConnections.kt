package com.github.shmvanhouten.adventofcode2022.day18

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
            .filter { !isSurroundedWithSideEffects(it) }
        return unattached.size
    }

    private fun isSurroundedWithSideEffects(bubble: Coordinate3d): Boolean {
        airPockets += bubble
        val isSurrounded = isSurroundedInVerticalSpace(bubble) &&
                isSurroundedInHorizontalSpace(bubble) &&
                isSurroundedInDeepSpace(bubble)
        if (!isSurrounded) {
            for (i in airPockets.lastIndex.downTo(airPockets.indexOf(bubble))) {
                airPockets.removeAt(i)
            }
        }
        return isSurrounded
    }

    private fun isSurroundedInVerticalSpace(bubble: Coordinate3d): Boolean {
        val (x, y, z) = bubble
        val vertical = (occupiedSpace + airPockets).filter { it.x == x && it.z == z }.map { it.y }
        val top = vertical.filter { it < y }.maxOrNull()
        val bottom = vertical.filter { it > y }.minOrNull()
        return top != null && (y - top == 1 || isSurroundedWithSideEffects(Coordinate3d(x, y - 1, z)))
                && bottom != null && (bottom - y == 1 || isSurroundedWithSideEffects(Coordinate3d(x, y + 1, z)))
    }

    private fun isSurroundedInHorizontalSpace(bubble: Coordinate3d): Boolean {
        val (x, y, z) = bubble
        val horizontal = (occupiedSpace + airPockets).filter { it.y == y && it.z == z }.map { it.x }
        val left = horizontal.filter { it < x }.maxOrNull()
        val right = horizontal.filter { it > x }.minOrNull()
        return left != null && (x - left == 1 || isSurroundedWithSideEffects(Coordinate3d(x - 1, y, z)))
                && right != null && (right - x == 1 || isSurroundedWithSideEffects(Coordinate3d(x + 1, y, z)))
    }

    private fun isSurroundedInDeepSpace(bubble: Coordinate3d): Boolean {
        val (x, y, z) = bubble
        val inDepth = (occupiedSpace + airPockets).filter { it.x == x && it.y == y }.map { it.z }
        val closest = inDepth.filter { it < z }.maxOrNull()
        val farthest = inDepth.filter { it > z }.minOrNull()
        return closest != null && (z - closest == 1 || isSurroundedWithSideEffects(Coordinate3d(x, y, z - 1)))
                && farthest != null && (farthest - z == 1 || isSurroundedWithSideEffects(Coordinate3d(x, y, z + 1)))
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
