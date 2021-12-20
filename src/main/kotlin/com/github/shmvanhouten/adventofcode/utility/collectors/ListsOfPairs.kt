package com.github.shmvanhouten.adventofcode.utility.collectors


/**
 * Regular List<Pair<F,S>>.toMap() drops any clashing keys,
 * this method requires a mergeFunction that will calculates a value
 * in the case of a key clash.
 *
 * @param mergeFunction function to apply in case an entry already exists for the key
 */
fun <FIRST, SECOND: Any> Collection<Pair<FIRST, SECOND>>.toMap(mergeFunction: (SECOND, SECOND) -> SECOND): Map<FIRST, SECOND> {
    val map = mutableMapOf<FIRST, SECOND>()
    this.forEach { (first, second) -> map.merge(first, second, mergeFunction) }
    return map.toMap()
}

fun <FIRST, SECOND> Collection<Pair<FIRST, SECOND>>.fracture(): Pair<List<FIRST>, List<SECOND>> {
    return this.map { it.first } to this.map { it.second }
}