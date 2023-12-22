package com.github.shmvanhouten.adventofcode2023.day22

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3DProgression

fun safeToBeDisintegrated(bricks: List<Brick>): List<Brick> {
    val bricksByLayer = bricks.groupBy { it.locations.start.z }
    val bricksByTopLayer = bricks.groupBy { it.locations.end.z }
    return bricks.filter { brick ->
        val bricksItSupports = bricksByLayer[brick.locations.end.z + 1]?.filter { it.isInTheSamePlaneAs(brick) }?: emptyList()
        bricksItSupports.isEmpty() || bricksItSupports.all { it.isSupportedByMoreThan1Brick(bricksByTopLayer) }
    }
}

fun drop(bricks: List<Brick>): List<Brick> {
    val droppedBricks = mutableListOf<Brick>()
    bricks.sortedBy { it.locations.start.z }
        .forEach { brick ->
            val bricksBelow = droppedBricks.filter { it.isInTheSamePlaneAs(brick) }
            if(bricksBelow.isEmpty()) droppedBricks += brick.droppedTo(1)
            else droppedBricks += brick.droppedTo(
                bricksBelow.maxOf { it.locations.end.z } + 1
            )

        }
    return droppedBricks
}


data class Brick(val locations: Coordinate3DProgression) {
    private val plane: CoordinateProgression = locations.start.on2dPlane..locations.end.on2dPlane

    fun droppedTo(z: Int): Brick {
        return Brick(
            Coordinate3DProgression(
                locations.start.copy(z = z),
                locations.end.copy(z = z + (locations.end.z - locations.start.z))
            )
        )
    }

    fun isInTheSamePlaneAs(other: Brick): Boolean {
        return this.plane.any { it in other.plane }
        // todo; make more efficient
    }

    fun isSupportedByMoreThan1Brick(bricksByTopLayer: Map<Int, List<Brick>>): Boolean {
        val layer = this.locations.start.z
        return (bricksByTopLayer[layer - 1]?.count {this.isInTheSamePlaneAs(it)}?:0) >= 2
    }
}

private fun String.coordinate3d(): Coordinate3d {
    val (x, y, z) = this.split(',')
    return Coordinate3d(x.toInt(), y.toInt(), z.toInt())
}

fun parse(raw: String): List<Brick> {
    return raw.lines().map {
        val(left, right) = it.split('~')
        Brick(Coordinate3DProgression(left.coordinate3d(), right.coordinate3d()))
    }
}
