package com.github.shmvanhouten.adventofcode2022.day07

private const val ROOT = "/"
private const val TOTAL_DISK_SPACE = 70000000L
private const val FREE_DISK_SPACE_REQUIRED = 30000000L

fun createDeviceFromBrowsingData(input: String, device: Device = Device()): Device {
    val lines = input.lines().iterator()
    while (lines.hasNext()) {
        val line = lines.next()
        when {
            line.isListInstruction() -> {}
            line.isMoveUpDirectoryInstruction() -> {
                device.currentDirectory = device.currentDirectory.parent!!
            }
            line.isMoveToRootDirectoryInstruction() -> {
                device.currentDirectory = device.rootDirectory
            }
            line.isChangeDirectoryInstruction() -> {
                device.currentDirectory = device.currentDirectory
                    .subDirectories.first { it.name == line.changeDirectoryName() }
            }
            line.isDirectory() -> {
                device.currentDirectory.subDirectories += line.toDirectory(device.currentDirectory)
            }
            else -> {
                device.currentDirectory.files.add(line.toFile())
            }
        }
    }

    return device
}

private fun String.toDirectory(parent: Directory): Directory {
    return Directory(name = this.split(' ')[1], parent = parent)
}

private fun String.toFile(): File {
    val (size, name) = this.split(' ')
    return File(name = name, size = size.toLong())
}

private fun String.isMoveUpDirectoryInstruction(): Boolean = split(' ').last() == ".."

private fun String.isMoveToRootDirectoryInstruction(): Boolean = split(' ').last() == ROOT

private fun String.changeDirectoryName(): String = this.split(' ')[2]

private fun String.isDirectory(): Boolean = this.startsWith("dir")

private fun String.isChangeDirectoryInstruction(): Boolean = this.startsWith("$ cd")

private fun String.isListInstruction(): Boolean = this.startsWith("$ ls")

class Device(
    var currentDirectory: Directory = Directory(name = ROOT, parent = null),
    val rootDirectory: Directory = currentDirectory
//    val registry: MutableSet<Directory> = mutableSetOf()

) {
    fun listAllDirectories(): List<Directory> {
        return rootDirectory.listAllSubDirectories()
    }

    fun usedSpace(): Long {
        return listAllDirectories().flatMap { it.files }.sumOf { it.size }
    }

    fun directoryToDeleteToFreeUpSpaceForUpdate(): Directory {
        val amountOfSpaceToDelete = usedSpace() - (TOTAL_DISK_SPACE - FREE_DISK_SPACE_REQUIRED)
        return listAllDirectories().filter { it.size >= amountOfSpaceToDelete }.minByOrNull { it.size }!!
    }
}

data class Directory(
    val name: String,
    val parent: Directory?,
    val subDirectories: MutableSet<Directory> = mutableSetOf(),
    val files: MutableSet<File> = mutableSetOf()
//    val children: List<>
) {
    fun listAllSubDirectories(): List<Directory> {
        return subDirectories.flatMap { it.listAllSubDirectories() } + this
    }

    val size: Long by lazy {
        files.sumOf { it.size } + subDirectories.sumOf { it.size }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class File(
    val name: String,
    val size: Long
)
