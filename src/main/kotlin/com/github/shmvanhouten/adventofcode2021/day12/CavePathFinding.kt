package com.github.shmvanhouten.adventofcode2021.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo
import com.github.shmvanhouten.adventofcode2021.day12.RuleSet.PART_1
import java.util.*
internal const val START = "start"
internal const val END = "end"
fun findAllPathsThroughCave(vectors: List<String>, ruleSet: RuleSet = PART_1): List<Path> {
    return findAllPathsThroughCave(parse(vectors), ruleSet)
}

fun findAllPathsThroughCave(vectors: Set<Pair<Node, Node>>, ruleSet: RuleSet): List<Path> {
    val openPaths = LinkedList(listStartingPaths(vectors, ruleSet))
    val completePaths = mutableListOf<Path>()
    while (openPaths.isNotEmpty()) {
        val path = openPaths.poll()
        val nextPaths = listNextSteps(vectors, path)
        completePaths += nextPaths.filter { it.isComplete() }
        openPaths += nextPaths.filter { !it.isComplete() }
    }
    return completePaths
}

private fun listNextSteps(
    vectors: Set<Pair<Node, Node>>,
    path: Path
) = vectors.filter { it.first == path.lastCave() }
    .map { it.second }
    .filter { path.canVisit(it) }
    .map { path + it }

fun listStartingPaths(vectors: Set<Pair<Node, Node>>, ruleSet: RuleSet): List<Path> {
    return vectors.filter { it.first == START }
        .map { Path(nodes = listOf(it.first, it.second), rules = ruleSet ) }
}

typealias Node = String


fun parse(vectors: List<String>): Set<Pair<Node, Node>> {
    return vectors
        .map { it.splitIntoTwo("-") }
        .flatMap { listOf(it, it.reversed()) }
        .toSet()
}

private fun <A, B> Pair<A, B>.reversed(): Pair<B, A> {
    return this.second to this.first
}
