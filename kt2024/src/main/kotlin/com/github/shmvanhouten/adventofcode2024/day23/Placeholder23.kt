package com.github.shmvanhouten.adventofcode2024.day23


fun listInterconnectedStartingWithT(input: String): List<List<String>> {
    val findInterconnected = findInterconnected(input).map { it.sorted() }.distinct()
    return findInterconnected
        .filter{ it.any {c -> c.startsWith('t')} }
}

fun findInterconnected(input: String): List<List<String>> {
    val comps = input.lines().map { it.split("-") }.map { (a, b) -> a to b }
    val connectedComps = (comps + comps.map { it.second to it.first }).groupBy ({ it.first }, {it.second}).mapValues { it.value.toSet() }
    return connectedComps.keys.flatMap { f -> connectedComps[f]!!.map { listOf(f, it) } }
        .mapNotNull { twoConnected ->
            val nextConnections = connectedComps[twoConnected.last()]!!
            val connectionsConnectedToFirst = nextConnections.filter { twoConnected.first() in connectedComps[it]!! }
            if(connectionsConnectedToFirst.isEmpty()) {
                null
            } else {
                connectionsConnectedToFirst.map { twoConnected + it }
            }
        }.flatten()
}
