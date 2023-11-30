package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.collections.joinToEvenlySpaced
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coord
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid.Companion.mutableGridOf

fun intGridFromSpaceDelimitedString(input: String) = Grid(input) { row -> row.split(' ').map { it.toInt() } }
fun intGridWithCoordinate(input: String) = Grid(input) { y, row ->
    row.mapIndexed { x, item -> Coordinate(x, y) to item.digitToInt() }
}
fun charGrid(input: String) = Grid(input, String::toList)
fun boolGridFromCoordinates(input: String): Grid<Boolean> {
    val locations = input.lines().map { Coordinate.parseFrom(it) }.toSet()
    if(locations.any { it.y < 0 || it.x< 0 } ) throw Error("grid with negative coordinates not implemented")
    val minX = 0
    val maxX = locations.maxOf { it.x }
    val minY = 0
    val maxY = locations.maxOf { it.y }
    val grid = (minY..maxY).map { y ->
        (minX..maxX).map { x -> locations.contains(Coordinate(x, y)) }
    }
    return Grid(grid)
}

fun boolGridFromPicture(input: String, targetChar: Char): Grid<Boolean> {
    val grid = input.lines().map { it.map { c -> c == targetChar }}
    return Grid(grid)
}

fun charGridFromPicture(input: String): Grid<Char> {
    return Grid(input.lines().map { it.toCharArray().toList() })
}

fun coordGrid(start: Coordinate, endExclusive: Coordinate): Grid<Coordinate> {
    return Grid(
        start.y.until(endExclusive.y)
            .map { y ->
                start.x.until(endExclusive.x).map { x -> Coordinate(x, y) }
            })
}

sealed interface IGrid<T, C: Coord> {
    fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(C) -> R): R
    fun <RESULT> map(transform: (T) -> RESULT): IGrid<RESULT, C>
    fun <RESULT> mapIndexed(function: (coord: C, element: T) -> RESULT): IGrid<RESULT, C>
    fun filterIndexed(function: (coord: C, element: T) -> Boolean): List<T>
    fun forEachIndexed(function: (coord: C, element: T) -> Unit)
    fun getOrNull(coord: C): T?

    operator fun get(coord: C): T

    fun hasElementAt(coord: C): Boolean
    fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): C?
    fun replaceElements(orig: T, replacement: T): Grid<T>
    fun count(condition: (T) -> Boolean): Int
    fun first(condition: (T) -> Boolean): T?
    fun filter(condition: (T) -> Boolean): List<T>
    fun any(condition: (T) -> Boolean): Boolean
    fun coordinatesMatching(condition: (T) -> Boolean): List<C>
    fun contains(coord: C): Boolean
    fun sumOfIndexed(function: (C, T) -> Long): Long
    fun withIndex(): IGrid<CoordinateIndexedValue<T, C>, C>
    fun surroundWith(element: T): IGrid<T, C>
}

open class Grid<T> (internal val grid: List<List<T>>) : IGrid<T, Coordinate> {

    constructor(input: String, mappingOperation: (String) -> List<T>) : this(input.lines()
        .map(mappingOperation)
        .map { it })

    constructor(input: String, mappingOperation: (index: Int, String) -> List<T>): this(input.lines()
        .mapIndexed (mappingOperation)
        .map { it })

    constructor(start: Coordinate, endExclusive: Coordinate, fn: (Coordinate) -> T): this(
        start.y.until(endExclusive.y)
            .map { y ->
                start.x.until(endExclusive.x).map { x -> fn(Coordinate(x, y)) }
            }
    )

    override fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(Coordinate) -> R): R {
        return (0 until height)
            .flatMap { y -> (0 until width).map { Coordinate(it, y) } }
            .maxOfOrNull{coord -> this.predicate(coord)}?: error("emtpy grid!")
    }

    override fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT> {
        return Grid(grid.map { it.map(transform) })
    }

    override fun <RESULT> mapIndexed(function: (coord: Coordinate, element: T) -> RESULT): Grid<RESULT> =
        grid.mapIndexed { y, row ->
            row.mapIndexed { x, t -> function(Coordinate(x, y), t) }
        }.let { Grid(it) }

    override fun filter(condition: (T) -> Boolean): List<T> {
        return grid.flatMap { row ->
            row.filter(condition)
        }
    }

    override fun filterIndexed(function: (coord: Coordinate, element: T) -> Boolean): List<T> =
        grid.mapIndexed { y, row ->
            row.filterIndexed { x, t -> function.invoke(Coordinate(x, y), t) }
        }.flatten()

    override fun any(condition: (T) -> Boolean): Boolean =
        grid.any { row -> row.any(condition) }

    override fun forEachIndexed(function: (coord: Coordinate, element: T) -> Unit) = grid
        .forEachIndexed { y, row ->
            row.forEachIndexed { x, t -> function(Coordinate(x, y), t) }
        }

    override fun getOrNull(coord: Coordinate): T? = grid.getOrNull(coord.y)?.getOrNull(coord.x)

    override operator fun get(coord: Coordinate): T {
        return grid[coord.y][coord.x]
    }

    operator fun get(x: Int, y: Int): T {
        return grid[y][x]
    }

    open operator fun get(y: Int): List<T> {
        return grid[y]
    }

    fun getColumn(x: Int): List<T> {
        return (0 until height).map { y -> this[x, y] }
    }

    override fun hasElementAt(coord: Coordinate): Boolean {
        return this.grid.getOrNull(coord.y)?.getOrNull(coord.x) != null
    }

    fun elementsSurroundingManhattan(x: Int, y: Int): List<T> {
        return Coordinate(x, y).getSurroundingManhattan()
            .map { this[it] }
    }

    override fun contains(coord: Coordinate): Boolean {
        return coord.y in 0.until(height)
                && coord.x in 0.until(width)
    }

    override fun sumOfIndexed(function: (Coordinate, T) -> Long): Long {
        return grid.mapIndexed { y, row ->
            row.mapIndexed { x, t ->
                function(Coordinate(x, y), t)
            }.sum()
        }.sum()
    }

    override fun first(condition: (T) -> Boolean): T? {
        this.grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, t ->
                if(condition(t)) return t
            }
        }
        return null
    }

    override fun coordinatesMatching(condition: (T) -> Boolean): List<Coordinate> {
        return this.grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, t -> if(condition(t)) Coordinate(x, y) else null }
        }
    }

    override fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): Coordinate? {
        grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, t ->
                    if(matchingFunction(t)) return Coordinate(x, y)
                }
            }
        return null
    }

    fun horizontalLineFrom(coord: Coordinate, length: Int): List<T> {
        return grid[coord.y].subList(coord.x, coord.x + length).toList()
    }

    fun verticalLineFrom(coord: Coordinate, length: Int): List<T> {
        return (coord.y until coord.y + length).map { this[coord.x, it] }
    }

    fun diagonalLineFrom(coord: Coordinate, length: Int): List<T> {
        return (0 until length).map { this[coord.x + it, coord.y + it] }
    }

    /**
     * for example:
     * ...#.
     * ..#..
     * .#...
     * #....
     */
    fun revDiagonalLineFrom(coord: Coordinate, length: Int): List<T> {
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
            (0 .. width - length).map{ x -> Coordinate(x, y) }
                .map { diagonalLineFrom(it, length) }
        }
    }

    fun windowedDiagonalReverse(length: Int): List<List<T>> {
        return (0 .. height - length).flatMap {y ->
            (length- 1 until width).map{ x -> Coordinate(x, y)}
                .map { revDiagonalLineFrom(it, length) }
        }
    }

    override fun withIndex(): Grid<CoordinateIndexedValue<T, Coordinate>> {
        return grid.mapIndexed { y, row ->
            row.mapIndexed { x, t ->
                CoordinateIndexedValue(Coordinate(x, y), t)
            }
        }.let { Grid(it) }
    }

    override fun surroundWith(element: T): Grid<T> {
        val line = listOf(List(width + 2) {element})
        return Grid(
            line +
            grid.map { row ->  listOf(element).plus(row) + element } +
            line
        )
    }

    override fun toString(): String {
        return toString("")
    }

    fun toString(delimiter: String): String {
        return grid.joinToString("\n"){ it.joinToString(delimiter) }
    }

    fun toStringEvenlySpaced(): String {
        val maxSpacingSize = grid.flatten().maxOfOrNull { it.toString().length }!! + 1
        return grid.joinToString("\n") {
            it.joinToEvenlySpaced(spaceSize = maxSpacingSize)
        }
    }

    override fun replaceElements(orig: T, replacement: T): Grid<T> {
        return this.grid.map{ line ->
            line.map{ t ->
                if(t == orig) replacement
                else t
            }
        }.let { Grid(it) }
    }

    override fun count(condition: (T) -> Boolean): Int {
        return grid.sumOf { it.count(condition) }
    }

    fun toMutableGrid(): MutableGrid<T> = mutableGridOf(grid)
    fun flatten(): List<T> {
        return grid.flatten()
    }

    val width: Int by lazy { grid.first().size }

    val height: Int by lazy { grid.size }

    companion object {

        fun <T> ofSize(width: Int, height: Int, element: T): Grid<T> {
            return Grid(List(height){ List(width) {element} })
        }

    }

}

data class CoordinateIndexedValue<T, C: Coord>(val location: C, val item: T)
