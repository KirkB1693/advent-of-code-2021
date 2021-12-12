fun main() {
    fun addEnergyToAdjacentOctopi(x: Int, y: Int, octopusGrid: Array<IntArray>): Array<IntArray> {
        for (x1 in x-1..x+1) {
            for (y1 in y-1..y+1) {
                if (x1 in octopusGrid.indices && y1 in octopusGrid[0].indices) {
                    if (octopusGrid[x1][y1] != 0) {
                        octopusGrid[x1][y1]++
                        if (octopusGrid[x1][y1] > 9) {
                            octopusGrid[x1][y1] = 0
                            addEnergyToAdjacentOctopi(x1,y1, octopusGrid)
                        }
                    }
                }
            }
        }
        return octopusGrid
    }

    fun processStep(octopusGrid: Array<IntArray>): Array<IntArray> {
        var tempGrid = octopusGrid.clone()
        // increment all energy levels by 1
        for (x in octopusGrid.indices) {
            for (y in octopusGrid[0].indices) {
                octopusGrid[x][y]++
            }
        }
        // process flashes
        for (x in tempGrid.indices) {
            for (y in tempGrid[0].indices) {
                if (tempGrid[x][y] > 9) {
                    tempGrid[x][y] = 0
                    tempGrid = addEnergyToAdjacentOctopi(x, y, octopusGrid)
                }
            }
        }
        return tempGrid
    }

    fun countFlashes(octopusGrid: Array<IntArray>): Int {
        var flashCount = 0
        for (x in octopusGrid.indices) {
            for (y in octopusGrid[0].indices) {
                if (octopusGrid[x][y] == 0) {
                    flashCount++
                }
            }
        }
        return flashCount
    }

    fun printGrid(octopusGrid: Array<IntArray>) {
        for (y in octopusGrid[0].indices) {
            val sb = StringBuilder()
            for (x in octopusGrid.indices) {
                sb.append(octopusGrid[x][y].toString())
            }
            println(sb.toString())
        }
    }

    fun part1(input: List<String>): Int {
        var octopusGrid = Array(input[0].length) {         // doing length first so depthMap[x][y] matches visual
            IntArray(input.size) { 0 }
        }
        for (x in input[0].indices) {
            for (y in input.indices) {
                octopusGrid[x][y] = input[y][x].toString().toInt()
            }
        }
        var flashCount = 0

        for (i in 1..100) {
            octopusGrid = processStep(octopusGrid)
            flashCount += countFlashes(octopusGrid)

        }
        return flashCount
    }

    fun part2(input: List<String>): Int {
        var octopusGrid = Array(input[0].length) {         // doing length first so depthMap[x][y] matches visual
            IntArray(input.size) { 0 }
        }
        for (x in input[0].indices) {
            for (y in input.indices) {
                octopusGrid[x][y] = input[y][x].toString().toInt()
            }
        }

        var synchronized = false
        var step = 0
        while (!synchronized) {
            step++
            octopusGrid = processStep(octopusGrid)
            if (countFlashes(octopusGrid) == 100) {
                synchronized = true
            }
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
