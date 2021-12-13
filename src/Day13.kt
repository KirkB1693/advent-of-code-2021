import java.lang.StringBuilder

fun main() {
    fun printFoldedPaper(list: MutableList<Pair<Int, Int>>) {
        var xMax = 0
        var yMax = 0
        list.forEach {
            if (it.first > xMax) {
                xMax = it.first
            }
            if (it.second > yMax) {
                yMax = it.second
            }
        }
        for (y in 0..yMax) {
            val line = StringBuilder()
            for (x in 0..xMax) {
                if (Pair(x, y) in list) {
                    print('#')
                } else {
                    print(' ')
                }
            }
            println()
        }
    }


    fun convertPointsBasedOnXFold(fold: Int, listOfPoints: MutableList<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
        val newListOfPoints = mutableListOf<Pair<Int, Int>>()
        listOfPoints.forEach {
            var tempX = it.first
            if (it.first > fold) {
                tempX = fold - (tempX - fold)
            }
            val newPair = Pair(tempX, it.second)
            if (!newListOfPoints.contains(newPair)) {
                newListOfPoints.add(newPair)
            }
        }
        return newListOfPoints
    }

    fun convertPointsBasedOnYFold(fold: Int, listOfPoints: MutableList<Pair<Int, Int>>): MutableList<Pair<Int, Int>> {
        val newListOfPoints = mutableListOf<Pair<Int, Int>>()
        listOfPoints.forEach {
            var tempY = it.second
            if (it.second > fold) {
                tempY = fold - (tempY - fold)
            }
            val newPair = Pair(it.first, tempY)
            if (!newListOfPoints.contains(newPair)) {
                newListOfPoints.add(newPair)
            }
        }
        return newListOfPoints
    }

    fun foldPaper(listOfPoints: MutableList<Pair<Int, Int>>, s: String): MutableList<Pair<Int, Int>> {
        if (s.contains("x=")) {
            val (_, fold) = s.split("x=")
            return convertPointsBasedOnXFold(fold.toInt(), listOfPoints)
        } else if (s.contains("y=")) {
            val (_, fold) = s.split("y=")
            return convertPointsBasedOnYFold(fold.toInt(), listOfPoints)
        }
        return mutableListOf<Pair<Int, Int>>()
    }

    fun part1(input: List<String>): Int {
        val listOfPoints = mutableListOf<Pair<Int, Int>>()
        val instructionsList = mutableListOf<String>()
        input.forEach {
            if (it.contains(',')) {
                val (x, y) = it.split(',')
                listOfPoints.add(Pair(x.toInt(), y.toInt()))
            } else if (it.isNotBlank()) {
                instructionsList.add(it)
            }
        }
        val newList = foldPaper(listOfPoints, instructionsList[0])
        return newList.size
    }

    fun part2(input: List<String>): Int {
        var listOfPoints = mutableListOf<Pair<Int, Int>>()
        val instructionsList = mutableListOf<String>()
        input.forEach {
            if (it.contains(',')) {
                val (x, y) = it.split(',')
                listOfPoints.add(Pair(x.toInt(), y.toInt()))
            } else if (it.isNotBlank()) {
                instructionsList.add(it)
            }
        }
        for (instruction in instructionsList) {
            listOfPoints = foldPaper(listOfPoints, instruction)
        }
        println()
        printFoldedPaper(listOfPoints)
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 21)

    val input = readInput("Day13")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
