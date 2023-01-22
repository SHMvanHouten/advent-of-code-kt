package com.github.shmvanhouten.adventofcode2022.day08.grid

import com.github.shmvanhouten.adventofcode.utility.grid.Coord
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.intGridWithCoord

fun visibleTrees(input: String): List<Tree> {
    val grid = intGridWithCoord(input)
    return visibleTrees(grid)
}

fun visibleTrees(grid: Grid<Pair<Coord, Int>>): List<Tree> {
    return grid.map { Tree(it.first, it.second) }
        .filterTreesVisibleFromAnySide()
}

fun bestTreeScenicScore(input: String): Int {
    val grid = intGridWithCoord(input)
    return bestTreeScenicScore(grid.map { Tree(it.first, it.second) })
}

fun bestTreeScenicScore(grid: Grid<Tree>): Int {
    return grid.maxOf{ grid.scenicScore(it) }
}

private fun Grid<Tree>.filterTreesVisibleFromAnySide(): List<Tree> {
    // todo: make ComparableGrid to be able to make these transforms/filters more generic

    return grid.mapIndexed{ y, row ->
        row.filterIndexed { x, _ ->
            grid[y].positionIsVisible(x) || getColumn(x).positionIsVisible(y)
        }
    }.flatten()
}

private fun Grid<Tree>.scenicScore(location: Coord): Int {
    val (left, right) = this[location.y].visibleTreesAround(location.x)
    val (above, below) = this.getColumn(location.x).visibleTreesAround(location.y)
    return left * right * below * above
}

private fun List<Tree>.visibleTreesAround(i: Int): Pair<Int, Int> {
    return this.visibleTreesBefore(i) to this.visibleTreesAfter(i)
}

private fun List<Tree>.visibleTreesAfter(i: Int): Int {
    return if(i == lastIndex) 0
    else {
        val valueAtI = this[i]
        subList(i + 1, size).takeUntil { it < valueAtI }.size
    }
}

private fun List<Tree>.visibleTreesBefore(i: Int): Int {
    return if(i == 0) 0
    else {
        val valueAtI = this[i]
        subList(0 , i).reversed().takeUntil { it < valueAtI }.size
    }
}

private fun <E> List<E>.takeUntil(predicate: (E) -> Boolean): List<E> {
    val list = ArrayList<E>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}

private fun List<Tree>.positionIsVisible(i: Int): Boolean {
    val tree = this[i]
    return isAtEdge(i) || subList(0, i).all { it < tree } || subList(i + 1, this.size).all { it < tree }
}

private fun <E> List<E>.isAtEdge(i: Int): Boolean = i == 0 || i == lastIndex

data class Tree(val location: Coord, val height: Int) {
    operator fun compareTo(other: Tree): Int {
        return this.height.compareTo(other.height)
    }
}