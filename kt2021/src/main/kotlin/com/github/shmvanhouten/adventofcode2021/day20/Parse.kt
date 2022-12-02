package com.github.shmvanhouten.adventofcode2021.day20

import com.github.shmvanhouten.adventofcode.utility.blocks
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

fun parse(input: String): Pair<String, Image> {
    val (enhancementString, rawImage) = input.blocks()
    return enhancementString to Image(rawImage.toCoordinateMap('#'))
}