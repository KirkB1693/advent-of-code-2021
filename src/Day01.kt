fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        var prevDepth = input[0].toInt()
        for (depthString in input) {
            val depth = depthString.toInt()
            if (depth > prevDepth) {  //depth increases
                count++
            }
            prevDepth = depth
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var increasingDepthWindowCount = 0
        val inputWindowed = input.windowed(3)
        var prevDepthWindow = inputWindowed[0].sumOf { it.toInt() }
        if (inputWindowed.size >= 2) {
            for (depthWindowString in inputWindowed) {
                val depthWindow = depthWindowString.sumOf { it.toInt() }
                if (depthWindow > prevDepthWindow) {  //depth window increases
                    increasingDepthWindowCount++
                }
                prevDepthWindow = depthWindow
            }
        }
        return increasingDepthWindowCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
