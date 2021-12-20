package com.github.shmvanhouten.adventofcode2021.day20

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition.*

fun enhanceImage(image: Set<Coordinate>, enhancementString: String, nrOfTimes: Int): Pair<Set<Coordinate>, Boolean> {
    return generateSequence(image to false) { (enhancedImage, surroundingAreLit) ->
        enhanceImage(enhancedImage, enhancementString, surroundingAreLit)
    }.drop(nrOfTimes)
        .first()
}

fun enhanceImage(image: Set<Coordinate>, enhancementString: String, surroundingPixelsAreLit: Boolean = false): Pair<Set<Coordinate>, Boolean> {
    val bounds = generateBounds(image)
    val (xrange, yrange) = bounds
    return ((yrange.first - 2)..(yrange.last + 2)).flatMap { y ->
        ((xrange.first - 2)..(xrange.last + 2)).map { x ->
            Coordinate(x, y)
        }
    }
        .filter { enhance(it, enhancementString, image, surroundingPixelsAreLit, bounds) == '#' }
        .toSet() to areSurroundingPixelsLitAfter(surroundingPixelsAreLit, enhancementString)
}

fun generateBounds(image: Set<Coordinate>): Bounds {
    val (minX, maxX) = minAndMaxX(image)
    val (minY, maxY) = minAndMaxY(image)
    return Bounds(
        minX..maxX,
        minY..maxY
    )
}

fun enhance(
    coordinate: Coordinate,
    enhancementString: String,
    image: Set<Coordinate>,
    surroundingPixelsAreLit: Boolean,
    bounds: Bounds
): Char {
    val value = coordinate.listTopLeftToBottomRight()
        .map {
            if (image.contains(it) || surroundingPixelsAreLit && it.isOutOfBounds(bounds))
                '1'
            else '0'
        }
        .joinToString("").toInt(2)
    return enhancementString[value]
}

private fun Coordinate.isOutOfBounds(image: Bounds): Boolean {
    val (xRange, yRange) = image
    return this.x < xRange.first ||
            this.x > xRange.last ||
            this.y < yRange.first ||
            this.y > yRange.last
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

fun minAndMaxY(image: Set<Coordinate>): Pair<Int, Int> {
    val yes = image.map { it.y }
    return yes.minOrNull()!! to yes.maxOrNull()!!
}

fun minAndMaxX(image: Set<Coordinate>): Pair<Int, Int> {
    val xes = image.map { it.x }
    return xes.minOrNull()!! to xes.maxOrNull()!!
}

private fun areSurroundingPixelsLitAfter(surroundingPixelsAreLit: Boolean, enhancementString: String) =
    if(surroundingPixelsAreLit) enhancementString.last() == '#'
    else enhancementString.first() == '#'

data class Bounds(val xRange: IntRange, val yRange: IntRange)