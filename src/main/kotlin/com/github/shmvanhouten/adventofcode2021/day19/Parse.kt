package com.github.shmvanhouten.adventofcode2021.day19

import com.github.shmvanhouten.adventofcode.utility.blocks
import com.github.shmvanhouten.adventofcode2021.coordinate3d.Coordinate3d

fun parse(input: String): List<BeaconMap> {
    return input.blocks()
        .map{ it.lines() }
        .map { it[0] to it.subList(1, it.size) }
        .map { toBeaconList(it.second) to it.first }
        .map { (list, id) ->
            val position = if (id == "--- scanner 0 ---") {
                Coordinate3d(0, 0, 0)
            } else {
                null
            }
            BeaconMap(coordinates = list, position = position, id = id)
        }
}

fun toBeaconList(lines: List<String>): List<Coordinate3d> {
    return lines.map { toCoordinate3d(it) }
}

fun toCoordinate3d(line: String): Coordinate3d {
    val (x, y, z) = line.split(',')
    return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
}
