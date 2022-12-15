package com.github.shmvanhouten.adventofcode2022.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
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
        .flatMap { findWhereSensorCannotBe(it).flatten() }
        .toSet()
}

fun findWhereSensorCannotBeAtY(input: String, y: Int): Set<CoordinateProgression> {
    return input.lines()
        .map { toSensorBeaconPair(it) }
        .flatMap { findWhereSensorCannotBe(it).filter { it.first().y == y }.filterOutOverlapping() }
        .toSet()
}

private fun List<CoordinateProgression>.filterOutOverlapping(): Set<CoordinateProgression> {
    return this.fold(setOf()) { acc: Set<CoordinateProgression>, newRange ->
        merge(acc, newRange)
    }
}

fun merge(ranges: Set<CoordinateProgression>, newRange: CoordinateProgression): Set<CoordinateProgression> {
    val rangeThatContainsStart = ranges.find { it.contains(newRange.first()) }
    val rangeThatContainsEnd = ranges.find { it.contains(newRange.last()) }
    return if(rangeThatContainsStart == null && rangeThatContainsEnd == null) {
        val rangesInNewRange = ranges.filter { newRange.contains(it.first()) || newRange.contains(it.last()) }
        val rangesToMerge = rangesInNewRange + setOf(newRange)
        val xRange = rangesToMerge.map { it.xRange }.flatten()
        setOf(Coordinate(xRange.min(), newRange.first().y)..Coordinate(xRange.max(), newRange.first().y))
    }
    else if(rangeThatContainsEnd == rangeThatContainsStart) ranges
    else if(rangeThatContainsStart != null && rangeThatContainsEnd != null) {
        ranges - setOf(rangeThatContainsStart, rangeThatContainsEnd) + setOf(CoordinateProgression(rangeThatContainsStart.first(), rangeThatContainsEnd.last()))
    } else if(rangeThatContainsStart != null) {
        ranges - setOf(rangeThatContainsStart) + setOf(CoordinateProgression(rangeThatContainsStart.first(), newRange.last()))
    } else if(rangeThatContainsEnd != null) {
        ranges - setOf(rangeThatContainsEnd) + setOf(CoordinateProgression(newRange.first(), rangeThatContainsEnd.last()))
    }
    else error("how did we get here?")
}

fun findWhereSensorCannotBe(input: Pair<Coordinate, Coordinate>): List<CoordinateProgression> {
    val (sensor, beacon) = input
    val distanceBetween = (sensor - beacon).let { abs(it.x) + abs(it.y) }
    return ((sensor.y - (distanceBetween))..(sensor.y + distanceBetween)).map{y ->
        Coordinate((sensor.x - distanceBetween) + (abs(sensor.y - y)), y)..Coordinate(((sensor.x + distanceBetween) - (abs(sensor.y - y))), y)
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
