package com.github.shmvanhouten.adventofcode2022.day08

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

fun forEachOrientationPrint(input: String) {
    println(input)
    println("rotatedLeft:")
    println(rotatedLeft(input))
    println("mirrored:")
    println(mirror(input))
    println("rotatedRight")
    println(rotatedLeft(mirror(input)))
}

fun treesVisible(input: String): Set<Coordinate> {
    val maxX = input.lines().first().lastIndex
    val maxY = input.lines().lastIndex
    return treesVisibleFromThisDirection(input) +
            treesVisibleFromThisDirection(rotatedLeft(input)).rotateCoordinatesRight(maxX, maxY) +
            treesVisibleFromThisDirection(mirror(input)).mirrorCoordinates(maxX, maxY) +
            treesVisibleFromThisDirection(rotatedLeft(mirror(input))).rotateCoordinatesLeft(maxX, maxY)
}

fun mirror(input: String): String {
    return input.lines().map { it.reversed() }.reversed().joinToString("\n")
}

private fun Set<Coordinate>.rotateCoordinatesRight(maxX: Int, maxY: Int): Set<Coordinate> {
    return map { Coordinate(maxY - it.y, it.x) }.toSet()
}

private fun Set<Coordinate>.mirrorCoordinates(maxX: Int, maxY: Int): Set<Coordinate> {
    return map { Coordinate(maxX - it.x, maxY - it.y) }.toSet()
}


private fun Set<Coordinate>.rotateCoordinatesLeft(maxX: Int, maxY: Int): Set<Coordinate> {
    return map { Coordinate(it.y,maxY - it.x) }.toSet()
}

fun treesVisibleFromThisDirection(input: String): Set<Coordinate> {

    return input.lines().flatMapIndexed {y:Int, trees: String ->
        findVisibleTreeX(trees)
            .map { Coordinate(it, y) }
    }.toSet()
}

fun findVisibleTreeX(treeLine: String): List<Int> {
    var largestTree = -1
    val visibleTreesIndex = mutableListOf<Int>()
    treeLine.mapIndexed { index, tree -> index to tree.digitToInt() }
        .forEach { (i, tree: Int) ->
            if(tree > largestTree) {
                visibleTreesIndex.add(i)
                largestTree = tree
            }
            if(tree == 9) return visibleTreesIndex
        }
    return visibleTreesIndex
}

private fun rotatedLeft(value: String): String {
    return (value.lines().first().lastIndex.downTo(0))
        .joinToString("\n") { charPosition ->
            (0..value.lines().lastIndex)
                .map { lineIndex -> value.lines()[lineIndex] }
                .map { line -> line[charPosition] }
                .joinToString("")
        }
}