package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

class MutableGrid<T>(
    private val mutableGrid: MutableList<MutableList<T>>
) : Grid<T>(grid = mutableGrid) {

    override operator fun get(y: Int): MutableList<T> {
        return mutableGrid[y]
    }

    operator fun set(coord: Coordinate, value: T) {
        mutableGrid[coord.y][coord.x] = value
    }

    operator fun set(x: Int, y: Int, value: T) {
        mutableGrid[y][x] = value
    }

    override fun surroundWith(element: T): MutableGrid<T> {
        val newRow = List(width + 2) { element }
        mutableGrid.forEach { row ->
            row.add(0, element)
            row.add(element)
        }
        mutableGrid.add(0, newRow.toMutableList())
        mutableGrid.add(newRow.toMutableList())

        return MutableGrid(mutableGrid) // reset width and height
    }

    companion object {
        fun <T> mutableGridOf(grid: List<List<T>>): MutableGrid<T> {
            return MutableGrid(grid.map { it.toMutableList() }.toMutableList())
        }
    }

}