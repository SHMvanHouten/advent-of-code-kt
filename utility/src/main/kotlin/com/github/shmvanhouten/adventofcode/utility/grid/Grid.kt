package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.collections.joinToEvenlySpaced
import com.github.shmvanhouten.adventofcode.utility.grid.RelativePosition.*

fun intGridFromSpaceDelimitedString(input: String) = Grid(input) { row -> row.split(' ').map { it.toInt() } }
fun intGridWithCoord(input: String) = Grid(input) { y, row -> row.mapIndexed { x, item ->
    Coord(x, y) to item.digitToInt()
}}
fun charGrid(input: String) = Grid(input, String::toList)
fun boolGridFromCoordinates(input: String): Grid<Boolean> {
    val locations = input.lines().map { Coord.parseFrom(it) }.toSet()
    if(locations.any { it.y < 0 || it.x< 0 } ) throw Error("grid with negative coordinates not implemented")
    val minX = 0
    val maxX = locations.maxOf { it.x }
    val minY = 0
    val maxY = locations.maxOf { it.y }
    val grid = (minY..maxY).map { y ->
        (minX..maxX).map { x -> locations.contains(Coord(x, y)) }
    }
    return Grid(grid)
}

fun boolGridFromPicture(input: String, targetChar: Char): Grid<Boolean> {
    val grid = input.lines().map { it.map { c -> c == targetChar }}
    return Grid(grid)
}

sealed interface IGrid<T> {
    fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(Coord) -> R): R
    fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT>
    fun <RESULT> mapIndexed(function: (x: Int, y: Int, element: T) -> RESULT): Grid<RESULT>
    fun filterIndexed(function: (x: Int, y: Int, element: T) -> Boolean): List<T>
    fun forEachIndexed(function: (x: Int, y: Int, element: T) -> Unit)
    fun getOrNull(coord: Coord): T?

    operator fun get(coord: Coord): T
    operator fun get(x: Int, y: Int): T
    operator fun get(y: Int): List<T>

    fun getColumn(x: Int): List<T>
    fun hasElementAt(coord: Coord): Boolean
    fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): Coord?
    fun replaceElements(orig: T, replacement: T): Grid<T>
    fun count(condition: (T) -> Boolean): Int
    fun first(condition: (T) -> Boolean): ItemWithLocation<T>?
    fun filter(condition: (T) -> Boolean): List<T>
    fun coordinatesMatching(condition: (T) -> Boolean): List<Coord>
    fun elementsSurroundingManhattan(x: Int, y: Int): List<T>
    fun contains(coord: Coord): Boolean
    fun sumOfIndexed(function: (Coord, T) -> Long): Long
}

open class Grid<T> (internal val grid: List<List<T>>) : IGrid<T> {

    constructor(input: String, mappingOperation: (String) -> List<T>) : this(input.lines()
        .map(mappingOperation)
        .map { it })

    constructor(input: String, mappingOperation: (index: Int, String) -> List<T>): this(input.lines()
        .mapIndexed (mappingOperation)
        .map { it })

    override fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(Coord) -> R): R {
        return (0 until height)
            .flatMap { y -> (0 until width).map { Coord(it, y) } }
            .maxOfOrNull{coord -> this.predicate(coord)}?: error("emtpy grid!")
    }

    override fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT> {
        return Grid(grid.map { it.map(transform) })
    }

    override fun <RESULT> mapIndexed(function: (x: Int, y: Int, element: T) -> RESULT): Grid<RESULT> =
        grid.mapIndexed { y, row ->
            row.mapIndexed { x, t -> function(x, y, t) }
        }.let { Grid(it) }

    override fun filter(condition: (T) -> Boolean): List<T> {
        return grid.flatMap { row ->
            row.filter(condition)
        }
    }

    override fun filterIndexed(function: (x: Int, y: Int, element: T) -> Boolean): List<T> =
        grid.mapIndexed { y, row ->
            row.filterIndexed { x, t -> function.invoke(x, y, t) }
        }.flatten()

    override fun forEachIndexed(function: (x: Int, y: Int, element: T) -> Unit) = grid
        .forEachIndexed { y, row ->
            row.forEachIndexed { x, t -> function(x, y, t) }
        }

    override fun getOrNull(coord: Coord): T? = grid.getOrNull(coord.y)?.getOrNull(coord.x)

    override operator fun get(coord: Coord): T {
        return grid[coord.y][coord.x]
    }

    override operator fun get(x: Int, y: Int): T {
        return grid[y][x]
    }

    override operator fun get(y: Int): List<T> {
        return grid[y]
    }

    override fun getColumn(x: Int): List<T> {
        return (0 until height).map { y -> this[x, y] }
    }

    override fun hasElementAt(coord: Coord): Boolean {
        return this.grid.getOrNull(coord.y)?.getOrNull(coord.x) != null
    }

    override fun elementsSurroundingManhattan(x: Int, y: Int): List<T> {
        return Coord(x, y).getSurroundingManhattan()
            .map { this[it] }
    }

    override fun contains(coord: Coord): Boolean {
        return coord.y in 0.until(height)
                && coord.x in 0.until(width)
    }

    override fun sumOfIndexed(function: (Coord, T) -> Long): Long {
        return grid.mapIndexed { y, row ->
            row.mapIndexed { x, t ->
                function(Coord(x, y), t)
            }.sum()
        }.sum()
    }

    override fun first(condition: (T) -> Boolean): ItemWithLocation<T>? {
        this.grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, t ->
                if(condition(t)) return ItemWithLocation(t, Coord(x, y))
            }
        }
        return null
    }

    override fun coordinatesMatching(condition: (T) -> Boolean): List<Coord> {
        return this.grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, t -> if(condition(t)) Coord(x, y) else null }
        }
    }

    override fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): Coord? {
        return first(matchingFunction)?.location
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

    fun toMutableGrid(): MutableGrid<T> = MutableGrid(grid)

    val width: Int by lazy { grid.first().size }

    val height: Int by lazy { grid.size }

}

data class Coord(val x: Int, val y: Int) {

    operator fun plus(otherCoord: Coord): Coord {
        val x = this.x + otherCoord.x
        val y = this.y + otherCoord.y
        return Coord(x, y)
    }

    fun getSurroundingManhattan(): Set<Coord> {
        return setOf(
            this + NORTH.coordinate,
            this + EAST.coordinate,
            this + SOUTH.coordinate,
            this + WEST.coordinate,
        )
    }

    fun move(direction: RelativePosition): Coord {
        return this + direction.coordinate
    }

    companion object {
        fun parseFrom(raw: String): Coord {
            val (x, y) = raw.split(",")
                .map { it.toInt() }
            return Coord(x, y)
        }
    }
}

enum class RelativePosition(val coordinate: Coord) {
    NORTH(Coord(0, -1)),
    NORTH_EAST(Coord(1, -1)),
    EAST(Coord(1, 0)),
    SOUTH_EAST(Coord(1, 1)),
    SOUTH(Coord(0, 1)),
    SOUTH_WEST(Coord(-1, 1)),
    WEST(Coord(-1, 0)),
    NORTH_WEST(Coord(-1, -1))
}

data class ItemWithLocation<T>(val item: T, val location: Coord)
