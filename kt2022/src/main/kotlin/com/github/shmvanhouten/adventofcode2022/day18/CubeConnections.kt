package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3d

fun countExposedSides(cubes: Set<Coordinate3d>): Int = cubes.sumOf { countConnectedAndAirPockets(it, cubes) }


fun countConnectedAndAirPockets(droplet: Coordinate3d, occupiedSpace: Set<Coordinate3d>): Int {
    val unattached = droplet.getSurroundingManhattan()
        .filter { !occupiedSpace.contains(it) }
    return unattached.size - unattached
        .count { isSurrounded(it, occupiedSpace) }

}

fun isSurrounded(bubble: Coordinate3d, occupiedSpace: Set<Coordinate3d>): Boolean {
    return isSurroundedInVerticalSpace(bubble, occupiedSpace) &&
            isSurroundedInHorizontalSpace(bubble, occupiedSpace) &&
            isSurroundedInDeepSpace(bubble, occupiedSpace)
}

fun isSurroundedInVerticalSpace(bubble: Coordinate3d, occupiedSpace: Set<Coordinate3d>): Boolean {
    val (x, y, z) = bubble
    val vertical = occupiedSpace.filter { it.x == x && it.z == z }.map { it.y }
    val top = vertical.filter { it < y }.maxOrNull()
    val bottom = vertical.filter { it > y }.minOrNull()
    return top != null && (y - top == 1 || isSurrounded(Coordinate3d(x, y - 1, z), occupiedSpace + bubble))
            && bottom != null && (bottom - y == 1 || isSurrounded(Coordinate3d(x, y + 1, z), occupiedSpace + bubble))
}

fun isSurroundedInHorizontalSpace(bubble: Coordinate3d, occupiedSpace: Set<Coordinate3d>): Boolean {
    val (x, y, z) = bubble
    val horizontal = occupiedSpace.filter { it.y == y && it.z == z}.map { it.x }
    val left = horizontal.filter { it < x }.maxOrNull()
    val right = horizontal.filter { it > x }.minOrNull()
    return left != null && (x - left == 1 || isSurrounded(Coordinate3d(x - 1, y, z), occupiedSpace + bubble))
            && right != null && (right - x == 1 || isSurrounded(Coordinate3d(x + 1, y, z), occupiedSpace + bubble))
}

fun isSurroundedInDeepSpace(bubble: Coordinate3d, occupiedSpace: Set<Coordinate3d>): Boolean {
    val (x, y, z) = bubble
    val inDepth = occupiedSpace.filter { it.x == x && it.y == y }.map { it.z }
    val closest = inDepth.filter { it < z }.maxOrNull()
    val farthest = inDepth.filter { it > z }.minOrNull()
    return closest != null && (z - closest == 1 || isSurrounded(Coordinate3d(x, y, z - 1), occupiedSpace + bubble))
            && farthest != null && (farthest - z == 1 || isSurrounded(Coordinate3d(x, y, z + 1), occupiedSpace + bubble))
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
