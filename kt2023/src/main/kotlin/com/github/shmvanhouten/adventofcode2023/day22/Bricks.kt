package com.github.shmvanhouten.adventofcode2023.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3DProgression

fun safeToBeDisintegrated(bricks: List<DroppedBrick>): List<DroppedBrick> {
    val bricksByLayer = bricks.groupBy { it.locations.start.z }
    return bricks.filter { brick ->
        val bricksItSupports = bricksByLayer[brick.locations.end.z + 1]?.filter { brick in it.supportedBy }?: emptyList()
        bricksItSupports.isEmpty() || bricksItSupports.all { it.supportedBy.size >= 2 }
    }
}

fun drop(bricks: List<Brick>): List<DroppedBrick> {
    val droppedBricks = mutableListOf<DroppedBrick>()
    bricks.sortedBy { it.locations.start.z }
        .forEach { brick ->
            val bricksBelow = droppedBricks.filter { it.isInTheSamePlaneAs(brick) }
            droppedBricks += if(bricksBelow.isEmpty()) brick.droppedTo(1, emptyList())
            else {
                val maxZ = bricksBelow.maxOf { it.locations.end.z }
                brick.droppedTo(
                    maxZ + 1,
                    bricksBelow.filter { it.locations.end.z == maxZ }
                )
            }

        }
    return droppedBricks
}

//fun cascadeFallBackwards(bricks: List<DroppedBrick>): Long {
//
//}

fun countTotalFalling(bricks: List<DroppedBrick>): Int {
    val bricksByLayer = bricks.groupBy { it.locations.start.z }
    val supportMap = bricks.associateWith { brick ->
        bricksByLayer[brick.locations.end.z + 1]?.filter { brick in it.supportedBy } ?: emptyList()
    }

    var total = 0
    val alreadyKnownToFall = mutableMapOf<DroppedBrick, Set<DroppedBrick>>()

    bricks.sortedByDescending { it.locations.start.z }.forEach { brick ->
        val fellBricks = countNumberOfBricksThatWouldFall(brick, supportMap, alreadyKnownToFall)
        alreadyKnownToFall += brick to fellBricks
        total += fellBricks.size - 1
    }

    return total
}

fun countNumberOfBricksThatWouldFall(
    brick: DroppedBrick,
    supportMap: Map<DroppedBrick, List<DroppedBrick>>,
    alreadyKnownToFall: MutableMap<DroppedBrick, Set<DroppedBrick>>
): Set<DroppedBrick> {
    val supports = ArrayDeque(supportMap[brick]!!)
    val removed = mutableSetOf(brick)
    while (supports.isNotEmpty()) {
        val support = supports.removeFirst()
        if(support.supportedBy.all { it in removed }) {
            if(alreadyKnownToFall.contains(support)) {
                val known = alreadyKnownToFall[support]!!
                removed += known
                supports += supportMap[support]!!.filter { it !in removed }
            } else {
                removed += support
                supports += supportMap[support]!!
            }
        }
    }

    return removed
}


open class Brick(val id: Int, val locations: Coordinate3DProgression) {
    private val plane: CoordinateProgression = locations.start.on2dPlane..locations.end.on2dPlane

    fun droppedTo(z: Int, supportingBricks: List<DroppedBrick>): DroppedBrick {
        return DroppedBrick(
            id,
            Coordinate3DProgression(
                locations.start.copy(z = z),
                locations.end.copy(z = z + (locations.end.z - locations.start.z))
            ),
            supportingBricks
        )
    }

    fun isInTheSamePlaneAs(other: Brick): Boolean {
        return this.plane.any { it in other.plane }
        // todo; make more efficient
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Brick

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "$id : $locations"
    }


}

data class DroppedBrick(
    val identifier: Int,
    private val range: Coordinate3DProgression,
    val supportedBy: List<DroppedBrick>
): Brick(identifier, range)

fun String.coordinate3d(): Coordinate3d {
    val (x, y, z) = this.split(',')
    return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
}

fun parse(raw: String): List<Brick> {
    return raw.lines().mapIndexed { i, it ->
        toBrick(it, i)
    }
}

fun toBrick(it: String, i: Int): Brick {
    val (left, right) = it.split('~')
    return Brick(i, Coordinate3DProgression(left.coordinate3d(), right.coordinate3d()))
}
