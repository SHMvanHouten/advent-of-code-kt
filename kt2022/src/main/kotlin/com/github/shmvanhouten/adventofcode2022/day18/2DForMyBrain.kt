package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun countExposedSides(cubes: Set<Coordinate>): Int {
    return cubes.sumOf { c1 -> countConnectedAndAirPockets(c1, cubes) }
}

fun countConnectedAndAirPockets(droplet: Coordinate, occupiedSpace: Set<Coordinate>): Int {
    val unAttached = droplet.getSurroundingManhattan()
        .filter { !occupiedSpace.contains(it) }
    val occupied = occupiedSpace.toMutableList()
    unAttached.toSet().forEach { isSurroundedWithSideEffects(it, occupied) }
    return unAttached.size - unAttached
        .count { occupied.contains(it) }
}

fun isSurroundedWithSideEffects(bubble: Coordinate, occupiedSpace: MutableList<Coordinate>): Boolean {
    occupiedSpace += bubble
    val isSurrounded = isSurroundedInVerticalSpace(bubble, occupiedSpace) &&
            isSurroundedInHorizontalSpace(bubble, occupiedSpace)
    if(!isSurrounded) {
        for (i in occupiedSpace.lastIndex.downTo(occupiedSpace.indexOf(bubble))) {
            occupiedSpace.removeAt(i)
        }
    }
    return isSurrounded
}

private fun isSurroundedInVerticalSpace(
    bubble: Coordinate,
    occupiedSpace: MutableList<Coordinate>
): Boolean {
    val (x, y) = bubble
    val vertical = occupiedSpace.filter { it.x == x }.map { it.y }
    val top = vertical.filter { it < y }.maxOrNull()
    val bottom = vertical.filter { it > y }.minOrNull()
    return top != null && (y - top == 1 || isSurroundedWithSideEffects(Coordinate(x, y - 1), occupiedSpace))
            && bottom != null && (bottom - y == 1 || isSurroundedWithSideEffects(Coordinate(x, y + 1), occupiedSpace))
}

private fun isSurroundedInHorizontalSpace(
    bubble: Coordinate,
    occupiedSpace: MutableList<Coordinate>
): Boolean {
    val (x, y) = bubble
    val horizontal = occupiedSpace.filter { it.y == y }.map { it.x }
    val toTheLeft = horizontal.filter { it < x }.maxOrNull()
    val toTheRight = horizontal.filter { it > x }.minOrNull()
    return toTheLeft != null && (x - toTheLeft == 1 || isSurroundedWithSideEffects(Coordinate(x - 1, y), occupiedSpace))
            && toTheRight != null && (toTheRight - x == 1 || isSurroundedWithSideEffects(Coordinate(x + 1, y), occupiedSpace))
}

fun parse2d(input: String): Set<Coordinate> {
    return input.lines().map { toCoordinate(it) }.toSet()
}