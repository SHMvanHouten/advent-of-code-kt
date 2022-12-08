package com.github.shmvanhouten.adventofcode2022.day07

private const val TOTAL_DISK_SPACE = 70000000L
private const val FREE_DISK_SPACE_REQUIRED = 30000000L

class Device(
    var currentDirectory: Directory = Directory(name = ROOT, parent = null),
    val rootDirectory: Directory = currentDirectory
) {
    fun filterDirectories(selector: Directory.() -> Boolean): List<Directory> {
        return listAllDirectories().filter(selector)
    }

    fun usedSpace(): Long {
        return rootDirectory.size
    }

    fun directoryToDeleteToFreeUpSpaceForUpdate(): Directory {
        val amountOfSpaceToDelete = usedSpace() - (TOTAL_DISK_SPACE - FREE_DISK_SPACE_REQUIRED)
        return listAllDirectories().filter { it.size >= amountOfSpaceToDelete }.minByOrNull { it.size }!!
    }

    private fun listAllDirectories(): List<Directory> {
        return rootDirectory.listAllDirectories()
    }
}

data class Directory(
    val name: String,
    val parent: Directory?,
    val subDirectories: MutableList<Directory> = mutableListOf(),
    val files: MutableList<File> = mutableListOf()
) {
    fun listAllDirectories(): List<Directory> {
        return subDirectories.flatMap { it.listAllDirectories() } + this
    }

    val size: Long by lazy {
        files.sumOf { it.size } + subDirectories.sumOf { it.size }
    }

}

data class File(
    val name: String,
    val size: Long
)