package com.github.shmvanhouten.adventofcode2021.day12

data class Path(val nodes: List<Node>, val hasVisitedSmallCaveTwice: Boolean = false, private val rules: RuleSet) {

    fun lastCave(): Node {
        return nodes.last()
    }

    fun canVisit(node: Node) = rules.canVisit(node, this)

    operator fun plus(node: Node): Path {
        return if(!rules.canVisitSmallCavesTwice || node.isUpperCase() || hasVisitedSmallCaveTwice) {
            this.copy(nodes = nodes + node)
        } else {
            this.copy(nodes = nodes + node, hasVisitedSmallCaveTwice = nodes.contains(node))
        }
    }

    fun isComplete() = nodes.last() == END

    override fun toString(): String {
        return nodes.joinToString(",")
    }

}
