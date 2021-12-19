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

        beaconsRelativeToScanner0 += currentScanner.coordinates

        val hits: List<Pair<BeaconMap, BeaconResult>> = remainingScanners
            .map { it to currentScanner.listOverlappingBeaconsWithOtherRotatedInAllDirections(it) }
            .filter { it.second != null }
            .map { it.first to it.second!! }

        currentScanners += hits.map { (hitBeaconMap, result) ->
            hitBeaconMap.copy(
            coordinates = hitBeaconMap.coordinates
                .map { c -> result.rotationFunctionUsed.invoke(c) }
                .map { it + result.relativePosition!! }
            , rotationFunction = result.rotationFunctionUsed,
            position = currentScanner.position!! + result.relativePosition!!
        ) }

    }
    if(remainingScanners.isNotEmpty()) error("did not finish all scanners!")
    return beaconsRelativeToScanner0
}

fun rotatedInAllDirections(beacons: BeaconMap): List<BeaconResult> {
    return beacons
        .facingEveryWhichWay()
}

typealias RotationFunction = (Coordinate3d) -> Coordinate3d

data class BeaconMap(
    val coordinates: List<Coordinate3d>,
    val rotationFunction: RotationFunction = { (a,b,c) -> Coordinate3d(a,b,c)},
    val position: Coordinate3d? = null,
    private val beaconId: String
) {

    fun listOverlappingBeaconsWithOtherRotatedInAllDirections(other: BeaconMap): BeaconResult? {
        return rotatedInAllDirections(other)
            .mapNotNull { (otherBeacons, rotationFunction) -> this.listOverlappingBeaconsWith(otherBeacons, rotationFunction)}
            .firstOrNull()
    }

    fun facingEveryWhichWay(): List<BeaconResult> {
        return mappingInAllDirections
            .map { f -> this.coordinates.map { f.invoke(it) } to f }
            .map { BeaconResult(it.first, it.second, null) }
    }

    private fun listOverlappingBeaconsWith(
        othersCoordinates: List<Coordinate3d>,
        rotationFunction: RotationFunction
    ) = this.coordinates.asSequence()
        .flatMap { pairWithEachOf(it, othersCoordinates) }
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
    val relativePosition: Coordinate3d?
)
