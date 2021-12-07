fun main() {
    fun removeLinesThatAreNotHorizontalOrVertical(lines: List<List<String>>): List<List<String>> {
        val newList = ArrayList<List<String>>()
        lines.forEach {
            if (it[0] == it[2] || it[1] == it[3]) {
                newList.add(it)
            }
        }
        return newList
    }

    fun getSizeOfPlot(usefulLines: List<List<String>>): Int {
        var maxSize = 0
        usefulLines.forEach {
            it.forEach { value ->
                if (value.toInt() > maxSize) {
                    maxSize = value.toInt()
                }
            }
        }
        return maxSize+1
    }

    fun plotVerticalLine(plot: Array<IntArray>, it: List<String>): Array<IntArray> {
        val y1: Int
        val y2: Int
        val x = it[0].toInt()
        if (it[1].toInt() > it[3].toInt()) {
            y1 = it[3].toInt()
            y2 = it[1].toInt()
        } else {
            y1 = it[1].toInt()
            y2 = it[3].toInt()
        }
        for (y in y1..y2) {
            plot[x][y] += 1
        }
        return plot
    }

    fun plotHorizontalLine(plot: Array<IntArray>, it: List<String>): Array<IntArray> {
        val x1: Int
        val x2: Int
        val y = it[1].toInt()
        if (it[0].toInt() > it[2].toInt()) {
            x1 = it[2].toInt()
            x2 = it[0].toInt()
        } else {
            x1 = it[0].toInt()
            x2 = it[2].toInt()
        }
        for (x in x1..x2) {
            plot[x][y] += 1
        }
        return plot
    }

    fun drawLines(plot: Array<IntArray>, usefulLines: List<List<String>>): Array<IntArray> {
        var newPlot = plot
        usefulLines.forEach {
            if (it[0] == it[2]) {
                // x1 = x2, so line is vertical
                newPlot = plotVerticalLine(newPlot, it)
            } else if (it[1] == it[3]) {
                // y1 = y2, so line is horizontal
                newPlot = plotHorizontalLine(newPlot,it)
            }

        }
        return newPlot
    }

    fun findNumberOfPointsOfOverlap(plot: Array<IntArray>): Int {
        var overlapCount = 0
        plot.forEach {
            it.forEach { point ->
                if (point >= 2) {
                    overlapCount ++
                }
            }
        }
        return overlapCount
    }

    fun part1(input: List<String>): Int {
        val inputString = input.toString()
        val initialArray = inputString.split("\\p{Punct}", " -> ", "[[", "[", "]]", "]", " ", ",")
        val cleanedArray = initialArray.filter { it.isNotBlank() }
        val lines = cleanedArray.chunked(4)
        val usefulLines = removeLinesThatAreNotHorizontalOrVertical(lines)
        val maxPlotSize = getSizeOfPlot(usefulLines)
        var plot = Array(maxPlotSize) { IntArray(maxPlotSize) { 0 } }
        plot = drawLines(plot, usefulLines)
        return findNumberOfPointsOfOverlap(plot)
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 10)

    val input = readInput("Day05")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
