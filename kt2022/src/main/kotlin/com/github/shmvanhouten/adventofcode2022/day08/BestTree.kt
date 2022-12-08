package com.github.shmvanhouten.adventofcode2022.day08

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

private val Map<Coordinate, Int>.maxX: Int
    get() {
        return this.keys.map { it.x }.max()
    }

private val Map<Coordinate, Int>.maxY: Int
    get() {
        return this.keys.map { it.y }.max()
    }

fun findBestTree(trees: Map<Coordinate, Int>): Int {
    val maxX = trees.maxX
    val maxY = trees.maxY
    return trees
        .map { calculateValue(it.key, it.value, trees, maxX, maxY) }
        .max()
}

fun calculateValue(
    location: Coordinate,
    treeValue: Int, trees: Map<Coordinate, Int>,
    maxX: Int,
    maxY: Int
): Int {
    val amountOfTreesVisibleLeft = if(location.x == 0) 0
    else(location.x - 1).downTo(1).asSequence()
        .map { trees[Coordinate(it, location.y)]!! }
        .takeWhile { it < treeValue }.count() + 1

    val amountOfTreesVisibleRight = if(location.x == maxX) 0
        else ((location.x + 1).until(maxX)).asSequence()
        .map { trees[Coordinate(it, location.y)]!! }
        .takeWhile { it < treeValue }.count() + 1

    val amountOfTreesVisibleUp = if (location.y == 0) 0
    else (location.y - 1).downTo(1).asSequence()
        .map { trees[Coordinate(location.x, it)]!! }
        .takeWhile { it < treeValue }.count() + 1

    val amountOfTreesVisibleDown = if (location.y == maxY) 0
    else((location.y + 1) until maxY).asSequence()
        .map { trees[Coordinate(location.x, it)]!! }
        .takeWhile { it < treeValue }.count() + 1
    return amountOfTreesVisibleLeft * amountOfTreesVisibleRight * amountOfTreesVisibleDown * amountOfTreesVisibleUp
}
