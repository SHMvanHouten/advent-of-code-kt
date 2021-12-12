package com.github.shmvanhouten.adventofcode2021.day12

data class Path(val nodes: List<Node>, val hasVisitedSmallCaveTwice: Boolean = false, private val rules: RuleSet) {

    fun lastCave(): Node {
        return nodes.last()
    }

    fun canVisit(node: Node) = rules.canVisit(node, this)

    operator fun plus(node: Node): Path {
        return this.copy(
            nodes = nodes + node,
            hasVisitedSmallCaveTwice = hasVisitedSmallCaveTwice
                    || isVisitingSmallCaveForTheSecondTime(node)
        )

    }

    private fun isVisitingSmallCaveForTheSecondTime(cave: Node) =
        rules.canVisitSmallCavesTwice && cave.isASmallCave() && nodes.contains(cave)

    fun isComplete() = nodes.last() == END

    override fun toString(): String {
        return nodes.joinToString(",")
    }

    fun hasNotVisitedCave(cave: Node) = !nodes.contains(cave)
}

private fun Node.isASmallCave(): Boolean = this.isLowerCase()

private fun String.isLowerCase(): Boolean {
    return this.all { it.isLowerCase() }
}
