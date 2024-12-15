package com.github.shmvanhouten.adventofcode2024.day14

import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromCoordinates
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun calculateSafetyFactor(moved: List<Robot>, width: Int, height: Int) =
    quadrants(width, height).map { it.countRobots(moved) }.product()

fun toRobot(input: String): Robot {
    val (position, velocity) = input.words().map { it.substringAfter("=") }
    return Robot(
        toCoordinate(position),
        toCoordinate(velocity)
    )
}

fun moveRobots(times: Int, width: Int, height: Int, robots: List<Robot>): List<Robot> =
    moveRobots(robots, width, height)
        .drop(times).first()

fun moveBotsUntilChristmas(
    robots: List<Robot>,
    width: Int,
    height: Int
): Int = moveRobots(robots, width, height)
    .indexOfFirst(::botsAreInFormation)

private fun moveRobots(
    robots: List<Robot>,
    width: Int,
    height: Int
) = generateSequence(robots) { bots ->
    bots.map { it.move(width, height) }
}

private fun botsAreInFormation(
    bots: List<Robot>
): Boolean {
    val grid = boolGridFromCoordinates(bots.map(Robot::position).toSet())
    val hasContinuousLine = grid.rows().any { hasContinuousLine(it) }
    if(hasContinuousLine) {
        println(grid.map { if(it) '█' else ' ' }.toString())
    }
    return hasContinuousLine
}

private fun hasContinuousLine(list: List<Boolean>, continuousness: Int = 10): Boolean {
    var trueCount = 0
    for (b in list) {
       if(b) trueCount++
       else trueCount = 0

       if(trueCount == continuousness) return true
    }
    return false
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

private fun quadrants(width: Int, height: Int): List<Quadrant> {
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

