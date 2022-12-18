package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun countExposedSides(cubes: Set<Coordinate>): Int {
    return cubes.sumOf { c1 -> countConnectedAndAirPockets(c1, cubes) }
}

fun countConnectedAndAirPockets(droplet: Coordinate, occupiedSpace: Set<Coordinate>): Int {
    val unAttached = droplet.getSurroundingManhattan()
        .filter { !occupiedSpace.contains(it) }
    return unAttached.size - unAttached
        .count { isSurrounded(it, occupiedSpace) }
}

private fun isSurrounded(bubble: Coordinate, occupiedSpace: Set<Coordinate>): Boolean {

    return isSurroundedInVerticalSpace(bubble, occupiedSpace) &&
            isSurroundedInHorizontalSpace(bubble, occupiedSpace)
}

private fun isSurroundedInVerticalSpace(
    bubble: Coordinate,
    occupiedSpace: Set<Coordinate>
): Boolean {
    val (x, y) = bubble
    val vertical = occupiedSpace.filter { it.x == x }.map { it.y }
    val top = vertical.filter { it < y }.maxOrNull()
    val bottom = vertical.filter { it > y }.minOrNull()
    return top != null && (y - top == 1 || isSurrounded(Coordinate(x, y - 1), occupiedSpace + bubble))
            && bottom != null && (bottom - y == 1 || isSurrounded(Coordinate(x, y + 1), occupiedSpace + bubble))
}

private fun isSurroundedInHorizontalSpace(
    bubble: Coordinate,
    occupiedSpace: Set<Coordinate>
): Boolean {
    val (x, y) = bubble
    val horizontal = occupiedSpace.filter { it.y == y }.map { it.x }
    val toTheLeft = horizontal.filter { it < x }.maxOrNull()
    val toTheRight = horizontal.filter { it > x }.minOrNull()
    return toTheLeft != null && (x - toTheLeft == 1 || isSurrounded(Coordinate(x - 1, y), occupiedSpace + bubble))
            && toTheRight != null && (toTheRight - x == 1 || isSurrounded(Coordinate(x + 1, y), occupiedSpace + bubble))
}

fun parse2d(input: String): Set<Coordinate> {
    return input.lines().map { toCoordinate(it) }.toSet()
}