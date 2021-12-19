package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d
import com.github.shmvanhouten.adventofcode2021.coordinate3d.mapping.mappingInAllDirections

fun findBeaconPositions(beaconMaps: List<BeaconMap>): BeaconMap {
    return emptyList()
}

fun BeaconMap.listOverlappingBeaconsWithOtherRotatedInAllDirections(other: BeaconMap): Pair<List<Coordinate3d>, RotationFunction>? {
    return rotatedInAllDirections(other)
        .map { (otherBeacons, rotationFunction) -> this.listOverlappingBeaconsWith(otherBeacons) to rotationFunction }
        .filter { it.first != null }
        .map { it.first!! to it.second }
        .firstOrNull()
}

fun rotatedInAllDirections(beacons: List<Coordinate3d>): List<Pair<BeaconMap, RotationFunction>> {
    return beacons
        .facingEveryWhichWay()
}

private fun BeaconMap.facingEveryWhichWay(): List<Pair<BeaconMap, RotationFunction>> {
    return mappingInAllDirections.map { f -> this.map { f.invoke(it) } to f }
}

private fun List<Coordinate3d>.listOverlappingBeaconsWith(
    other: List<Coordinate3d>
) = asSequence()
    .flatMap { pairWithEachOf(it, other) }
    .groupBy { it.first - it.second }
    .entries
    .find { it.value.size >= 12 }
    ?.value?.map { it.first }

private fun pairWithEachOf(beacon: Coordinate3d, others: List<Coordinate3d>): List<Pair<Coordinate3d, Coordinate3d>> {
    return others.map { otherBeacon -> beacon to otherBeacon }
}

typealias RotationFunction = (Coordinate3d) -> Coordinate3d
typealias BeaconMap = List<Coordinate3d>
