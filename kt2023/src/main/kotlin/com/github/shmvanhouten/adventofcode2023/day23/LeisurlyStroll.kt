package com.github.shmvanhouten.adventofcode2023.day23

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

class Forest(input: String, private val getDirections: Forest.(Coordinate) -> List<Direction>): Grid<Char>(input, String::toList) {
    fun possiblePathsFrom(location: Coordinate): List<Coordinate> = getDirections(location)
        .map { location.move(it) }
        .filter { this.contains(it) && this[it] != '#' }
}

fun longestSlipperyPath(input: String): Int {
    val grid = Forest(input) {
        when(this[it]) {
            '.' -> Direction.entries
            '^' -> listOf(NORTH)
            '>' -> listOf(EAST)
            'v' -> listOf(SOUTH)
            '<' -> listOf(WEST)
            else -> error("unknown char ${this[it]}")
        }
    }
    return longestPath(grid, allowReverseNode = false)
}

fun longestGrippyPath(input: String): Int {
    val grid = Forest(input) {Direction.entries}
    return longestPath(grid)
}

fun longestPath(grid: Forest, allowReverseNode: Boolean = true): Int {
    val goal = grid.lastCoordinateMatching { it == '.' }!!
    val firstNode = createNode(Coordinate(1, 0), Coordinate(1, 1), grid)
    val reversedFirst = firstNode.reversed()
    val nodes = mutableMapOf((firstNode.first to firstNode.second) to firstNode, (reversedFirst.first to reversedFirst.second) to reversedFirst)
    var biggestFinishedPath: Path? = null
    val incompletePaths = mutableListOf(Path(listOf(firstNode)))

    while (incompletePaths.isNotEmpty()) {
        val path = incompletePaths.removeLast()
        val location = path.last
        if(location == goal) {
            biggestFinishedPath = if(path.length > (biggestFinishedPath?.length ?: 0)) path
            else biggestFinishedPath
        } else {
            val nextLocations = grid.possiblePathsFrom(location)
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
                    if(allowReverseNode) {
                        listOf(node, node.reversed())
                    } else listOf(node)
                }.forEach { node -> nodes[node.first to node.second] = node }
        }
    }

    return biggestFinishedPath!!.length
}

fun createNode(prev: Coordinate, loc: Coordinate, grid: Forest): Node {
    val steps = mutableListOf(prev, loc)
    while (true) {
        val next = grid.possiblePathsFrom(steps.last())
            .filter { it != steps[steps.lastIndex - 1] }
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

data class Path(val nodes: List<Node>, val length: Int = nodes.sumOf { it.length }) {
    operator fun plus(node: Node): Path {
        return Path(nodes + node, length + node.length)
    }

    val penultimateStep: Coordinate by lazy {this.nodes.last().penultimate}

    val last: Coordinate = nodes.last().last

    override fun toString(): String {
        return "${nodes.first().first} - $last : $length"
    }
}
