package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun findCheats3(input: String, cheatSteps: Int = 2, improvementNeeded: Int = 100): Int {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    val quickestPath = quickestPath(grid, Path(listOf(start)), goal)!!
//    println(quickestPath.print(grid))
    val path = quickestPath.steps
    return possibleCheatsAlongPath(grid, quickestPath, cheatSteps)
        .filter { (start, end) -> path.subList(0, path.indexOf(start)).size + path.subList(path.indexOf(end), path.size).size + start.distanceFrom(end) <= path.size - improvementNeeded }
        .distinct()
        .count()

}