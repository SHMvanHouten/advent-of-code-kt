package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d

class Mutable3dGrid<T>(
    grid: List<Grid<T>>,
    private val mutableGrid: MutableList<MutableGrid<T>> = grid.map { it.toMutableGrid() }.toMutableList()
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

}