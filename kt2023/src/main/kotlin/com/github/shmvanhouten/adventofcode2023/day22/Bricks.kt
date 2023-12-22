package com.github.shmvanhouten.adventofcode2023.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3DProgression

fun safeToBeDisintegrated(bricks: List<DroppedBrick>, supportMap: Map<DroppedBrick, List<DroppedBrick>>): List<DroppedBrick> {
    val bricksByLayer = bricks.groupBy { it.locations.start.z }
    return bricks.filter { brick ->
        val bricksItSupports = bricksByLayer[brick.locations.end.z + 1]?.filter { brick in it.supportedBy }?: emptyList()
        bricksItSupports.isEmpty() || bricksItSupports.all { it.supportedBy.size >= 2 }
    }
}

fun drop(bricks: List<Brick>): Pair<List<DroppedBrick>, Map<DroppedBrick, List<DroppedBrick>>> {
    val droppedBricks = mutableListOf<DroppedBrick>()
    val supportMap = mutableMapOf<DroppedBrick, List<DroppedBrick>>()
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
    return droppedBricks to supportMap
}


open class Brick(val locations: Coordinate3DProgression, val id: Int) {
    private val plane: CoordinateProgression = locations.start.on2dPlane..locations.end.on2dPlane

    fun droppedTo(z: Int, supportingBricks: List<DroppedBrick>): DroppedBrick {
        return DroppedBrick(
            Coordinate3DProgression(
                locations.start.copy(z = z),
                locations.end.copy(z = z + (locations.end.z - locations.start.z))
            ),
            supportingBricks,
            id
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
        return locations.hashCode()
    }

    override fun toString(): String {
        return "$id : $locations"
    }


}

data class DroppedBrick(
    private val range: Coordinate3DProgression,
    val supportedBy: List<DroppedBrick>,
    val identifier: Int
): Brick(range, identifier)

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
    return Brick(Coordinate3DProgression(left.coordinate3d(), right.coordinate3d()), i)
}
