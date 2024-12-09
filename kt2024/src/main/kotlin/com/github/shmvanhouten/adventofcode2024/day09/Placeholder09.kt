package com.github.shmvanhouten.adventofcode2024.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import kotlin.math.min

fun main() {
    val input = readFile("/input-day09.txt")
    println(input)
}

fun toFileBlocks(input: String): List<Block> {
    return input.mapIndexed{ index, c ->
        if(index.isEven()) {
            FileBlock(index / 2, c.digitToInt())
        } else {
            EmptyBlock(c.digitToInt())
        }

    }
 }

fun mergeToSingleBlock(fileBlocks: List<Block>): List<Id> {
    val justFileBlocks = fileBlocks.filterIsInstance<FileBlock>()
    val endBlocks = justFileBlocks.toMutableList()
    var remainingEndBlock = endBlocks.removeLast()
    return fileBlocks.flatMap {
        if(it is FileBlock) it.toIdList()
        else {
            var emptyPlaces = it.length
            val filledIn = mutableListOf<Id>()
            while (emptyPlaces > 0) {
                filledIn += remainingEndBlock.copy(length = min(emptyPlaces, remainingEndBlock.length)).toIdList()
                val remainingEmptyPlaces = emptyPlaces
                emptyPlaces -= remainingEndBlock.length
                remainingEndBlock = remainingEndBlock.copy(length = remainingEndBlock.length - remainingEmptyPlaces)
                if(remainingEndBlock.length <= 0) remainingEndBlock = endBlocks.removeLast()
            }
            filledIn
        }
    }.take(justFileBlocks.sumOf { it.length })
}

fun checkSum(blocks: List<Id>): Long {
    return blocks.mapIndexed { index, c -> index * c.toLong() }.sum()
}

sealed interface Block {val length: Int}
data class FileBlock(val id: Int, override val length: Int): Block {
    override fun toString(): String {
        return (0..<length).map { id }.joinToString("")
    }

    fun toIdList(): List<Id> {
        return (0..<length).map { id }
    }
}
data class EmptyBlock(override val length: Int): Block {
    override fun toString(): String {
        return (0..<length).map { '.' }.joinToString("")
    }
}

fun Int.isEven() = this % 2 == 0

typealias Id = Int
