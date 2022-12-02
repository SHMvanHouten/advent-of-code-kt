package com.github.shmvanhouten.adventofcode2021.day22


fun parse(input: String): List<RebootStep> {
   return input.lines()
       .map { it.toRebootStep() }
}

private fun String.toRebootStep(): RebootStep {
    val (toggle, ranges) = this.split(' ')
    val (xRange, yRange, zRange) = ranges.split(',').map { it.toRange() }
    return RebootStep(Toggle.valueOf(toggle.uppercase()), Cuboid(xRange, yRange, zRange))
}

fun String.toRange(): IntRange {
    val start = this.substring(this.indexOf('=') + 1, this.indexOf('.'))
    val end = this.substring(this.indexOf("..") + 2)
    return start.toInt()..end.toInt()
}