package com.github.shmvanhouten.adventofcode2023.day02

import com.github.shmvanhouten.adventofcode.utility.collectors.productOf
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

const val RED = "red"
const val BLUE = "blue"
const val GREEN = "green"

fun parse(string: String): Game {
    val id = string.substringBetween("Game ", ":").toInt()
    val cubes = string.substringAfter(": ")
        .split("; ")
        .map { parseSet(it) }
    return Game(id, cubes)
}

fun parseSet(input: String): CubeSet {
    return input.split(", ")
        .map { it.split(" ") }
        .map { (nr, color) -> color to nr.toInt() }
        .map { Cube(it.first, it.second) }
        .associateBy { c -> c.color }
}

fun List<Game>.sumIdsOfPossibleGames(requirements: CubeSet): Int =
    filter { it.isPossible(requirements) }
        .sumOf { it.id }

fun List<Game>.sumOfPowers(): Long =
        map(Game::minimumsPerColor)
       .sumOf { it.productOf(Cube::count) }

data class Game(val id: Int, val cubeSets: List<CubeSet>) {
    fun isPossible(requirements: CubeSet): Boolean {
        return cubeSets.none { cubeSet ->
            cubeSet.values.any { it.doesNotMeetRequirements(requirements) }
        }
    }

    fun minimumsPerColor(): List<Cube> =
        listOf(
            Cube(RED, maximumOfColor(RED)),
            Cube(GREEN, maximumOfColor(GREEN)),
            Cube(BLUE, maximumOfColor(BLUE))
        )

    private fun maximumOfColor(color: String) = cubeSets.maxOf { cubes ->
        cubes[color]?.count ?: 0
    }
}

data class Cube(val color: Color, val count: Count) {
    fun doesNotMeetRequirements(requirements: CubeSet) =
        this.count > requirements[this.color]!!.count

}
typealias CubeSet = Map<Color, Cube>

typealias Color = String
typealias Count = Int