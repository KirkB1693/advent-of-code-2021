import kotlin.text.StringBuilder

fun main() {
    fun getInsertionPairs(input: List<String>): Map<String, String> {
        val pairsList = hashMapOf<String, String>()
        for (lineNumber in input.indices) {
            if (lineNumber != 0 && input[lineNumber].isNotBlank()) {
                val (rule, characterToInsert) = input[lineNumber].split(" -> ")
                pairsList[rule] = characterToInsert
            }
        }
        return pairsList
    }

    fun getMostCommonElementMinusLeastCommon(polymer: String): Int {
        val countMap = hashMapOf<Char, Int>()
        polymer.forEach {
            if (countMap[it] == null) {
                countMap[it] = 1
            } else {
                val tempCount = countMap[it]!!
                countMap[it] = tempCount + 1
            }
        }
        var max = 0
        var min = Int.MAX_VALUE
        for ((_, count) in countMap) {
            if (max < count) {
                max = count
            }
            if (min > count) {
                min = count
            }
        }
        return max - min
    }

    fun part1(input: List<String>): Int {
        val polymerTemplate = input[0]
        val insertionPairs: Map<String, String> = getInsertionPairs(input)
        var polymer = polymerTemplate
        for (step in 1..10) {
            val newPolymer = StringBuilder()
            val pairsToCheck = polymer.windowed(size = 2, step = 1)
            for (pair in pairsToCheck) {
                val characterToInsert = insertionPairs[pair] ?: ""
                if (newPolymer.isEmpty()) {
                    newPolymer.append(pair[0]).append(characterToInsert).append(pair[1])
                } else {
                    newPolymer.append(characterToInsert).append(pair[1])
                }
            }
            polymer = newPolymer.toString()
        }
        return getMostCommonElementMinusLeastCommon(polymer)
    }

    fun addNewPairToPolymer(newPair: String, polymer: HashMap<String, Long>, count: Long) {
        val tempCount = polymer[newPair] ?: 0L
        polymer[newPair] = tempCount + count
    }

    fun getMostCommonElementMinusLeastCommonPart2(polymer: HashMap<String, Long>, initialElement: Char): Long {
        val countMap = hashMapOf<Char, Long>()
        countMap[initialElement] = 1
        for ((pair, count) in polymer) {
            val tempCount = countMap[pair[1]] ?: 0L
            countMap[pair[1]] = tempCount + count
        }

        var max = 0L
        var min = Long.MAX_VALUE
        for ((_, count) in countMap) {
            if (max < count) {
                max = count
            }
            if (min > count) {
                min = count
            }
        }
        return max - min
    }

    fun removePairFromPolymer(pairToCheck: String, polymer: HashMap<String, Long>, count: Long) {
        val tempCount = polymer[pairToCheck] ?: count
        polymer[pairToCheck] = tempCount - count
    }

    fun part2(input: List<String>): Long {
        val polymerTemplate = input[0]
        val insertionPairs: Map<String, String> = getInsertionPairs(input)
        val initialElement = polymerTemplate[0]
        var polymer = hashMapOf<String, Long>()
        val pairsToAddToPolymer = polymerTemplate.windowed(size = 2, step = 1)
        for (pair in pairsToAddToPolymer) {
            if (polymer[pair] == null) {
                polymer[pair] = 1
            } else {
                val tempValue = polymer[pair]
                polymer[pair] = tempValue!! + 1
            }
        }
        for (step in 1..40) {
            val newPolymer: HashMap<String, Long> = HashMap(polymer)
            for ((pairToCheck, count) in polymer) {
                if (count > 0) {
                    val characterToInsert = insertionPairs[pairToCheck] ?: ""
                    if (characterToInsert != "") {
                        removePairFromPolymer(pairToCheck, newPolymer, count)
                        val newPairA = StringBuilder()
                        newPairA.append(pairToCheck[0]).append(characterToInsert)
                        addNewPairToPolymer(newPairA.toString(), newPolymer, count)
                        val newPairB = StringBuilder()
                        newPairB.append(characterToInsert).append(pairToCheck[1])
                        addNewPairToPolymer(newPairB.toString(), newPolymer, count)
                    }
                }
            }
            polymer = HashMap(newPolymer)
        }
        return getMostCommonElementMinusLeastCommonPart2(polymer, initialElement)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
