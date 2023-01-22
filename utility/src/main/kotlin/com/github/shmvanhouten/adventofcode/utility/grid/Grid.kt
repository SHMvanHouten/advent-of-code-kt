package com.github.shmvanhouten.adventofcode.utility.grid

fun spaceDelimitedIntGrid(input: String) = Grid(input) {  row -> row.split(' ').map { it.toInt() } }
fun intGridWithCoord(input: String) = Grid(input) { y, row -> row.mapIndexed { x, item ->
    Coord(x, y) to item.digitToInt()
}}

class Grid<T> (val grid: MutableList<MutableList<T>>){

    constructor(input: String, mappingOperation: (String) -> List<T>) : this(input.lines()
        .map(mappingOperation)
        .map { it.toMutableList() }.toMutableList())

    constructor(input: String, mappingOperation: (index: Int, String) -> List<T>): this(input.lines()
        .mapIndexed (mappingOperation)
        .map { it.toMutableList() }
        .toMutableList())

    fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(Coord) -> R): R {
        return (0 until height)
            .flatMap { y -> (0 until width).map { Coord(it, y) } }
            .maxOfOrNull{coord -> this.predicate(coord)}?: error("emtpy grid!")
    }

    fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT> {
        return Grid(grid.map { it.map(transform).toMutableList() }.toMutableList())
    }

    fun filter(function: (T) -> Boolean): List<T> {
        grid.map {  }
        return grid.flatten().filter(function)
    }

    operator fun get(x: Int, y: Int): T {
        return grid[y][x]
    }

    operator fun get(y: Int): List<T> {
        return grid[y]
    }

    fun getColumn(x: Int): List<T> {
        return (0 until height).map { y -> this[x, y] }
    }

    fun horizontalLineFrom(coord: Coord, length: Int): List<T> {
        return grid[coord.y].subList(coord.x, coord.x + length).toList()
    }

    fun verticalLineFrom(coord: Coord, length: Int): List<T> {
        return (coord.y until coord.y + length).map { this[coord.x, it] }
    }

    fun diagonalLineFrom(coord: Coord, length: Int): List<T> {
        return (0 until length).map { this[coord.x + it, coord.y + it] }
    }

    /**
     * for example:
     * ...#.
     * ..#..
     * .#...
     * #....
     */
    fun revDiagonalLineFrom(coord: Coord, length: Int): List<T> {
        return (0 until length).map { this[coord.x - it, coord.y + it] }
    }

    fun allWindowed(length: Int): List<List<T>> {
        return windowedHorizontal(length) +
                windowedVertical(length) +
                windowedDiagonal(length) +
                windowedDiagonalReverse(length)

    }

    fun windowedHorizontal(length: Int, step: Int = 1, partialWindows: Boolean = false): List<List<T>> {
        return (0 until height).flatMap { y ->
            this[y].windowed(length, step, partialWindows)
        }
    }

    fun windowedVertical(length: Int, step: Int = 1, partialWindows: Boolean = false): List<List<T>> {
        return (0 until width).flatMap { x ->
            this.getColumn(x).windowed(length, step, partialWindows)
        }
    }

    fun windowedDiagonal(length: Int): List<List<T>> {
        return (0 .. height - length).flatMap {y ->
            (0 .. width - length).map{ x -> Coord(x, y)}
                .map { diagonalLineFrom(it, length) }
        }
    }

    fun windowedDiagonalReverse(length: Int): List<List<T>> {
        return (0 .. height - length).flatMap {y ->
            (length- 1 until width).map{ x -> Coord(x, y)}
                .map { revDiagonalLineFrom(it, length) }
        }
    }

    val width: Int by lazy { grid.first().size }

    val height: Int by lazy { grid.size }

}

data class Coord(val x: Int, val y: Int)