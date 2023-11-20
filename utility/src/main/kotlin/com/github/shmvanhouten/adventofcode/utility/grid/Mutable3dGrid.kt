package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d

class Mutable3dGrid<T>(
    private val mutableGrid: MutableList<MutableGrid<T>>
): Grid3d<T>(mutableGrid) {

    override operator fun get(z: Int): MutableGrid<T> {
        return mutableGrid[z]
    }

    operator fun set(coord: Coordinate3d, value: T) {
        this[coord.z][coord.on2dPlane] = value
    }

    operator fun set(x: Int, y: Int, z: Int, value: T) {
        set(Coordinate3d(x, y, z), value)
    }

    override fun surroundWith(element: T): Mutable3dGrid<T> {
        val grid2d = Grid.ofSize(width + 2, height + 2, element)
        mutableGrid.forEach{mg2d -> mg2d.surroundWith(element) }
        mutableGrid.add(0, grid2d.toMutableGrid())
        mutableGrid.add(grid2d.toMutableGrid())
        return Mutable3dGrid(this.mutableGrid)
    }

    companion object {
        fun <T> mutable3dGridOf(grid: List<Grid<T>>): Mutable3dGrid<T> {
            return Mutable3dGrid(grid.map { it.toMutableGrid() }.toMutableList())
        }
    }

}