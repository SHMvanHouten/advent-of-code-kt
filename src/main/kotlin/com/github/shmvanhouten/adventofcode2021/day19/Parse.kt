package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode.utility.blocks
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d

fun parse(input: String): List<BeaconMap> {
    return input.blocks()
        .map{ it.lines() }
        .map { it.subList(1, it.size) }
        .map { toBeaconList(it) }
}

fun toBeaconList(lines: List<String>): List<Coordinate3d> {
    return lines.map { it.toCoordinate3d() }
}

private fun String.toCoordinate3d(): Coordinate3d {
    val (x, y, z) = this.split(',')
    return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
}
