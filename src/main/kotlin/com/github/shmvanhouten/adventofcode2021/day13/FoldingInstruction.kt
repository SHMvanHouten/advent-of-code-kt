package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

data class FoldingInstruction(val foldingLine: FoldingLine, val n: Int) {
    fun applyFilter(coordinate: Coordinate): Boolean {
        return when (foldingLine) {
            FoldingLine.X -> coordinate.x >= n
            FoldingLine.Y -> coordinate.y >= n
        }
    }

    fun applyFold(coordinate: Coordinate): Coordinate {
        return when (foldingLine) {
            FoldingLine.X -> coordinate.copy(x = n - (coordinate.x - n))
            FoldingLine.Y -> coordinate.copy(y = n - (coordinate.y - n))
        }
    }
}

enum class FoldingLine {
    X, Y
}
