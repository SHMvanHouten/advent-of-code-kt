package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Direction
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Direction.*
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
//
//private fun Pair<BeaconMap, Orientation>.facingUpEveryPossibleWay(): List<Pair<BeaconMap, Orientation>> {
//    val (beacons, orientation) = this
//    return when(orientation.facing) {
//        POS_X, NEG_X -> beacons.rotatedAroundTheXAxis(orientation)
//        POS_Y, NEG_Y -> beacons.rotatedAroundTheYAxis(orientation)
//        POS_Z, NEG_Z -> beacons.rotatedAroundTheZAxis(orientation)
//    }
//}

fun List<Coordinate3d>.rotatedAroundTheXAxis(orientation: Orientation): List<Pair<BeaconMap, Orientation>> {
    return generateSequence(this to orientation) { (beacons, orient) ->
        beacons.map { it.rotate90LeftAlongXAxis() } to orient.rotatingLeftAlongXAxis()
    }
        .take(4)
        .toList()
}

//
//fun List<Coordinate3d>.rotatedAroundTheYAxis(orientation: Orientation): List<Pair<BeaconMap, Orientation>> {
//    return generateSequence(this to orientation) { (beacons, orient) ->
//        beacons.map { it.rotate90LeftAlongYAxis() } to orient.rotatingLeftAlongYAxis()
//    }
//        .take(4)
//        .toList()
//}

//private fun BeaconMap.facingEveryWhichWay(): List<Pair<BeaconMap, Orientation>> {
//    return listOf(
//        this to Orientation(),
//        this.map { it.turn(POS_Z) } to Orientation(facing = POS_Z, up = NEG_X),
//        this.map { it.turn(NEG_Z) } to Orientation(facing = NEG_Z),
//        this.map { it.turn(POS_Y)} to Orientation(facing = POS_Y),
//        this.map { it.turn(NEG_Y) } to Orientation(facing = NEG_Y),
//        this.map { it.turn(NEG_X) } to Orientation(facing = NEG_X)
//    )
//
//}

data class Orientation(
    val facing: Direction = POS_X,
    val up: Direction = POS_Y
    // right = POS_Z
) {
    fun rotatingLeftAlongXAxis(): Orientation {
        return this.copy(up = directionToRotateToAlongX()
        )
    }

    private fun directionToRotateToAlongX(): Direction {
        return when (facing) {
            POS_X -> rotateAlongXWhenFacingUp()
            NEG_X -> rotateAlongXWhenFacingUp().negate()
            else -> error("cannot rotate around x axis when facing $facing")
        }
    }

    private fun rotateAlongXWhenFacingUp(): Direction {
        return when(up) {
            POS_Y -> NEG_Z
            NEG_Y -> POS_Z
            POS_Z -> POS_Y
            NEG_Z -> NEG_Y
            else -> error("cannot rotate along x axis when up is $up")
        }
    }
}

typealias RotationFunction = (Coordinate3d) -> Coordinate3d
typealias BeaconMap = List<Coordinate3d>
