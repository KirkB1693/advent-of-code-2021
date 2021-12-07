fun main() {
    fun simulateDay(lanternfishList: MutableList<Int>): MutableList<Int> {
        // if lifecycle 0 set to 7 (will decrement to 6) and add a fish at end with life 8, then decrement all fish
        for (fish in 0 until lanternfishList.size) {
            if (lanternfishList[fish] == 0) {
                lanternfishList[fish] = 7
                lanternfishList.add(8)
            }
            lanternfishList[fish]--
        }
        return lanternfishList
    }

    fun part1(input: List<String>): Int {
        val inputString = input.toString()
        val initialArray = inputString.split("\\p{Punct}", " -> ", "[[", "[", "]]", "]", " ", ",")
        val cleanedArray = initialArray.filter { it.isNotBlank() }
        var lanternfishList = cleanedArray.map { it.toInt() }.toMutableList()
        for (i in 1..80) {
            lanternfishList = simulateDay(lanternfishList)
        }
        return lanternfishList.size
    }

    fun simulateDayV2(numberOfFishAtEachState: Array<Long>): Array<Long> {
        val newList = Array<Long>(9) {0}
        for (index in numberOfFishAtEachState.indices) {
            if (index == 0) {
                newList[6] += numberOfFishAtEachState[0]
                newList[8] += numberOfFishAtEachState[0]
            } else {
                newList[index-1] += numberOfFishAtEachState[index]
            }
        }
        return newList
    }

    fun part2(input: List<String>): Long {
        val inputString = input.toString()
        val initialArray = inputString.split("\\p{Punct}", " -> ", "[[", "[", "]]", "]", " ", ",")
        val cleanedArray = initialArray.filter { it.isNotBlank() }
        val initialLanternfishList = cleanedArray.map { it.toInt() }.toMutableList()
        var numberOfFishAtEachState = Array<Long>(9) {0}
        initialLanternfishList.forEach {
            numberOfFishAtEachState[it]++
        }
        for (i in 1..256) {
            numberOfFishAtEachState = simulateDayV2(numberOfFishAtEachState)
        }
        return numberOfFishAtEachState.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
