fun main() {
    fun part1(input: List<String>): Int {
        println(input)
        return input.size
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 10)

    val input = readInput("Day05")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
