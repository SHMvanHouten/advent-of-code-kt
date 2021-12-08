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

val segmentToDigitMapping: Map<List<Segment>, Int> = listOf(
    values().toList() - MIDDLE to 0,
    listOf(TOP_RIGHT, BOTTOM_RIGHT) to 1,
    listOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM) to 2,
    listOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 3,
    listOf(TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT) to 4,
    listOf(TOP, TOP_LEFT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 5,
    values().toList() - TOP_RIGHT to 6,
    listOf(TOP, TOP_RIGHT, BOTTOM_RIGHT) to 7,
    values().toList() to 8,
    values().toList() - BOTTOM_LEFT to 9
).associate { it.first.sorted() to it.second }

//val zero = values().toList() - MIDDLE
//val one = listOf(TOP_RIGHT, BOTTOM_RIGHT)
//val two = listOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM)
//val three = listOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM)
//val four = listOf(TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT)
//val five = listOf(TOP, TOP_LEFT, MIDDLE, BOTTOM_RIGHT, BOTTOM)
//val six = values().toList() - TOP_RIGHT
//val seven = listOf(TOP, TOP_RIGHT, BOTTOM_RIGHT)
//val eight = values().toList()
//val nine = values().toList() - BOTTOM_LEFT
