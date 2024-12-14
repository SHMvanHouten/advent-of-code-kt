package com.github.shmvanhouten.adventofcode2024.day14

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun main() {
    val robots = readFile("/input-day14.txt")
        .lines()
        .map { toRobot(it) }
//    robots.onEach(::println)
    val moved = moveRobots(100, 101, 103, robots)
    println(quadrants(101, 103).map { it.countRobots(moved) }.onEach { println(it) }.product())
}

fun quadrants(width: Int, height: Int): List<Quadrant> {
    return listOf(
        Quadrant(
        xRange = (0..<width/ 2),
        yRange = (0..<height/ 2)
        ),
        Quadrant(
            xRange = (0..<width/ 2),
            yRange = ((height/2 + 1)..<height)
        ),
        Quadrant(
            xRange = ((width/2 + 1)..<width),
            yRange = (0..<height/ 2)
        ),
        Quadrant(
            xRange = ((width/2 + 1)..<width),
            yRange = ((height/2 + 1)..<height)
        )
    )

}

fun moveRobots(times: Int, width: Int, height: Int, robots: List<Robot>): List<Robot> {
    return generateSequence(robots) { bots ->
        bots.map { it.move(width, height) }
    }.drop(times).first()
}

fun toRobot(input: String): Robot {
    val (position, velocity) = input.words().map { it.substringAfter("=") }
    return Robot(
        toCoordinate(position),
        toCoordinate(velocity)
    )
}

data class Robot(
    val position: Coordinate,
    val velocity: Coordinate
) {
    fun move(width: Int, height: Int): Robot {
        val gridSize = Coordinate(width, height)
        return copy(
            position = (position + gridSize + velocity) % gridSize
        )
    }
}

data class Quadrant(
    val xRange: IntRange,
    val yRange: IntRange
) {
    fun countRobots(robots: List<Robot>): Int {
        return robots.count { (pos, _) ->
            pos.x in xRange && pos.y in yRange
        }
    }
}

