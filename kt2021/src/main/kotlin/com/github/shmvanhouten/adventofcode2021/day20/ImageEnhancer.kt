package com.github.shmvanhouten.adventofcode2021.day20

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition.*

fun enhanceImage(image: Image, enhancementString: String, nrOfTimes: Int): Image {
    return generateSequence(image) { enhancedImage ->
        enhanceImage(enhancedImage, enhancementString)
    }.drop(nrOfTimes)
        .first()
}

fun enhanceImage(
    image: Image,
    enhancementString: String
): Image {
    return image.bounds.enlargeBy(2)
        .listCoordinatesInBounds()
        .filter { enhance(it, enhancementString, image) }
        .toSet()
        .let {
            Image(it, areSurroundingPixelsLitAfter(image.surroundingPixelsAreLit, enhancementString))
        }

}

fun enhance(
    coordinate: Coordinate,
    enhancementString: String,
    image: Image
): Boolean {
    val value = coordinate.listTopLeftToBottomRight()
        .map {
            if (
                image.contains(it)
                || image.surroundingPixelsAreLit && it.isOutOfBounds(image.bounds)
            ) '1'
            else '0'
        }
        .joinToString("").toInt(2)
    return enhancementString[value] == '#'
}

private fun Coordinate.isOutOfBounds(bounds: Bounds): Boolean {
    return bounds.isInBounds(this)
}

private fun Coordinate.listTopLeftToBottomRight(): List<Coordinate> {
    return listOf(
        this + TOP_LEFT.coordinate,
        this + TOP.coordinate,
        this + TOP_RIGHT.coordinate,
        this + LEFT.coordinate,
        this,
        this + RIGHT.coordinate,
        this + BOTTOM_LEFT.coordinate,
        this + BOTTOM.coordinate,
        this + BOTTOM_RIGHT.coordinate,
    )
}

private fun areSurroundingPixelsLitAfter(surroundingPixelsAreLit: Boolean, enhancementString: String) =
    if (surroundingPixelsAreLit) enhancementString.last() == '#'
    else enhancementString.first() == '#'

data class Image(val image: Set<Coordinate>, val surroundingPixelsAreLit: Boolean = false) {

    val size: Int by lazy { image.size }
    val bounds: Bounds by lazy { Bounds(image) }

    fun contains(c: Coordinate): Boolean = image.contains(c)

}

data class Bounds(val xRange: IntRange, val yRange: IntRange) {
    constructor(image: Collection<Coordinate>) :
            this(
                minToMaxX(image),
                minToMaxY(image)
            )

    fun enlargeBy(n: Int): Bounds {
        return Bounds(
            (xRange.first - n)..(xRange.last + n),
            (yRange.first - n)..(yRange.last + n)
        )
    }

    fun listCoordinatesInBounds(
    ) = yRange.flatMap { y ->
        xRange.map { x ->
            Coordinate(x, y)
        }
    }

    fun isInBounds(coordinate: Coordinate): Boolean {
        return coordinate.x !in xRange ||
                coordinate.y !in yRange
    }

}

private fun minToMaxY(image: Collection<Coordinate>): IntRange {
    val ys = image.map { it.y }
    return ys.minOrNull()!!..ys.maxOrNull()!!
}

private fun minToMaxX(image: Collection<Coordinate>): IntRange {
    val xes = image.map { it.x }
    return xes.minOrNull()!!..xes.maxOrNull()!!
}