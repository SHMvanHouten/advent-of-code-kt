package com.github.shmvanhouten.adventofcode.utility.coordinate

fun draw(coordinates: Collection<Coordinate>, targetChar: Char = '█'): String {
    val (minY, maxY) = coordinates.map { it.y }.extremes() ?: error("empty collection $coordinates")
    val (minX, maxX) = coordinates.map { it.x }.extremes() ?: error("empty collection $coordinates")
    return (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).map { x ->
            if (coordinates.contains(Coordinate(x, y))) {
                targetChar
            } else {
                ' '
            }
        }.joinToString("")
    }
}

fun Map<Coordinate, Char>.draw(): String {
    val (minY, maxY) = this.map { it.key.y }.extremes() ?: error("empty collection ${this.keys}")
    val (minX, maxX) = this.map { it.key.x }.extremes() ?: error("empty collection ${this.keys}")
    return (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).map { x ->
            if (this.contains(Coordinate(x, y))) {
                this[Coordinate(x, y)]
            } else {
                '?'
            }
        }.joinToString("")
    }
}

val increasedShadingChars = listOf(
    ' ',
    '‧',//\u2027
    '჻',//\u10FB
    '҉',//\u0489
    '።',//\u1362
    '#',
    '░',//\u2591
    '▒',//\u2592
    '▓',//\u2593
    '█' //\u2588
)

