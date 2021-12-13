package com.github.shmvanhouten.adventofcode2021.day13

data class FoldingInstruction(val foldingLine: FoldingLine, val n: Int)

enum class FoldingLine {
    X, Y
}
