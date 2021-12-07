import kotlin.math.absoluteValue

fun main() {
    fun findLargestSize(initialCrabSubPositionList: MutableList<Int>): Int {
        var size = 0
        initialCrabSubPositionList.forEach {
            if (it > size) {
                size = it
            }
        }
        return size
    }

    fun leastNumberOfMovesToAlignSubs(numberOfCrabSubsAtEachPosition: Array<Long>): Long {
        var fuel = 9999999999999L
        for (positionToCheck in numberOfCrabSubsAtEachPosition.indices) {
            var fuelUsedFromMoves = 0L
            for (positionToMove in numberOfCrabSubsAtEachPosition.indices) {
                fuelUsedFromMoves += numberOfCrabSubsAtEachPosition[positionToMove]*(positionToMove - positionToCheck).absoluteValue
            }
            if (fuel > fuelUsedFromMoves) {
                fuel = fuelUsedFromMoves
            }
        }
        return fuel
    }

    fun part1(input: List<String>): Long {
        val inputString = input.toString()
        val initialArray = inputString.split("\\p{Punct}", " -> ", "[[", "[", "]]", "]", " ", ",")
        val cleanedArray = initialArray.filter { it.isNotBlank() }
        val initialCrabSubPositionList = cleanedArray.map { it.toInt() }.toMutableList()
        val size = findLargestSize(initialCrabSubPositionList)
        val numberOfCrabSubsAtEachPosition = Array<Long>(size+1) {0}  //need size +1 since array includes position 0
        initialCrabSubPositionList.forEach {
            numberOfCrabSubsAtEachPosition[it]++
        }
        val fuel = leastNumberOfMovesToAlignSubs(numberOfCrabSubsAtEachPosition)
        return fuel
    }

    fun leastFuelToAlignSubs(numberOfCrabSubsAtEachPosition: Array<Long>): Long {
        var fuel = 9999999999999999L
        for (positionToCheck in numberOfCrabSubsAtEachPosition.indices) {
            var fuelUsedFromMoves = 0L
            for (positionToMove in numberOfCrabSubsAtEachPosition.indices) {
                var fuelToMoveOneSubToCheckPosition = 0
                if (numberOfCrabSubsAtEachPosition[positionToMove] > 0) {
                    val positionsAwayFromCheck = (positionToMove - positionToCheck).absoluteValue
                    if (positionsAwayFromCheck > 0) {
                        for (i in 1..positionsAwayFromCheck) {
                            fuelToMoveOneSubToCheckPosition += i
                        }
                    }
                }
                fuelUsedFromMoves += numberOfCrabSubsAtEachPosition[positionToMove]*fuelToMoveOneSubToCheckPosition
            }
            if (fuel > fuelUsedFromMoves) {
                fuel = fuelUsedFromMoves
            }
        }
        return fuel
    }

    fun part2(input: List<String>): Long {
        val inputString = input.toString()
        val initialArray = inputString.split("\\p{Punct}", " -> ", "[[", "[", "]]", "]", " ", ",")
        val cleanedArray = initialArray.filter { it.isNotBlank() }
        val initialCrabSubPositionList = cleanedArray.map { it.toInt() }.toMutableList()
        val size = findLargestSize(initialCrabSubPositionList)
        val numberOfCrabSubsAtEachPosition = Array<Long>(size+1) {0}  //need size +1 since array includes position 0
        initialCrabSubPositionList.forEach {
            numberOfCrabSubsAtEachPosition[it]++
        }
        val fuel = leastFuelToAlignSubs(numberOfCrabSubsAtEachPosition)
        return fuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37L)
    check(part2(testInput) == 168L)

    val input = readInput("Day07")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
