package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode.utility.lambda.identity
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d
import com.github.shmvanhouten.adventofcode2021.coordinate3d.mapping.mappingInAllDirections
import java.util.*

fun findBeaconPositions(beaconMaps: List<BeaconMap>): Pair<Set<Coordinate3d>, List<Coordinate3d>> {
    val currentScanners = LinkedList(listOf(beaconMaps[0]))
    val remainingScanners = (beaconMaps).toMutableList()
    val beaconsRelativeToScanner0 = beaconMaps[0].coordinates.toMutableSet()
    val scannerPositions = mutableListOf<Coordinate3d>()
    while (currentScanners.isNotEmpty()) {
        val currentScanner = currentScanners.poll()
        remainingScanners -= currentScanner

        beaconsRelativeToScanner0 += currentScanner.coordinates
        scannerPositions += currentScanner.position!!

        currentScanners += remainingScanners
            .map { it to currentScanner.listOverlappingBeaconsWithOtherRotatedInAllDirections(it) }
            .filter { it.second != null }
            .map { it.first to it.second!! }
            .map { (hitBeaconMap, result) ->
                hitBeaconMap.copy(
                    coordinates = orientBeaconsTowardOrigin(hitBeaconMap, result),
                    rotationFunction = result.rotationFunctionUsed,
                    position = result.relativePosition!!
                )
            }

    }
    if (remainingScanners.isNotEmpty()) error("did not finish all scanners!")

    return beaconsRelativeToScanner0 to scannerPositions
}

private fun orientBeaconsTowardOrigin(
    hitBeaconMap: BeaconMap,
    result: BeaconResult
) = hitBeaconMap.coordinates
    .map { c -> result.rotationFunctionUsed.invoke(c) }
    .map { it + result.relativePosition!! }

fun calculateLongestDistance(positions: List<Coordinate3d>): Int? {
    return positions
        .flatMap { c -> positions.map { it.manhattanDistanceTo(c) } }
        .maxOrNull()
}

typealias RotationFunction = (Coordinate3d) -> Coordinate3d

data class BeaconMap(
    val coordinates: List<Coordinate3d>,
    val rotationFunction: RotationFunction = ::identity,
    val position: Coordinate3d? = null,
    val id: String
) {

    fun listOverlappingBeaconsWithOtherRotatedInAllDirections(other: BeaconMap): BeaconResult? {

        return other.facingEveryWhichWay()
            .mapNotNull { (otherBeacons, rotationFunction) ->
                this.listOverlappingBeaconsWith(otherBeacons, rotationFunction)
            }
            .firstOrNull()
    }

    fun facingEveryWhichWay(): Sequence<Pair<List<Coordinate3d>, RotationFunction>> {
        return mappingInAllDirections
            .asSequence()
            .map { f -> this.coordinates.map { f.invoke(it) } to f }
    }

    private fun listOverlappingBeaconsWith(
        othersCoordinates: List<Coordinate3d>,
        rotationFunction: RotationFunction
    ) = this.coordinates
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

    private fun pairWithEachOf(
        beacon: Coordinate3d,
        others: List<Coordinate3d>
    ): List<Pair<Coordinate3d, Coordinate3d>> =
        others.map { otherBeacon -> beacon to otherBeacon }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeaconMap

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}


data class BeaconResult(
    val beacons: List<Coordinate3d>,
    val rotationFunctionUsed: RotationFunction,
    val relativePosition: Coordinate3d?
)
