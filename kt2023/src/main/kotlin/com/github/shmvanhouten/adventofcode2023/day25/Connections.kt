package com.github.shmvanhouten.adventofcode2023.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2023.day12.tail

fun main() {
    readFile("/input-day25.txt")
        .lines()
        .onEach(::println)
}

fun findNodesToCutBetween(input: String): List<Pair<String, String>> {
    val connections = parse(input)

    val nodeDistances = sortNodePairsByDistanceDescending(connections)
    val (maxDistanceNodes, distance) = nodeDistances.maxBy { it.value }

    val nodeDistancesFromSource = nodeDistances.filter { it.key.first == maxDistanceNodes.first }.mapKeys { it.key.second }
    val nodeDistancesFromTarget = nodeDistances.filter { it.key.first == maxDistanceNodes.second }.mapKeys { it.key.second }

    val groupedByDistanceSource = nodeDistancesFromSource.entries.groupBy { it.value }.mapValues { (_, v) -> v.map { it.key } }.toSortedMap()
    val groupedByDistanceTarget = nodeDistancesFromTarget.entries.groupBy { it.value }.mapValues { (_, v) -> v.map { it.key } }.toSortedMap()
    val potentialFromSource = findGroupBeforeAndAfterFunnel(groupedByDistanceSource)
    val potentialFromTarget = findGroupBeforeAndAfterFunnel(groupedByDistanceTarget)
    val nodesSourceSide = potentialFromSource.first.filter { potentialFromTarget.second.contains(it) }
    val nodesTargetSide = potentialFromTarget.first.filter { potentialFromSource.second.contains(it) }
    return nodesSourceSide.map { it to connections[it]!!.single { nodesTargetSide.contains(it) } }
}

fun findGroupBeforeAndAfterFunnel(groupedByDistanceSource: Map<Int, List<String>>): Pair<List<String>, List<String>> {
    val distances = (groupedByDistanceSource - listOf(1, 2)).toSortedMap().values.toList()
    val funnel = distances.indexOfFirst { it.size <= 23 }
    return distances[funnel - 1] to distances[funnel]
}

fun sortNodePairsByDistanceDescending(input: String): Map<Pair<String, String>, Int> {
    val connections = parse(input)

    return sortNodePairsByDistanceDescending(connections)
}

private fun sortNodePairsByDistanceDescending(connections: Map<String, List<String>>): MutableMap<Pair<String, String>, Int> {
    var distance = 1
    val nodeDistances =
        connections.flatMap { (key, value) -> value.map { (key to it) to distance } }.toMap().toMutableMap()
    var nextDistance = nodeDistances.flatMap { (sourceTarget, _) ->
        val (source, target) = sourceTarget
        connections[target]!!.filter { it != source }.map { source to it }
    }
    while (nextDistance.isNotEmpty()) {
        distance++
        nodeDistances += nextDistance.map { it to distance }
        nextDistance = nextDistance.flatMap { (source, target) ->
            connections[target]!!.filter { it != source }
                .map { source to it }
                .filter { !nodeDistances.contains(it) }
        }
    }

    return nodeDistances
}

private fun <E> List<E>.combineElements(): Sequence<Pair<E, E>> {
    return sequence {
        0.until(size - 1).forEach { i ->
            (i + 1).until(size).forEach { j ->
                yield(this@combineElements[i] to this@combineElements[j])
            }
        }
    }
}

fun divideIntoGroups(input: String): List<Set<String>> {
    val connections = parse(input)

    return connections.permute()
        .asSequence()
        .map { createGroups(it) }
        .first { it.size > 1 }
}

fun removeConnections(input: String, connections: List<Pair<String, String>>): List<Set<String>> {
    return removeConnections(parse(input), connections)
}

fun removeConnections(map: Map<String, List<String>>, connections: List<Pair<String, String>>): List<Set<String>> {
    val newMap = map.toMutableMap()
    connections.forEach { (first, second) ->
        newMap[first] = (newMap[first]!! - second)
        newMap[second] = (newMap[second]!! - first)
    }
    return createGroups(newMap)
}

private fun Map<String, List<String>>.permute(): List<Map<String, List<String>>> {
    return keys.toList().permute()
        .map {
            val connections = this.toMutableMap()
            it.permuteWithConnections(this).map { (first, second) ->
                connections[first] = (connections[first]!! - second)
                connections[second] = (connections[second]!! - first)
            }
            connections
        }
}

private fun List<String>.permuteWithConnections(map: Map<String, List<String>>): List<Pair<String, String>> {
    TODO()
}

private fun List<String>.permute(): List<List<String>> {
    val mapNotNull = (0..(lastIndex - 2)).flatMap { i ->
        ((i + 1)..(lastIndex - 1)).flatMap { j ->
            (j + 1).until(size).map { k ->
                listOf(this[i], this[j], this[k])
            }
        }
    }
    return mapNotNull
}

fun createGroups(connections: Map<String, List<String>>): MutableList<MutableSet<String>> {
    val groups = mutableListOf<MutableSet<String>>()
    connections.keys.forEach {key ->
        var group = groups.firstOrNull { it.contains(key) }
        if(group == null) {
            group = mutableSetOf()
            groups += group
            group.addAll(collectAllConnected(key, connections))
        }
    }
    return groups
}

fun collectAllConnected(start: String, connections: Map<String, List<String>>): Set<String> {
    val result: MutableSet<String> = mutableSetOf()
    val unconnected = mutableListOf(start)
    while (unconnected.isNotEmpty()) {
        val node = unconnected.removeLast()
        if(!result.contains(node)) {
            result += node
            unconnected += connections[node]!!
        }
    }
    return result
}

fun parse(input: String): Map<String, List<String>> {
    val connections = mutableMapOf<String, List<String>>()
    input.lines().map { it.split(": ", " ") }
        .forEach {list ->
            val origin = list.first()
            val targets = list.tail()

            targets.forEach {
                connections.merge(origin, listOf(it), List<String>::plus)
                connections.merge(it, listOf(origin), List<String>::plus)

            }
        }
    return connections.toMap()
}
