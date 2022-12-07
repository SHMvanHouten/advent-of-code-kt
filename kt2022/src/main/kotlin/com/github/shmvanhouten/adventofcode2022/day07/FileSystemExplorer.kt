package com.github.shmvanhouten.adventofcode2022.day07

const val ROOT = "/"

fun createDeviceFromBrowsingData(input: String): Device {
    val device = Device()
    for (line in input.lines())
        when {
            line.isListInstruction() -> {}

            line.isMoveUpDir() ->
                device.currentDirectory = device.currentDirectory.parent!!

            line.isMoveToRoot() ->
                device.currentDirectory = device.rootDirectory

            line.isChangeDirectory() ->
                device.currentDirectory = device.currentDirectory
                    .subDirectories.first { it.name == line.changeDirectoryName() }

            line.isDirectory() ->
                device.currentDirectory.subDirectories += line.toDirectory(device.currentDirectory)

            else -> device.currentDirectory.files.add(line.toFile())
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

private fun String.isMoveUpDir(): Boolean = split(' ').last() == ".."

private fun String.isMoveToRoot(): Boolean = split(' ').last() == ROOT

private fun String.changeDirectoryName(): String = this.split(' ')[2]

private fun String.isDirectory(): Boolean = this.startsWith("dir")

private fun String.isChangeDirectory(): Boolean = this.startsWith("$ cd")

private fun String.isListInstruction(): Boolean = this.startsWith("$ ls")

