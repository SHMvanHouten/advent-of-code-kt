package com.github.shmvanhouten.adventofcode.utility

object FileReader {

    fun readFile(relativePath: String): String{
        val stringBuilder = StringBuilder()

        val inputStream = this::class.java.getResourceAsStream(relativePath)?: error("could not read file")
        inputStream.bufferedReader()
            .useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                    stringBuilder.append('\n')
                }
            }

        return stringBuilder.toString().trim()
    }
}