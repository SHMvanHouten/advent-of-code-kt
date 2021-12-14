package com.github.shmvanhouten.adventofcode2021.day14

import com.github.shmvanhouten.adventofcode.utility.collectors.toMap

class InsertionRules(elementPairsToInsertElements: List<Pair<ElementPair, String>>) {

    private val rules: Map<ElementPair, Pair<ElementPair, ElementPair>> = elementPairsToInsertElements
        .associate { (elementPair: ElementPair, insertElement: String) ->
            elementPair to (elementPair[0] + insertElement to insertElement + elementPair[1])
        }

    fun applyTo(pairs: Map<ElementPair, Long>): Map<ElementPair, Long> {
        return pairs.map { (pair, count) -> rules[pair]!! to count }
            .flatMap { (twoPairs, count) -> listOf(twoPairs.first to count, twoPairs.second to count) }
            .toMap(Long::plus)
    }
}

typealias ElementPair = String