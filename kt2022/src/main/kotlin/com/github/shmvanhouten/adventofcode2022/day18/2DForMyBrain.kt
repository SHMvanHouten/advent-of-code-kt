package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun countExposedSides(cubes: Set<Coordinate>): Int {
    val occupiedSpace = cubes
    return cubes.sumOf { c1 -> countConnectedAndAirPockets(c1, occupiedSpace) }

}

fun countConnectedAndAirPockets(droplet: Coordinate, occupiedSpace: Set<Coordinate>): Int {
//    val directions = airPockets.filter { it.manhattanDistanceTo(droplet) == 1 }.map { it - droplet }
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
    val vertical = occupiedSpace.filter { it.x == x }
    return vertical.filter { it.y < y }.maxOfOrNull { it.y } != null && vertical.filter { it.y > y }
        .minOfOrNull { it.y } != null
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