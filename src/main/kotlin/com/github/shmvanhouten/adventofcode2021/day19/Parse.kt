package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode.utility.blocks
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d

fun parse(input: String): List<BeaconMap> {
    return input.blocks()
        .map{ it.lines() }
        .map { it.subList(1, it.size) }
        .map { toBeaconList(it) }
        .mapIndexed { i, list ->
            if (i == 0) {
                BeaconMap(coordinates = list, position = Coordinate3d(0, 0, 0))
            } else {
                BeaconMap(list)
            }
        }
}

fun toBeaconList(lines: List<String>): List<Coordinate3d> {
    return lines.map { toCoordinate3d(it) }
}

fun toCoordinate3d(line: String): Coordinate3d {
    val (x, y, z) = line.split(',')
    return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
}
