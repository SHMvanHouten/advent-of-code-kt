package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d

fun findBeaconPositions(beaconMaps: List<BeaconMap>): BeaconMap {
    return emptyList()
}

fun listOverlappingBeacons(one: BeaconMap, other: BeaconMap): BeaconMap {

    val mapIndexed1 = one.mapIndexed { index, oneC ->
        val otherC = other[index]
        oneC - otherC
    }

    val mapIndexed2 =

    return one
}

typealias BeaconMap = List<Coordinate3d>