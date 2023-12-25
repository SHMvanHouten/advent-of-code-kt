package com.github.shmvanhouten.adventofcode2023.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode2023.day12.tail

fun main() {
    readFile("/input-day25.txt")
        .lines()
        .onEach(::println)
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
