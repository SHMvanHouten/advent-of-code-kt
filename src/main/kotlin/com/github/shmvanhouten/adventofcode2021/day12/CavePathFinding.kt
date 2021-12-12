package com.github.shmvanhouten.adventofcode2021.day12

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo
import java.util.*
private const val START = "start"
private const val END = "end"
fun findAllPathsThroughCave(vectors: List<String>): List<Path> {
    return findAllPathsThroughCave(parse(vectors))
}

fun findAllPathsThroughCave(vectors: Set<Pair<Node, Node>>): List<Path> {
    val openPaths = LinkedList<Path>(listStartingPaths(vectors))
    val completePaths = mutableListOf<Path>()
    while (openPaths.isNotEmpty()) {
        val path = openPaths.poll()
        val nextPaths = vectors.filter { it.first == path.lastCave() }
            .map { it.second }
            .filter { path.canVisitAgain(it) }
            .map { path + it }
        completePaths += nextPaths.filter { it.isComplete() }
        openPaths += nextPaths.filter { !it.isComplete() }
    }
    return completePaths
}

fun listStartingPaths(vectors: Set<Pair<Node, Node>>): List<Path> {
    return vectors.filter { it.first == START }.map { Path(listOf(it.first, it.second)) }
}

typealias Node = String

data class Path(val nodes: List<Node>) {
    fun lastCave(): Node {
        return nodes.last()
    }

    fun canVisitAgain(it: Node) = it.isUpperCase()
            || !nodes.contains(it)

    operator fun plus(node: Node): Path = Path(nodes + node)

    fun isComplete() = nodes.last() == END
}

private fun String.isUpperCase(): Boolean = this.first().isUpperCase()


fun parse(vectors: List<String>): Set<Pair<Node, Node>> {
    return vectors
        .map { it.splitIntoTwo("-") }
        .flatMap { listOf(it, it.reversed()) }
        .toSet()
}

private fun <A, B> Pair<A, B>.reversed(): Pair<B, A> {
    return this.second to this.first
}
