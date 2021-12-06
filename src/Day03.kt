fun main() {

    fun part1(input: List<String>): Int {
        val binaryStringLength = input[0].length
        if (binaryStringLength > 0) {
            val gammaArray = Array(binaryStringLength) { "0" }
            val epsilonArray = Array(binaryStringLength) { "0" }
            val zeroCount = IntArray(binaryStringLength) { 0 }
            for (binaryDataString in input) {
                var zeroIndex = -2
                var startIndex = 0
                while (zeroIndex != -1) {
                    zeroIndex = binaryDataString.indexOf("0", startIndex)
                    if (zeroIndex != -1) {
                        startIndex = zeroIndex + 1
                        zeroCount[zeroIndex] += 1
                    }
                }
            }
            for ((index, count) in zeroCount.withIndex()) {
                if ((input.size - count) > count) {
                    // more ones, so change gamma to 1
                    gammaArray[index] = "1"
                } else {
                    // more zeros, so change epsilon to 1
                    epsilonArray[index] = "1"
                }
            }
            val binaryGamma = gammaArray.joinToString(separator = "")
            val binaryEpsilon = epsilonArray.joinToString(separator = "")
            val gamma = Integer.parseInt(binaryGamma, 2)
            val epsilon = Integer.parseInt(binaryEpsilon, 2)
            return gamma * epsilon
        } else return 0
    }

    fun reduceListBasedOnDigit(
        index: Int,
        lifeSupportList: MutableList<String>,
        digitToCheck: String
    ): MutableList<String> {
        val tempList = mutableListOf<String>()
        for (item in lifeSupportList) {
            if (item[index].toString() == digitToCheck) {
                tempList.add(item)
            }
        }
        return tempList
    }

    fun findOxygenGeneratorRating(
        lifeSupportList: MutableList<String>,
        index: Int
    ): MutableList<String> {
        var zeroCount = 0
        for (binaryDataString in lifeSupportList) {
            if (binaryDataString[index].toString() == "0") {
                zeroCount += 1
            }
        }
        return if (zeroCount > (lifeSupportList.size / 2)) {
            reduceListBasedOnDigit(index, lifeSupportList, "0")
        } else {
            reduceListBasedOnDigit(index, lifeSupportList, "1")
        }
    }

    fun findCO2ScrubberRating(
        lifeSupportList: MutableList<String>,
        index: Int
    ): MutableList<String> {
        var zeroCount = 0
        for (binaryDataString in lifeSupportList) {
            if (binaryDataString[index].toString() == "0") {
                zeroCount += 1
            }
        }
        return if (zeroCount <= (lifeSupportList.size / 2)) {
            reduceListBasedOnDigit(index, lifeSupportList, "0")
        } else {
            reduceListBasedOnDigit(index, lifeSupportList, "1")
        }
    }

    fun part2(input: List<String>): Int {
        val binaryStringLength = input[0].length
        if (binaryStringLength > 0) {
            var mostCommonList = input.toMutableList()
            var leastCommonList = input.toMutableList()
            for (index in 0 until binaryStringLength) {
                if (mostCommonList.size > 1) {
                    mostCommonList = findOxygenGeneratorRating(mostCommonList, index)
                }
                if (leastCommonList.size > 1) {
                    leastCommonList = findCO2ScrubberRating(leastCommonList, index)
                }
                if (mostCommonList.size == 1 && leastCommonList.size == 1) break
            }

            val binaryOxygenGeneratorRating = mostCommonList[0]
            val binaryCO2ScrubberRating = leastCommonList[0]
            val oxygenGeneratorRating = Integer.parseInt(binaryOxygenGeneratorRating, 2)
            val cO2ScrubberRating = Integer.parseInt(binaryCO2ScrubberRating, 2)
            return oxygenGeneratorRating * cO2ScrubberRating
        } else return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)


    val input = readInput("Day03")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
