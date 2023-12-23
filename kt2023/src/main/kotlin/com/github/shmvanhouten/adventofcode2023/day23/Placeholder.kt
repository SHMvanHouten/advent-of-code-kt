package com.github.shmvanhouten.adventofcode2023.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    readFile("/input-day23.txt")
        .lines()
        .onEach(::println)
}

fun longestPath(input: String): Int {
    val grid = charGrid(input)
    val goal = grid.lastCoordinateMatching { it == '.' }!!
    val firstNode = createNode(Coordinate(1, 0), Coordinate(1, 1), grid)
    val reversedFirst = firstNode.reversed()
    val nodes = mutableMapOf((firstNode.first to firstNode.second) to firstNode, (reversedFirst.first to reversedFirst.second) to reversedFirst)
    var biggestFinishedPath: Path? = null
    val incompletePaths = mutableListOf(Path(listOf(firstNode)))

    while (incompletePaths.isNotEmpty()) {
        val path = incompletePaths.removeLast()
        val location = path.last
        if(location == goal) biggestFinishedPath = if(path.length > (biggestFinishedPath?.length ?: 0)) path else biggestFinishedPath
        else {
            val nextLocations = path.last.getSurroundingManhattan()
                .filter { grid[it] != '#' }
                .filter { it != path.penultimateStep }
            val discoveredNodes = nextLocations.mapNotNull { nodes[location to it] }
            val newNodes = nextLocations
                .filter { loc -> discoveredNodes.none { it.second == loc } }
                .map { createNode(path.last, it, grid) }

            incompletePaths += discoveredNodes
                .filter { node -> node.last !in path.nodes.map { it.first } }
                .map { node -> path + node }
            incompletePaths += newNodes.map { path + it }

            newNodes
                .flatMap { node ->
                    listOf(node, node.reversed())
                }.forEach { node -> nodes[node.first to node.second] = node }
        }
    }

    return biggestFinishedPath!!.length
}

private fun Grid<Char>.possiblePathsFrom(location: Coordinate): List<Coordinate> = location.getSurroundingManhattan()
    .filter { this.contains(it) && this[it] != '#' }

fun createNode(prev: Coordinate, loc: Coordinate, grid: Grid<Char>): Node {
    val steps = mutableListOf(prev, loc)
    while (true) {
        val next = grid.possiblePathsFrom(steps.last())
            .filter { it !in steps }
        if(next.size > 1) {
            return Node(steps)
        } else if(next.isEmpty()) {
            return Node(steps)
        } else {
            steps += next
        }
    }
}

data class Node(val steps: List<Coordinate>, val length: Int = steps.size - 1) {
    fun reversed(): Node {
        return Node(steps.reversed(), length)
    }

    override fun toString(): String {
        return "${steps.first()}-${steps.last()} : ${steps.size}"
    }

    val first: Coordinate = steps.first()
    val second: Coordinate = steps[1]
    val penultimate: Coordinate = steps[steps.lastIndex - 1]
    val last: Coordinate = steps.last()


}

private fun Char.isSlope(): Boolean = this != '.'

private fun Coordinate.moveInDirection(char: Char): Coordinate = when(char) {
    '>' -> this.move(EAST)
    'v' -> this.move(SOUTH)
    '<' -> this.move(WEST)
    '^' -> this.move(NORTH)
    else -> error("unknown char $char")
}

data class Path(val nodes: List<Node>, val length: Int = nodes.sumOf { it.length }) {
    fun lastNode(): Node = this.nodes.last()
    operator fun plus(node: Node): Path {
        return Path(nodes + node, length + node.length)
    }

    val penultimateStep: Coordinate by lazy {this.nodes.last().penultimate}

    val last: Coordinate = nodes.last().last

    override fun toString(): String {
        return "${nodes.first().first} - $last : $length"
    }
}
