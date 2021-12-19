package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d
import com.github.shmvanhouten.adventofcode2021.coordinate3d.mapping.mappingInAllDirections
import java.util.*

fun findBeaconPositions(beaconMaps: List<BeaconMap>): Set<Coordinate3d> {
    val currentScanners = LinkedList(listOf(beaconMaps[0]))
    val remainingScanners = (beaconMaps).toMutableList()
    val beaconsRelativeToScanner0 = beaconMaps[0].coordinates.toMutableSet()
    while (currentScanners.isNotEmpty()) {
        val currentScanner = currentScanners.poll()
        remainingScanners -= currentScanner

        beaconsRelativeToScanner0 += currentScanner.coordinates.map { it + currentScanner.position!! }

        val hits: List<Pair<BeaconMap, BeaconResult>> = remainingScanners
            .map { it to currentScanner.listOverlappingBeaconsWithOtherRotatedInAllDirections(it) }
            .filter { it.second != null }
            .map { it.first to it.second!! }
        currentScanners += hits.map { it.first.copy(
            coordinates = it.first.coordinates.map { c -> it.second.rotationFunctionUsed.invoke(c) },
            rotationFunction = it.second.rotationFunctionUsed,
            position = currentScanner.position!! + it.second.relativePosition
        ) }

    }
    if(remainingScanners.isNotEmpty()) error("did not finish all scanners!")
    return beaconsRelativeToScanner0
}

fun rotatedInAllDirections(beacons: BeaconMap): List<Pair<BeaconMap, RotationFunction>> {
    return beacons
        .facingEveryWhichWay()
}

typealias RotationFunction = (Coordinate3d) -> Coordinate3d

data class BeaconMap(
    val coordinates: List<Coordinate3d>,
    val rotationFunction: RotationFunction = { (a,b,c) -> Coordinate3d(a,b,c)},
    val position: Coordinate3d? = null,
    private val beaconId: UUID = UUID.randomUUID()
) {

    fun listOverlappingBeaconsWithOtherRotatedInAllDirections(other: BeaconMap): BeaconResult? {
        return rotatedInAllDirections(other)
            .mapNotNull { (otherBeacons, rotationFunction) -> this.listOverlappingBeaconsWith(otherBeacons, rotationFunction)}
            .firstOrNull()
    }

    fun facingEveryWhichWay(): List<Pair<BeaconMap, RotationFunction>> {
        return mappingInAllDirections
            .map { f -> this.coordinates.map { f.invoke(it) } to f }
            .map { BeaconMap(it.first) to it.second }
    }

    private fun listOverlappingBeaconsWith(
        other: BeaconMap,
        rotationFunction: RotationFunction
    ) = this.coordinates.asSequence()
        .flatMap { pairWithEachOf(it, other.coordinates) }
        .groupBy { it.first - it.second }
        .entries
        .find { (_, matchingCoordinates) -> matchingCoordinates.size >= 12 }
        ?.let { (relative, matchingCoordinates) ->
            BeaconResult(
                beacons = matchingCoordinates.map { it.first },
                rotationFunction,
                relative
            )
        }

    private fun pairWithEachOf(beacon: Coordinate3d, others: List<Coordinate3d>): List<Pair<Coordinate3d, Coordinate3d>> {
        return others.map { otherBeacon -> beacon to otherBeacon }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeaconMap

        if (beaconId != other.beaconId) return false

        return true
    }

    override fun hashCode(): Int {
        return beaconId.hashCode()
    }

}


data class BeaconResult(
    val beacons: List<Coordinate3d>,
    val rotationFunctionUsed: RotationFunction,
    val relativePosition: Coordinate3d
)
