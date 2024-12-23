package com.github.shmvanhouten.adventofcode2024.day23


fun listInterconnectedStartingWithT(input: String): List<List<String>> {
    val connectedComps = getAllCompConnections(input)
    return generateSequenceOfIncreasingGroupSizes(connectedComps)
        .drop(2)
        .first()
        .filter { it.any { c -> c.startsWith('t') } }
}

fun findPassword(input: String): String {
    val connectedComps = getAllCompConnections(input)
    return generateSequenceOfIncreasingGroupSizes(connectedComps)
        .takeWhile { it.isNotEmpty() }
        .last() // last in the sequence
        .single()
        .sorted().joinToString(",")
}

private fun generateSequenceOfIncreasingGroupSizes(connectedComps: Map<String, Set<String>>): Sequence<List<List<String>>> {
    val sequence = generateSequence(connectedComps.keys.map { listOf(it) }) { groups ->
        groups.flatMap { findNextConnectionConnectedToAll(connectedComps, it.toSet()) }.map { it.sorted() }.distinct()
    }
    return sequence
}

private fun findNextConnectionConnectedToAll(connectedComps: Map<String, Set<String>>, group: Set<String>): List<List<String>> {
    val filter = connectedComps[group.last()]!!.filter { it !in group }
    return filter
        .filter { lastConnection -> group.all { it in connectedComps[lastConnection]!! } }
        .map { group.toList() + it }
}

private fun getAllCompConnections(input: String): Map<String, Set<String>> {
    val comps = input.lines().map { it.split("-") }.map { (a, b) -> a to b }
    val connectedComps = (comps + comps.map { it.second to it.first }).groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }
    return connectedComps
}
