package com.github.shmvanhouten.adventofcode2022.day07

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day07Test {

    @Nested
    inner class Part1 {

        @Test
        @DisplayName("is instruction is $ cd /, current directory is '/'")
        internal fun `if instruction is $ forwardslash `() {
            val browsingData = "\$ cd /"
            val device = createDeviceFromBrowsingData(browsingData)
            assertThat(device.currentDirectory.name).isEqualTo("/")
        }

        @Test
        internal fun `after command ls, read the result and put them into the directory`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |14848514 b.txt
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.currentDirectory.files)
                .hasSize(1)
            assertThat(device.currentDirectory.files.first().size)
                .isEqualTo(14848514L)
        }

        @Test
        internal fun `subdirectories are also put into the directory`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |dir a
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.currentDirectory.subDirectories)
                .hasSize(1)
            assertThat(device.currentDirectory.subDirectories.first().name)
                .isEqualTo("a")
        }

        @Test
        internal fun `if dir only contains files, it has the sum of the files it contains`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |14848514 b.txt
                |12345632 wut.exe
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.currentDirectory.files)
                .hasSize(2)
            assertThat(device.currentDirectory.size)
                .isEqualTo(14848514L + 12345632)
        }

        @Test
        internal fun `subdirectories also have file sizes`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |dir a
                |$ cd a
                |$ ls
                |14848514 b.txt
                |12345632 wut.exe
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.rootDirectory.subDirectories.first { it.name == "a" }.size)
                .isEqualTo(14848514L + 12345632)
        }

        @Test
        internal fun `subdirectories also add to the filesize of parent directories`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |dir a
                |$ cd a
                |$ ls
                |14848514 b.txt
                |12345632 wut.exe
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.rootDirectory.name).isEqualTo("/")
            assertThat(device.rootDirectory.size)
                .isEqualTo(14848514L + 12345632)
        }

        @Test
        internal fun `dir dotdot moves up to the parent`() {
            val browsingData = """
                |$ cd /
                |$ ls
                |dir a
                |$ cd a
                |$ cd ..
            """.trimMargin()

            val device = createDeviceFromBrowsingData(browsingData)

            assertThat(device.currentDirectory.name).isEqualTo("/")
        }

        @Test
        internal fun example() {
            val device = createDeviceFromBrowsingData(exampleInput)

            val smallDirectories = device.filterDirectories{ size <= 100000 }
            assertThat(smallDirectories.sumOf { it.size })
                .isEqualTo(95437)
        }

        @Test
        internal fun `part 1`() {
            val device = createDeviceFromBrowsingData(input)

            val smallDirectories = device.filterDirectories { size <= 100000 }
            assertThat(smallDirectories.sumOf { it.size })
                .isEqualTo(1315285)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example2() {
            val device = createDeviceFromBrowsingData(exampleInput)

            assertThat(device.usedSpace()).isEqualTo(48381165)
            assertThat(device.directoryToDeleteToFreeUpSpaceForUpdate().size)
                .isEqualTo(24933642)
        }

        @Test
        internal fun `part 2`() {
            val device = createDeviceFromBrowsingData(input)

            assertThat(device.directoryToDeleteToFreeUpSpaceForUpdate().size)
                .isEqualTo(9847279)

        }
    }

    private val exampleInput = """
        |$ cd /
        |$ ls
        |dir a
        |14848514 b.txt
        |8504156 c.dat
        |dir d
        |$ cd a
        |$ ls
        |dir e
        |29116 f
        |2557 g
        |62596 h.lst
        |$ cd e
        |$ ls
        |584 i
        |$ cd ..
        |$ cd ..
        |$ cd d
        |$ ls
        |4060174 j
        |8033020 d.log
        |5626152 d.ext
        |7214296 k
    """.trimMargin()

    private val input by lazy { readFile("/input-day07.txt")}

}
