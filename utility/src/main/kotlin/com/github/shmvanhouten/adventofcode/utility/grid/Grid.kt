package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.collections.joinToEvenlySpaced
import com.github.shmvanhouten.adventofcode.utility.grid.RelativePosition.*

fun intGridFromSpaceDelimitedString(input: String) = Grid(input) { row -> row.split(' ').map { it.toInt() } }
fun intGridWithCoord(input: String) = Grid(input) { y, row -> row.mapIndexed { x, item ->
    Coord(x, y) to item.digitToInt()
}}
fun charGrid(input: String) = Grid(input, String::toList)

class Grid<T> (private val grid: List<List<T>>){

    constructor(input: String, mappingOperation: (String) -> List<T>) : this(input.lines()
        .map(mappingOperation)
        .map { it })

    constructor(input: String, mappingOperation: (index: Int, String) -> List<T>): this(input.lines()
        .mapIndexed (mappingOperation)
        .map { it })

    fun <R: Comparable<R>> maxOf(predicate: Grid<T>.(Coord) -> R): R {
        return (0 until height)
            .flatMap { y -> (0 until width).map { Coord(it, y) } }
            .maxOfOrNull{coord -> this.predicate(coord)}?: error("emtpy grid!")
    }

    fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT> {
        return Grid(grid.map { it.map(transform) })
    }

    fun <RESULT> mapIndexed(function: (x: Int, y: Int, element: T) -> RESULT): Grid<RESULT> =
        grid.mapIndexed { y, row ->
            row.mapIndexed { x, t -> function(x, y, t) }
        }.let { Grid(it) }

    fun filterIndexed(function: (x: Int, y: Int, element: T) -> Boolean): List<T> =
        grid.mapIndexed { y, row ->
            row.filterIndexed { x, t -> function.invoke(x, y, t) }
        }.flatten()

    fun forEachIndexed(function: (x: Int, y: Int, element: T) -> Unit) = grid
        .forEachIndexed { y, row ->
            row.forEachIndexed { x, t -> function(x, y, t) }
        }

    fun getOrNull(coord: Coord): T? = grid.getOrNull(coord.y)?.getOrNull(coord.x)

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

    fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): Coord? {
        this.grid.forEachIndexed { y, line ->
            val x = line.indexOfFirst(matchingFunction::invoke)
            if(x >= 0) return Coord(x, y)
        }
        return null
    }

    fun replaceElements(orig: T, replacement: T): Grid<T> {
        return this.grid.map{ line ->
            line.map{ t ->
                if(t == orig) replacement
                else t
            }
        }.let { Grid(it) }
    }

    fun toMutableGrid(): MutableGrid<T> = grid.map { it.toMutableList() }.toMutableList()

    val width: Int by lazy { grid.first().size }

    val height: Int by lazy { grid.size }

}

typealias MutableGrid<T> = MutableList<MutableList<T>>

operator fun <T> MutableGrid<T>.set(coord: Coord, item: T) {
    this[coord.y][coord.x] = item
}

operator fun <T> MutableGrid<T>.get(coord: Coord): T {
    return this[coord.y][coord.x]
}

fun <T> MutableGrid<T>.hasElementAt(coord: Coord): Boolean {
    return this.getOrNull(coord.y)?.getOrNull(coord.x) != null
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
