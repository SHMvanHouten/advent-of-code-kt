package com.github.shmvanhouten.adventofcode2021.day12

enum class RuleSet(val canVisitSmallCavesTwice: Boolean) {
    PART_1(false),
    PART_2(true);

    fun canVisit(cave: Node, path: Path): Boolean {
        return cave.isALargeCave()
                || (!canVisitSmallCavesTwice && path.hasNotVisitedCave(cave))
                || canVisitSmallCavesTwice && cave != START && (!path.hasVisitedSmallCaveTwice || path.hasNotVisitedCave(cave))
    }

}

private fun Node.isALargeCave() = this.isUpperCase()
internal fun String.isUpperCase(): Boolean = this.all { it.isUpperCase() }