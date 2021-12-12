package com.github.shmvanhouten.adventofcode2021.day12

enum class RuleSet(val canVisitSmallCavesTwice: Boolean) {
    PART_1(false),
    PART_2(true);

    fun canVisit(node: Node, path: Path): Boolean {
        return if(!canVisitSmallCavesTwice) {
            node.isUpperCase() || !path.nodes.contains(node)
        } else {
            (node.isUpperCase()
                    || (node != START && !(path.hasVisitedSmallCaveTwice && path.nodes.contains(node))))
        }
    }
}

internal fun String.isUpperCase(): Boolean = this.first().isUpperCase()