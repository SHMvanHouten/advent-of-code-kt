package com.github.shmvanhouten.adventofcode2022.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo
import kotlin.math.abs

fun findBeaconCoordinates(input: String) : Set<Coordinate> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .map { it.second }
        .toSet()
}

fun findWhereSensorCannotBe(input: String): Set<Coordinate> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .flatMap { findWhereSensorCannotBe(it) }
        .toSet()
}

fun findWhereSensorCannotBe(input: Pair<Coordinate, Coordinate>): List<Coordinate> {
    val (sensor, beacon) = input
    val distanceBetween = (sensor - beacon).let { abs(it.x) + abs(it.y) }
    return ((sensor.y - (distanceBetween + 1))..(sensor.y + distanceBetween)).flatMap{y ->
        (((sensor.x - distanceBetween) + (abs(sensor.y - y)))..((sensor.x + distanceBetween) - (abs(sensor.y - y)))).map { x->
            Coordinate(x, y)
        }
    }
}

fun toSensorBeaconPair(input: String): Pair<Coordinate, Coordinate> {
    val (sensor, beacon) = input.splitIntoTwo(":")
    return toCoordinate(sensor) to toCoordinate(beacon)
}

fun toCoordinate(input: String): Coordinate {
    return Coordinate(
        x = input.substringAfter("x=").substringBefore(',').toInt(),
        y = input.substringAfter("y=").substringBefore(':').toInt()
        )
}
