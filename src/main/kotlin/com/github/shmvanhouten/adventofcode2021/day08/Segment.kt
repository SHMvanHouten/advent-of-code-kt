package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode2021.day08.Segment.*

enum class Segment {
    TOP,
    TOP_RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    TOP_LEFT,
    MIDDLE
}

val segmentsToNumberMapping: Map<Set<Segment>, Int> = listOf(
    (values().toList() - MIDDLE).toSet() to 0,
    setOf(TOP_RIGHT, BOTTOM_RIGHT) to 1,
    setOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM) to 2,
    setOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 3,
    setOf(TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT) to 4,
    setOf(TOP, TOP_LEFT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 5,
    (values().toList() - TOP_RIGHT).toSet() to 6,
    setOf(TOP, TOP_RIGHT, BOTTOM_RIGHT) to 7,
    values().toList().toSet() to 8,
    (values().toList() - BOTTOM_LEFT).toSet() to 9
).toMap()
