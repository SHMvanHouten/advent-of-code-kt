package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.negate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun findBeaconPositions(beaconMaps: List<List<Coordinate>>): List<Coordinate> {
    return emptyList()
}

fun List<Coordinate>.listOverlappingBeaconsWithOtherRotatedInAllDirections(other: List<Coordinate>): Pair<List<Coordinate>, Orientation2D>? {
    return rotatedInAllDirections(other)
        .map { (otherBeacons, rotation) -> this.listOverlappingBeaconsWith(otherBeacons) to rotation }
        .filter { it.first != null }
        .map { it.first!! to it.second }
        .firstOrNull()
}

private fun List<Coordinate>.listOverlappingBeaconsWith(
    other: List<Coordinate>
) = asSequence()
    .flatMap { pairWithEachOf(it, other) }
    .groupBy { it.first - it.second }
    .entries
    .find { it.value.size >= 12 }
    ?.value?.map { it.first }

fun rotatedInAllDirections(beacons: List<Coordinate>): List<Pair<List<Coordinate>, Orientation2D>> {
    return beacons
        .normalAndAxesSwitched()
        .map { it.withAndWithoutNegatedX() }.flatten()
        .map { it.withAndWithoutNegatedY() }.flatten()
}

private fun Pair<List<Coordinate>, Orientation2D>.withAndWithoutNegatedX(): List<Pair<List<Coordinate>, Orientation2D>> {
    val (beacons, orientation) = this
    return listOf(
        beacons to orientation,
        beacons.map { it.copy(x = it.x.negate()) } to orientation.copy(negatedX = true)
    )
}

private fun List<Coordinate>.normalAndAxesSwitched(): List<Pair<List<Coordinate>, Orientation2D>> {
    return listOf(
        this to Orientation2D(),
        this.map { Coordinate(it.y, it.x) } to Orientation2D(switchedXY = true)
    )
}

private fun Pair<List<Coordinate>, Orientation2D>.withAndWithoutNegatedY(): List<Pair<List<Coordinate>, Orientation2D>> {
    val (beacons, orientation) = this
    return listOf(
        beacons to orientation,
        beacons.map { it.copy(y = it.y.negate()) } to orientation.copy(negatedY = true)
    )
}

private fun List<Coordinate>.withAndWithoutNegatedX(): List<List<Coordinate>> {
    return listOf(
        this,
        this.map { it.copy(x = it.x.negate()) }
    )
}

private fun pairWithEachOf(beacon: Coordinate, others: List<Coordinate>): List<Pair<Coordinate, Coordinate>> {
    return others.map { otherBeacon -> beacon to otherBeacon }
}

fun to2DBeaconList(block: String): List<Coordinate> {
    return block.lines().map { toCoordinate(it) }
}

data class Orientation2D(
    val switchedXY: Boolean = false,
    val negatedX: Boolean = false,
    val negatedY: Boolean = false
    )