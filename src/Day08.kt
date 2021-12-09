import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val inputString = input.toString()
        val initialArray = inputString.split(", ", "[", "]")
        val cleanedInitialArray = initialArray.filter { it.isNotBlank() }
        val rawOutputValues = cleanedInitialArray.toMutableList()
        for (i in rawOutputValues.indices) {
            rawOutputValues[i] = rawOutputValues[i].substringAfter('|').trim()
        }
        val initialAllOutputValues = rawOutputValues.toString().split(" ", ", ", "[", "]")
        val allOutputValues = initialAllOutputValues.filter { it.isNotBlank() }
        var countOfKnownDigits = 0
        allOutputValues.forEach{
            if (it.length in 2..4 || it.length == 7) {
                countOfKnownDigits++
            }
        }
        return countOfKnownDigits
    }

    fun findDigitMap(rawInput: String): TreeMap<Int, String> {
        val digitMap = TreeMap<Int, String>()
        val inputList = rawInput.split(" ")
        val unknownList = mutableListOf<String>()
        inputList.forEach {
            when(it.length) {
                2 -> {
                    digitMap[1] = it
                }
                3 -> {
                    digitMap[7] = it
                }
                4 -> {
                    digitMap[4] = it
                }
                7 -> {
                    digitMap[8] = it
                }
                else -> unknownList.add(it)
            }
        }
        unknownList.forEach {
            when(it.length) {
                5 -> {
                    if (it.contains((digitMap[7])!![0]) && it.contains((digitMap[7])!![1]) && it.contains((digitMap[7])!![2])) {
                        digitMap[3] = it
                    } else {
                        var countSignals = 0
                        (digitMap[4])!!.forEach { signal ->
                            if (it.contains(signal)) {
                                countSignals++
                            }
                        }
                        when(countSignals) {
                            2 -> digitMap[2] = it
                            3 -> digitMap[5] = it
                        }
                    }
                }
                6 -> {
                    if (!(it.contains((digitMap[7])!![0]) && it.contains((digitMap[7])!![1]) && it.contains((digitMap[7])!![2]))) {
                        digitMap[6] = it
                    } else {
                        var countSignals = 0
                        (digitMap[4])!!.forEach { signal ->
                            if (it.contains(signal)) {
                                countSignals++
                            } else {
                                digitMap[0] = it
                            }
                        }
                        if (countSignals == 4) {
                            digitMap[9] = it
                        }
                    }
                }
            }
        }

        return digitMap
    }

    fun compareDigits(output: String, s: String?): Boolean {
        if (s == null) return false
        if (s.length != output.length) return false
        return output.all { s.contains(it) }
    }

    fun findDigit(digitMap: TreeMap<Int, String>, output: String): String {
        return when(output.length) {
            2 -> {
                "1"
            }
            3 -> {
                "7"
            }
            4 -> {
                "4"
            }
            7 -> {
                "8"
            }
            5 -> {
                if (compareDigits(output, digitMap[2])) {
                    "2"
                } else if (compareDigits(output,digitMap[3])) {
                    "3"
                } else {
                    "5"
                }
            }
            6 -> {
                if (compareDigits(output, digitMap[0])) {
                    "0"
                } else if (compareDigits(output,digitMap[6])) {
                    "6"
                } else {
                    "9"
                }
            }
            else -> ""
        }
    }

    fun part2(input: List<String>): Int {
        val inputString = input.toString()
        val initialArray = inputString.split(", ", "[", "]")
        val cleanedInitialArray = initialArray.filter { it.isNotBlank() }
        var digitMap: TreeMap<Int, String>
        val outputValues = mutableListOf<Int>()
        val rawOutputValues = cleanedInitialArray.toMutableList()
        for (i in rawOutputValues.indices) {
            rawOutputValues[i] = rawOutputValues[i].substringAfter('|').trim()
        }
        val rawInputValues = cleanedInitialArray.toMutableList()
        for (i in rawInputValues.indices) {
            rawInputValues[i] = rawInputValues[i].substringBefore('|').trim()
            digitMap = findDigitMap(rawInputValues[i])
            val outputBuilder = StringBuilder()
            var listOfOutputValues = rawOutputValues[i].split(" ")  // output values for the corresponding input line
            listOfOutputValues = listOfOutputValues.filter { it.isNotBlank() }
            listOfOutputValues.forEach {
                outputBuilder.append(findDigit(digitMap, it))  // signals may not be in the same order as digitMap, so compare signals in digitMap with signals in output value to find actual digit for output
            }
            val actualOutput = outputBuilder.toString()
            outputValues.add(actualOutput.toInt())
        }

        return outputValues.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
