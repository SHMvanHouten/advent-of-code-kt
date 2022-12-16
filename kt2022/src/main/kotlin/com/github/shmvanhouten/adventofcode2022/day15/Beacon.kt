package com.github.shmvanhouten.adventofcode2022.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun findBeaconCoordinates(input: String) : Set<Coordinate> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .map { it.second }
        .toSet()
}

fun findWhereSensorCannotBe(input: String): Set<Coordinate> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .flatMap { findWhereSensorCannotBe(it).map { toCoordinateProgression(it) }.flatten() }
        .toSet()
}

fun toCoordinateProgression(pair: Pair<IntRange, Int>): CoordinateProgression {
    val (xrange, y) = pair
    return CoordinateProgression(Coordinate(xrange.first(), y), Coordinate(xrange.last, y))
}

fun findWhereSensorCannotBeAtY(input: String, y: Int): Set<Int> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .flatMap { findWhereSensorCannotBe(it).filter { it.second == y }.flatMap { it.first } }
        .toSet()
}

fun findWhereSensorCannotBe(input: Pair<Coordinate, Coordinate>): List<Pair<IntRange, Int>> {
    val (sensor, beacon) = input
    val distanceBetween = (sensor - beacon).let { abs(it.x) + abs(it.y) }
    return ((sensor.y - (distanceBetween))..(sensor.y + distanceBetween)).map{y ->
        ((sensor.x - distanceBetween) + (abs(sensor.y - y)))..((sensor.x + distanceBetween) - (abs(sensor.y - y))) to y
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

fun findWhereSensorIsInRange(input: String, min: Int = 0, max: Int = 4000000): Coordinate {
    val sensorRanges = input.lines()
        .map { toSensorRange(it, min, max) }

    sensorRanges.forEach { range ->

        val find = range.edges.find { edgeLoc -> (sensorRanges).none { it.isInRangeOf(edgeLoc) } }
        if(find != null) return find
    }
    error("nothing found")
}

fun toSensorRange(input: String, min: Int, max: Int): SensorRange {
    val (sensor, beacon) = input.splitIntoTwo(":")
    return SensorRange(toCoordinate(sensor), toCoordinate(beacon), min, max)
}

data class SensorRange(
    val sensor: Coordinate,
    val beacon: Coordinate,
    val min: Int = 0,
    val max: Int = 4000000
) {
    val sensorToBeaconDistance = sensor.distanceFrom(beacon)
    fun isInRangeOf(edgeLoc: Coordinate): Boolean {
        return sensor.distanceFrom(edgeLoc) <= sensorToBeaconDistance
    }

    val edges: Sequence<Coordinate> by lazy {
        val distanceBetween = (sensor - beacon).let { abs(it.x) + abs(it.y) }
        sequence {
            (max(sensor.y - 1 - distanceBetween, min)..min(sensor.y + 1 + distanceBetween, max)).map { y ->
                yield(Coordinate((max((sensor.x - 1 - distanceBetween) + (abs(sensor.y - y)), min)), y))
                yield(Coordinate((min((sensor.x + 1 + distanceBetween) - (abs(sensor.y - y)), max)), y))
            }
        }
    }
}

fun Coordinate.frequency(): Long {
    return 4000000L * x + y
}
