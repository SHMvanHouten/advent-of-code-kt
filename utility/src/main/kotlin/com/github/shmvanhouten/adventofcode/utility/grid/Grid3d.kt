package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d

open class Grid3d<T>(
    private val grid: List<Grid<T>>
): IGrid<T, Coordinate3d> {

    companion object {
        fun from3dPicture(`2dGrids`: List<String>): Grid3d<Char> {
            return Grid3d(`2dGrids`.map { charGridFromPicture(it) })
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

    override fun <RESULT> map(transform: (T) -> RESULT): Grid<RESULT> {
        throw NotImplementedError("Please implement me")
    }

    override fun <RESULT> mapIndexed(function: (coord: Coordinate3d, element: T) -> RESULT): Grid<RESULT> {
        throw NotImplementedError("Please implement me")
    }

    override fun filterIndexed(function: (coord: Coordinate3d, element: T) -> Boolean): List<T> {
        throw NotImplementedError("Please implement me")
    }

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

    operator fun get(x: Int, y: Int, z: Int): T {
        return get(Coordinate3d(x, y, z))
    }

    open operator fun get(z: Int): Grid<T> {
        return grid[z]
    }

    override fun hasElementAt(coord: Coordinate3d): Boolean {
        return grid.getOrNull(coord.z)?.getOrNull(coord.on2dPlane) != null
    }

    override fun firstCoordinateMatching(matchingFunction: (T) -> Boolean): Coordinate3d? {
        throw NotImplementedError("Please implement me")
    }

    override fun replaceElements(orig: T, replacement: T): Grid<T> {
        throw NotImplementedError("Please implement me")
    }

    override fun count(condition: (T) -> Boolean): Int {
        return grid.sumOf { it.count(condition) }
    }

    override fun first(condition: (T) -> Boolean): CoordinateIndexedValue<T, Coordinate3d>? {
        return grid.withIndex().firstNotNullOf { (z, g2d) -> g2d.first(condition)?.atDepth(z) }
    }

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
        return Mutable3dGrid(grid)
    }

    override fun withIndex(): IGrid<CoordinateIndexedValue<T, Coordinate3d>, Coordinate3d> {
        return grid.mapIndexed { z, grid2D ->
            grid2D.mapIndexed { (x, y), element ->
                CoordinateIndexedValue(Coordinate3d(x, y, z), element)
            }
        }.let { Grid3d(it) }
    }

    val width: Int by lazy { grid.first().width }

    val height: Int by lazy { grid.first().height}

    val depth: Int by lazy { grid.size }

}