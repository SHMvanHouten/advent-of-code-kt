package com.github.shmvanhouten.adventofcode2022.day08.grid

import com.github.shmvanhouten.adventofcode.utility.collections.allAfter
import com.github.shmvanhouten.adventofcode.utility.collections.allBefore
import com.github.shmvanhouten.adventofcode.utility.grid.Coord
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.intGridWithCoord

fun visibleTrees(input: String): List<Tree> {
    val grid = intGridWithCoord(input)
    return visibleTrees(grid)
}

fun visibleTrees(grid: Grid<Pair<Coord, Int>>): List<Tree> {
    return grid
        .map { (location, height) -> Tree(location, height) }
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

    return this.filterIndexed { x, y, _ ->
            this[y].positionIsVisible(x) || getColumn(x).positionIsVisible(y)
        }
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
    return isAtEdge(i) || this.allBefore(i) { it < tree } || allAfter(i) { it < tree }
}

private fun <E> List<E>.isAtEdge(i: Int): Boolean = i == 0 || i == lastIndex

data class Tree(val location: Coord, val height: Int) {
    operator fun compareTo(other: Tree): Int {
        return this.height.compareTo(other.height)
    }
}