package com.github.shmvanhouten.adventofcode.utility

object FileReader {

    fun readFile(relativePath: String): String {
        return (this::class.java.getResourceAsStream(relativePath) ?: error("could not read file: $relativePath"))
            .bufferedReader()
            .useLines { lines ->
                lines.joinToString("\n").trimEnd()
            }
    }
}