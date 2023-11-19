package com.github.shmvanhouten.adventofcode.utility.grid

class MutableGrid<T>(
    grid: List<List<T>>,
    private val mutableGrid: MutableList<MutableList<T>> = grid.map { it.toMutableList() }.toMutableList()
) : Grid<T>(grid = mutableGrid) {

    override operator fun get(y: Int): MutableList<T> {
        return mutableGrid[y]
    }

    operator fun set(coord: Coord, value: T) {
        mutableGrid[coord.y][coord.x] = value
    }

    operator fun set(x: Int, y: Int, value: T) {
        mutableGrid[y][x] = value
    }

}