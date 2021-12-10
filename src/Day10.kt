import java.lang.StringBuilder
import java.util.*

fun main() {
    var lineHasError: Boolean
    var lineSyntaxScore: Int
    val incompleteLines = mutableListOf<String>()
    val completionStrings = mutableListOf<String>()

    fun getScoreOfChar(current: Char): Int {
        return when (current) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }

    fun checkForValidOpenClosePairs(str: String) : Boolean
    {
        if (str.isEmpty())
            return true

        lineSyntaxScore = 0
        val stack : Stack<Char> = Stack<Char>()
        for (i in str.indices)
        {
            val current = str[i]
            if (current == '{' || current == '(' || current == '[' || current == '<')
            {
                stack.push(current)
            }


            if (current == '}' || current == ')' || current == ']' || current == '>')
            {
                if (stack.isEmpty()) {
                    // syntax error, closing without an opening
                    lineSyntaxScore = getScoreOfChar(current)
                    return false
                }
                val last = stack.peek()
                if (current == '}' && last == '{' || current == ')' && last == '(' || current == ']' && last == '[' || current == '>' && last == '<')
                    stack.pop()
                else {
                    // this is a syntax error, not closed correctly
                    lineSyntaxScore = getScoreOfChar(current)
                    return false
                }
            }

        }
        // incomplete, but no error
        incompleteLines.add(str)
        val sb = StringBuilder()
        while (stack.isNotEmpty()) {
            when (stack.pop())  {
                '(' -> sb.append(')')
                '{' -> sb.append('}')
                '[' -> sb.append(']')
                '<' -> sb.append('>')
            }
        }
        completionStrings.add(sb.toString())
        return true
    }

    fun findSyntaxScore(input: List<String>): Int {
        val linesWithSyntaxErrors = mutableListOf<String>()
        var syntaxScore = 0
        lineSyntaxScore = 0
        input.forEach {
            lineHasError = !checkForValidOpenClosePairs(it)
            if (lineHasError) {
                linesWithSyntaxErrors.add(it)
            }
            syntaxScore += lineSyntaxScore
        }
        return syntaxScore
    }

    fun part1(input: List<String>): Int {
        return findSyntaxScore(input)
    }

    fun getScoresForCompletionStrings(completionStrings: MutableList<String>): MutableList<Long> {
        val scoresList = mutableListOf<Long>()
        completionStrings.forEach { string ->
            var score = 0L
            string.forEach { char ->
                score *= 5L
                when(char) {
                    ')' -> score += 1L
                    ']' -> score += 2L
                    '}' -> score += 3L
                    '>' -> score += 4L
                }
            }
            scoresList.add(score)
        }
        return scoresList
    }

    fun findAutoCompleteScore(input: List<String>) : Long {
        incompleteLines.clear()
        completionStrings.clear()
        input.forEach {
            checkForValidOpenClosePairs(it)
        }
        val scores = getScoresForCompletionStrings(completionStrings)
        scores.sort()
        return scores[(scores.size / 2)]
    }

    fun part2(input: List<String>): Long {
        return findAutoCompleteScore(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
