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
            EmptyBlock(length = c.digitToInt())
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

fun defrag(fileBlocks: List<Block>): MutableList<Block> {
    val mutableBlocks =  fileBlocks.toMutableList()
    for (fb in fileBlocks.filterIsInstance<FileBlock>().reversed()) {
        val indexOfEmptySpace = mutableBlocks.indexOfFirst { it is EmptyBlock && it.length >= fb.length }
        if(mutableBlocks.indexOf(fb) < indexOfEmptySpace) continue
        if(indexOfEmptySpace == -1) continue
        mutableBlocks[mutableBlocks.indexOf(fb)] = EmptyBlock(length = fb.length)

        val emptyBlock = mutableBlocks[indexOfEmptySpace]
        if(emptyBlock.length == fb.length) {
            mutableBlocks[indexOfEmptySpace] = fb
        } else{
            mutableBlocks[indexOfEmptySpace] = EmptyBlock(length = emptyBlock.length - fb.length)
            mutableBlocks.add(indexOfEmptySpace, fb)
        }
    }
    return mutableBlocks
}

fun checkSum(blocks: List<Block>): Long {
    var index = 0
    var checkSum = 0L
    for (block in blocks) {
        for (i in (0..<block.length)) {
            checkSum += index * block.id
            index++
        }
    }
    return checkSum
}

fun checkSumIds(blocks: List<Id>): Long {
    return blocks.mapIndexed { index, c -> index * c.toLong() }.sum()
}

sealed interface Block {
    val id: Int
    val length: Int
}
data class FileBlock(override val id: Int, override val length: Int): Block {
    override fun toString(): String {
        return (0..<length).map { id }.joinToString("")
    }

    fun toIdList(): List<Id> {
        return (0..<length).map { id }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileBlock

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }


}
data class EmptyBlock(override val id: Int = 0, override val length: Int): Block {
    override fun toString(): String {
        return (0..<length).map { '.' }.joinToString("")
    }
}

fun Int.isEven() = this % 2 == 0

typealias Id = Int
