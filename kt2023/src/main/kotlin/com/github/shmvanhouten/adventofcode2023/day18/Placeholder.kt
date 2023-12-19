package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn.LEFT
import com.github.shmvanhouten.adventofcode.utility.coordinate.Turn.RIGHT
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGridFromCoordinates
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween
import com.github.shmvanhouten.adventofcode.utility.strings.words
import kotlin.math.abs

fun main() {
    readFile("/input-day18.txt")
        .lines()
        .onEach(::println)
//        .let { dig(it) }.also(::println)
}

fun toInstruction(line: String): Instruction {
    val (turn, steps) = line.words()
    val direction = when(turn) {
        "R" -> EAST
        "L" -> WEST
        "U" -> NORTH
        "D" -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, steps.toInt())
}

fun digHex(hexInstructions: String): Long {
    val trench = hexInstructions.lines()
        .map { toInstructionHex(it) }
//        .onEach { println(it) }
    return dig(trench)
}

fun toInstructionHex(line: String): Instruction {
    val ins = line.substringBetween("(#", ")")
    val direction = when(ins.last()) {
        '0' -> EAST
        '2' -> WEST
        '3' -> NORTH
        '1' -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, ins.substring(0, ins.lastIndex).toInt(16))
}

fun dig(instructions: String): Long = dig(instructions.lines().map { toInstruction(it) })

fun dig(instructions: List<Instruction>): Long {
    val trench = getTrench(instructions)

    val trenches = trench.map { it.first }
    return shoeLace(trenches) + (trenches.circumference() / 2)
}

private fun List<Coordinate>.circumference(): Long {
    return this.windowed(2) {(first, second) -> (first..second).count().toLong() - 1}.sum() + 2 // +2 for start and end
}

fun shoeLace(trenches: List<Coordinate>): Long {
    val (first, second) = trenches.windowed(2)
        .map { (first, second) -> first.x.toLong() * second.y.toLong() to first.y.toLong() * second.x.toLong() }.unzip()
    val abs = abs(first.sum() - second.sum())
    return abs / 2
}


fun calculateSurfaceArea(instructions: List<Instruction>): Long {
    val trench = instructions.runningFold(Coordinate(0, 0)) { loc, ins ->  loc.move(ins.direction, ins.distance)}

    val finishedBoxes = mutableSetOf<Box>()
    val boxes = mutableSetOf<Box>()
    trench.windowed(2).forEach {(left, right) ->
        val range = left..right
        if(boxes.isEmpty()) boxes += Box.start(range)
        else {
            val connectedBoxes = range.connectAndRemoveBox(boxes)
            val (completed, incomplete) = connectedBoxes.partition{it.isCompletable(boxes)}
            completed.forEach{ box ->
                finishedBoxes += box
            }
            boxes += incomplete
        }
    }
    return finishedBoxes.sumOf { it.surface() }
}

private fun CoordinateProgression.connectAndRemoveBox(boxes: MutableSet<Box>): List<Box> {
    val matching = boxes.single { it.connectsTo(this) }
    boxes.removeIf { it.connectsTo(this) }
    return matching.add(this)
}

data class Box(
    val horizontalLines: MutableList<CoordinateProgression> = mutableListOf(),
    val verticalLines: MutableList<CoordinateProgression> = mutableListOf(),
    private val start: Coordinate,
    var end: Coordinate,
) {
    private val locationBeforeStart: Coordinate by lazy {
        val coordinateProgression =
            horizontalLines.firstOrNull { it.start == start } ?: verticalLines.first { it.start == start }
        coordinateProgression.getAdjacentPos()
    }

    fun size(): Int = horizontalLines.size + verticalLines.size

    fun connectsTo(line: CoordinateProgression): Boolean {
        return this.end == line.start
    }

    fun add(newLine: CoordinateProgression): List<Box> {
        if(newLine.isHorizontal()) {
            val existingLine = horizontalLines.firstOrNull()
            if(existingLine == null || newLine.size == existingLine.size && existingLine.direction != newLine.direction) {
                check(existingLine == null || newLine.start.x == existingLine.end.x) // top line must start where bottom ended or vice versa
                horizontalLines += newLine
                end = newLine.end
                return listOf(this)
            } else if(existingLine.direction == newLine.direction) {
                return listOf(this, Box.start(newLine.tail()))
            }else if(newLine.size > existingLine.size) {
                if(existingLine.size + 1 == newLine.size) {// finishing an "attachment"
                    val cutLine = newLine.start..newLine.beforeLast()
                    horizontalLines += cutLine
                    end = cutLine.end
                    return listOf(this)
                } else {
                    val (matchingRange, newRange) = newLine.cutAtX(existingLine.start.x)
                    horizontalLines += matchingRange
                    return listOf(
                        this.copy(end = matchingRange.end),
                        Box.start(newRange)
                    )
                }
            }
            else {
                horizontalLines += newLine
                end = newLine.end
                return listOf(this)
            }
        } else {
            val existingLine = verticalLines.firstOrNull()
            if(verticalLines.any { it.end == newLine.start }) {
                val oldLine = verticalLines.first { it.end == newLine.start }
                verticalLines.remove(oldLine)
                verticalLines.add(oldLine.start..newLine.end)
                end = newLine.end
                return listOf(this)
            } else if(existingLine == null || newLine.size == existingLine.size || horizontalLines.size == 2) {
                verticalLines += newLine
                end = newLine.end
                return listOf(this)
            } else if(existingLine.size > newLine.size) {
                if(existingLine.size + 1 == newLine.size) {// finishing an "attachment"
//                    val cutLine = newLine.start..newLine.beforeLast()
//                    horizontalLines += cutLine
//                    end = cutLine.end
//                    return listOf(this)
                    TODO()
                } else {
                    val (matchingRange, newRange) = newLine.cutAtY(existingLine.start.y)
                    verticalLines += matchingRange
                    return listOf(
                        this.copy(end = matchingRange.end),
                        Box.start(newRange)
                    )
                }
            } else {
                TODO()
            }
        }
    }

    fun isComplete(): Boolean = this.size() == 4
    fun surface(): Long {
        check(horizontalLines.first().count() == horizontalLines.last().count())
        check(verticalLines.first().count() == verticalLines.last().count())
        return horizontalLines.first().count().toLong() * verticalLines.first().count()
    }

    fun isCompletable(boxes: MutableSet<Box>): Boolean {
        if(this.start == this.end) return true
        else if(this.size() < 3) return false
        else {
            val completed = this.complete(boxes)
            return completed != null
        }
    }

    private fun complete(boxes: MutableSet<Box>): Box? {
        val target = this.locationBeforeStart
        val box = boxes.firstOrNull { it.end == target }
        if(box == null) return null
        else {
            if(box.verticalLines.size == 2) box.add((box.end..this.end.copy(y = box.end.y)))
            else box.add((box.end..this.end.copy(x = box.end.x)))
            return this.fillOut()
        }
    }

    private fun fillOut(): Box {
        return this.add(this.end..this.start).first()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Box

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    companion object {
        fun start(range: CoordinateProgression): Box {
            return if(range.isHorizontal()) Box(horizontalLines = mutableListOf(range), start = range.start, end = range.end)
            else Box(verticalLines = mutableListOf(range), start = range.start, end = range.end)
        }
    }


}

private fun CoordinateProgression.getAdjacentPos(): Coordinate {
    if(this.isVertical()) {
        if(start.y < end.y) return this.start.copy(y = start.y - 1)
        else return this.start.copy(y = start.y + 1)
    } else {
        if(start.x < end.x) return this.start.copy(x = start.x - 1)
        else return this.start.copy(x = start.x + 1)
    }
}

private fun getTrench(instructions: List<Instruction>): List<Pair<Coordinate, Turn>> {
    var current = Coordinate(1, 1)
    val trench = mutableListOf(current to getTurn(instructions.last().direction, instructions.first().direction))

    val (dir, s) = instructions.first()

    current = current.move(dir, s)
    trench += current to getTurn(dir, instructions[1].direction)

    instructions.windowed(2).forEach { (prev, new) ->
        val (direction, steps) = new
        current = current.move(direction, steps)

        val turn = getTurn(prev.direction, direction)

        trench += current to turn
    }
    return trench
}

private fun getTurn(direction: Direction, nextTurn: Direction) =
    when (direction) {
        nextTurn.turnLeft() -> RIGHT
        nextTurn.turnRight() -> LEFT
        else -> error("unsupported turn from $direction to $nextTurn")
    }

data class Instruction(val direction: Direction, val distance: Int)

fun toGrid(input: String): Grid<Char> {
    val trench = getTrench(input.lines().map { toInstruction(it) })
//        .onEach { println("turn ${it.second} to ${it.first}") }
        .map { it.first }
        .windowed(2).flatMap { (c1, c2) -> c1..c2 }
    return charGridFromCoordinates(trench)
}
