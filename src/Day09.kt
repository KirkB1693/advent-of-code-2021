fun main() {
    fun assignDepths(input: List<String>, depthMap: Array<IntArray>): Array<IntArray> {
        for (x in input[0].indices) {
            for (y in input.indices) {
                depthMap[x][y] = input[y][x].toString().toInt()
            }
        }
        return depthMap
    }

    fun isLowPoint(pointToCheck: Int, pointAbove: Int, pointBelow: Int, pointToLeft: Int, pointToRight: Int): Boolean {
        return (pointToCheck < pointAbove && pointToCheck < pointBelow && pointToCheck < pointToLeft && pointToCheck < pointToRight)
    }

    fun getRiskLevel(depthMap: Array<IntArray>): Int {
        var riskLevel = 0
        for (x in depthMap.indices) {
            for (y in depthMap[0].indices) {
                val pointToCheck = depthMap[x][y]
                val pointAbove = if ((y - 1) in depthMap[0].indices) {
                    depthMap[x][y - 1]
                } else {
                    10
                }
                val pointBelow = if ((y + 1) in depthMap[0].indices) {
                    depthMap[x][y + 1]
                } else {
                    10
                }
                val pointToLeft = if ((x - 1) in depthMap.indices) {
                    depthMap[x - 1][y]
                } else {
                    10
                }
                val pointToRight = if ((x + 1) in depthMap.indices) {
                    depthMap[x + 1][y]
                } else {
                    10
                }
                if (isLowPoint(pointToCheck, pointAbove, pointBelow, pointToLeft, pointToRight)) {
                    riskLevel += pointToCheck + 1
                }

            }
        }
        return riskLevel
    }

    fun part1(input: List<String>): Int {
        var depthMap = Array(input[0].length) {         // doing length first so depthMap[x][y] matches visual
            IntArray(input.size) { 0 }
        }
        depthMap = assignDepths(input, depthMap)
        return getRiskLevel(depthMap)
    }

    fun assignLowPoints(lowPointMap: Array<IntArray>, depthMap: Array<IntArray>): Array<IntArray> {
        for (x in depthMap.indices) {
            for (y in depthMap[0].indices) {
                val pointToCheck = depthMap[x][y]
                val pointAbove = if ((y - 1) in depthMap[0].indices) {
                    depthMap[x][y - 1]
                } else {
                    10
                }
                val pointBelow = if ((y + 1) in depthMap[0].indices) {
                    depthMap[x][y + 1]
                } else {
                    10
                }
                val pointToLeft = if ((x - 1) in depthMap.indices) {
                    depthMap[x - 1][y]
                } else {
                    10
                }
                val pointToRight = if ((x + 1) in depthMap.indices) {
                    depthMap[x + 1][y]
                } else {
                    10
                }
                if (isLowPoint(pointToCheck, pointAbove, pointBelow, pointToLeft, pointToRight)) {
                    lowPointMap[x][y] = 1
                }

            }
        }
        return lowPointMap
    }

    fun calculateCurrentBasinSize(basinMap: Array<IntArray>): Int {
        var basinSize = 0
        for (x in basinMap.indices) {
            for (y in basinMap[0].indices) {
                if (basinMap[x][y] == 1) {
                    basinSize++
                }
            }
        }
        return basinSize
    }

    fun floodFill(x: Int, y: Int, basinMap: Array<IntArray>, depthMap: Array<IntArray>): Int {
        if (x in basinMap.indices && y in basinMap[0].indices) {  // point is inside the map
            return if (basinMap[x][y] == 1 || depthMap[x][y] == 9) {
                // return, I've already been here, or it's a basin edge
                0
            } else {
                // mark this point in basin and keep exploring
                basinMap[x][y] = 1

                floodFill(x, y - 1, basinMap, depthMap)  // go up
                floodFill(x, y + 1, basinMap, depthMap) // go down
                floodFill(x - 1, y, basinMap, depthMap) // go left
                floodFill(x + 1, y, basinMap, depthMap) // go right
                calculateCurrentBasinSize(basinMap)
            }
        } else {
            return 0  // point was outside the map
        }
    }

    fun calculateBasinSize(x: Int, y: Int, depthMap: Array<IntArray>): Int {
        val basinMap = Array(depthMap.size) {
            IntArray(depthMap[0].size) { 0 }
        }
        return floodFill(x, y, basinMap, depthMap)
    }

    fun getThreeLargestBasinSizes(lowPointMap: Array<IntArray>, depthMap: Array<IntArray>): Array<Int> {
        val basinSizeList = arrayOf<Int>(0, 0, 0)
        for (x in depthMap.indices) {
            for (y in depthMap[0].indices) {
                if (lowPointMap[x][y] == 1) {
                    val currentBasinSize = calculateBasinSize(x, y, depthMap)
                    if (currentBasinSize > basinSizeList[0]) {
                        basinSizeList[0] = currentBasinSize
                        basinSizeList.sort()  // sort array ascending so that we always compare the new basin size to the lowest value and replace that item in the list
                    }
                }
            }
        }
        return basinSizeList
    }

    fun part2(input: List<String>): Long {
        var depthMap = Array(input[0].length) {         // doing length first so depthMap[x][y] matches visual
            IntArray(input.size) { 0 }
        }
        var lowPointMap = Array(input[0].length) {
            IntArray(input.size) { 0 }
        }
        depthMap = assignDepths(input, depthMap)
        lowPointMap = assignLowPoints(lowPointMap, depthMap)
        val threeLargestBasinSizes = getThreeLargestBasinSizes(lowPointMap, depthMap)
        return (threeLargestBasinSizes[0].toLong() * threeLargestBasinSizes[1] * threeLargestBasinSizes[2])
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134L)

    val input = readInput("Day09")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
