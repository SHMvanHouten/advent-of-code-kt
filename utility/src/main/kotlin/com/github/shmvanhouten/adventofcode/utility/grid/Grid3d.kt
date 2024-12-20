package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d

open class Grid3d<T>(
    private val grid: List<Grid<T>>
): IGrid<T, Coordinate3d> {

    companion object {
        fun from3dPicture(`2dGrids`: List<String>): Grid3d<Char> {
            return Grid3d(`2dGrids`.map { charGrid(it) })
        }

        fun <T> fromCoordinates(input: String, matching: T, missing: T): Grid3d<T> {
            if(input.contains('-')) throw Error("Cannot create grid with negative coordinates")
            val matchings = input.lines().map(Coordinate3d::parse).toSet()
            val maxZ = matchings.maxOf { it.z }
            val maxY = matchings.maxOf { it.y }
            val maxX = matchings.maxOf { it.x }
            return (0..maxZ).map { z ->
                (0..maxY).map { y ->
                    (0..maxX).map { x ->
                        val coord = Coordinate3d(x, y, z)
                        if(matchings.contains(coord)) matching
                        else missing
                    }
                }.let { Grid(it) }
            }.let { Grid3d(it) }
        }
    }

    override fun <R : Comparable<R>> maxOf(predicate: Grid<T>.(Coordinate3d) -> R): R {
        throw NotImplementedError("Please implement me")
    }

    override fun <RESULT> map(transform: (T) -> RESULT): Grid3d<RESULT> {
        throw NotImplementedError("Please implement me")
    }

    override fun <RESULT> mapIndexed(function: (coord: Coordinate3d, element: T) -> RESULT): Grid3d<RESULT> {
        return Grid3d(
            this.grid.mapIndexed { z, grid2d ->
                grid2d.mapIndexed { coord, element ->
                    function(coord.atDepth(z), element)
                }
            })
    }

    override fun filterIndexed(function: (coord: Coordinate3d, element: T) -> Boolean): List<T> {
        return grid.flatMapIndexed { z, grid2D ->
            grid2D.filterIndexed { coord, element ->
                function(coord.atDepth(z), element)
            }
        }
    }

    override fun any(condition: (T) -> Boolean): Boolean =
        grid.any { it.any(condition) }

    override fun forEachIndexed(function: (coord: Coordinate3d, element: T) -> Unit) {
        grid.forEachIndexed { z, grid2D ->
            grid2D.forEachIndexed { (x, y), element ->
                function(Coordinate3d(x, y, z), element)
            }
        }
    }

    override fun getOrNull(coord: Coordinate3d): T? {
        return grid.getOrNull(coord.z)?.getOrNull(coord.on2dPlane)
    }

    override operator fun get(coord: Coordinate3d): T {
        return this[coord.z][coord.on2dPlane]
    }

    override fun perimeter(): Sequence<T> {
        return sequence<T> {
            yieldAll(grid.first().flatten())
            grid.drop(1).take(grid.size - 2).forEach {
                yieldAll(it.perimeter())
            }
            yieldAll(grid.last().flatten())
        }
    }

    override fun isOnPerimiter(loc: Coordinate3d): Boolean {
        TODO()
    }

    operator fun get(x: Int, y: Int, z: Int): T {
        return get(Coordinate3d(x, y, z))
    }

    open operator fun get(z: Int): Grid<T> {
        return grid[z]
    }

    override fun hasElementAt(coord: Coordinate3d): Boolean {
        return grid.getOrNull(coord.z)?.getOrNull(coord.on2dPlane) != null
    }

    override fun firstLocationOf(matchingFunction: (T) -> Boolean): Coordinate3d? {
        grid.forEachIndexed{z, g2 ->
            val matching = g2.firstLocationOf(matchingFunction)
            if(matching != null) return matching.atDepth(z)
        }
        return null
    }

    override fun replaceElements(orig: T, replacement: T): Grid<T> {
        throw NotImplementedError("Please implement me")
    }

    override fun count(condition: (T) -> Boolean): Int {
        return grid.sumOf { it.count(condition) }
    }

    override fun first(condition: (T) -> Boolean): T? {
        return grid.firstNotNullOfOrNull { g2d -> g2d.first(condition) }
    }

    override fun first(): T = grid.first().first()

    override fun last(): T = grid.last().last()

    override fun filter(condition: (T) -> Boolean): List<T> {
        return grid.flatMap { it.filter(condition) }
    }

    override fun coordinatesMatching(condition: (T) -> Boolean): List<Coordinate3d> {
        throw NotImplementedError("Please implement me")
    }

    override fun contains(coord: Coordinate3d): Boolean {
        return coord.z in 0.until(depth)
                && coord.y in 0.until(height)
                && coord.x in 0.until(width)
    }

    override fun sumOfIndexed(function: (Coordinate3d, T) -> Long): Long {
        return grid.mapIndexed { z, grid2D ->
            grid2D.sumOfIndexed { (x, y), t ->
                function(Coordinate3d(x, y, z), t)
            }
        }.sum()
    }

    fun toMutable3dGrid(): Mutable3dGrid<T> {
        return Mutable3dGrid.mutable3dGridOf(grid)
    }

    override fun withIndex(): Grid3d<CoordinateIndexedValue<T, Coordinate3d>> {
        return this.mapIndexed { coord, element ->
            CoordinateIndexedValue(coord, element)
        }
    }

    override fun surroundWith(element: T): Grid3d<T> {
        val grid2d = listOf(Grid.ofSize(width + 2, height + 2, element))
        return Grid3d(
            grid2d
            + grid.map { it.surroundWith(element) }
            + grid2d
        )
    }

    fun firstCoordinateMatchingIndexed(condition: (Coordinate3d, T) -> Boolean): Coordinate3d? {
        grid.forEachIndexed { z, grid ->
            grid.grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, element ->
                    val loc = Coordinate3d(x, y, z)
                    if(condition(loc, element)) return loc
                }
            }
        }
        return null
    }

    val width: Int by lazy { grid.first().width }

    val height: Int by lazy { grid.first().height}

    val depth: Int by lazy { grid.size }

}