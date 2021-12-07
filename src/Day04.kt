fun main() {
    fun getRandomNumbersList(s: String): List<Int> {
        val regex = ","
        return s.split(regex).map {
            it.trim().toInt()
        }
    }

    fun getLine(s: String): List<Int> {
        return s.trim().split("  ", " ").map {
            it.trim().toInt()
        }
    }

    fun getBingoBoards(input: List<String>): ArrayList<List<List<Int>>> {
        val board = ArrayList<List<Int>>()
        val listOfBoards = ArrayList<List<List<Int>>>()
        var lineCount = 0
        for (lineNumber in 1 until input.size) {
            if (input[lineNumber].isBlank()) {
                lineCount = 0
                board.clear()
            } else {
                lineCount++
                val currentLine = getLine(input[lineNumber])
                board.add(currentLine)
                if (lineCount == 5) {
                    val boardToAdd = board.toMutableList()
                    listOfBoards.add(boardToAdd)
                }
            }
        }
        return listOfBoards
    }

    fun markTheBingoBoards(
        number: Int,
        bingoBoards: ArrayList<List<List<Int>>>,
        boardsToMark: Array<Array<IntArray>>
    ): Array<Array<IntArray>> {
        for (boardNumber in 0 until bingoBoards.size) {
            for (lineNumber in 0 until bingoBoards[boardNumber].size) {
                val columnNumber = bingoBoards[boardNumber][lineNumber].indexOf(number)
                if (columnNumber != -1) {
                    boardsToMark[boardNumber][lineNumber][columnNumber] = 1
                }
            }
        }
        return boardsToMark
    }

    fun sumOfUnmarkedNumbersOnBoard(numberedBoard: List<List<Int>>, markedBoard: Array<IntArray>): Int {
        var sumOfUnmarkedNumbers = 0
        for (lineNumber in markedBoard.indices) {
            if (markedBoard[lineNumber].contains(1)) {
                for (columnNumber in markedBoard[lineNumber].indices) {
                    if (markedBoard[lineNumber][columnNumber] == 0) {
                        sumOfUnmarkedNumbers += numberedBoard[lineNumber][columnNumber]
                    }
                }
            } else {
                sumOfUnmarkedNumbers += numberedBoard[lineNumber].sum()
            }
        }
        return sumOfUnmarkedNumbers
    }

    fun getSumIfWinningBoard(
        bingoBoards: ArrayList<List<List<Int>>>,
        boardsToMark: Array<Array<IntArray>>
    ): Int {
        val winningList = intArrayOf(1, 1, 1, 1, 1)
        for (boardNumber in 0 until bingoBoards.size) {
            for (lineNumber in 0 until bingoBoards[boardNumber].size) {
                for (column in 0 until bingoBoards[boardNumber][lineNumber].size) {
                    val columnNumber = boardsToMark[boardNumber][lineNumber].indexOf(1)
                    if (columnNumber != -1) {
                        if (boardsToMark[boardNumber][lineNumber].contentEquals(winningList)) {
                            // the line is a winning line, all 1's
                            return sumOfUnmarkedNumbersOnBoard(bingoBoards[boardNumber], boardsToMark[boardNumber])
                        } else {
                            if (lineNumber != 0) {
                                // if we aren't starting the check from the first line the column has a zero in it
                                break
                            } else {
                                // starting from the first line, check the lines below in the same column for a 1
                                for (row in 0 until bingoBoards[boardNumber].size) {
                                    if (boardsToMark[boardNumber][row][column] == 0) {
                                        // the column contains a zero, so break out of loop
                                        break
                                    } else {
                                        // column contains a one
                                        if (row == (bingoBoards[boardNumber].size - 1)) {
                                            // made it to bottom of column and all were 1's
                                            return sumOfUnmarkedNumbersOnBoard(
                                                bingoBoards[boardNumber],
                                                boardsToMark[boardNumber]
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    } else {
                        break
                    }
                }
            }

        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val randomNumbersList = getRandomNumbersList(input[0])
        val bingoBoards = getBingoBoards(input)
        var boardsToMark = Array(bingoBoards.size) {
            Array(
                bingoBoards[0].size
            ) { IntArray(bingoBoards[0][0].size) { 0 } }
        }

        var score = -1
        for (number in randomNumbersList) {
            boardsToMark = markTheBingoBoards(number, bingoBoards, boardsToMark)

            val winningSum = getSumIfWinningBoard(bingoBoards, boardsToMark)
            if (winningSum != -1) {
                score = winningSum * number
                break
            }
        }
        return score
    }

    fun allBoardsHaveWon(boardsThatHaveWon: Array<Int>): Boolean {
        return !boardsThatHaveWon.contains(0)
    }

    fun getSumOfLastWinningBoard(
        bingoBoards: ArrayList<List<List<Int>>>,
        boardsToMark: Array<Array<IntArray>>,
        boardsThatHaveWon: Array<Int>
    ): Int {
        val winningList = intArrayOf(1, 1, 1, 1, 1)
        for (boardNumber in 0 until bingoBoards.size) {
            if (boardsThatHaveWon[boardNumber] == 0) {
                for (lineNumber in 0 until bingoBoards[boardNumber].size) {
                    for (column in 0 until bingoBoards[boardNumber][lineNumber].size) {
                        val columnNumber = boardsToMark[boardNumber][lineNumber].indexOf(1)
                        if (columnNumber != -1) {
                            if (boardsToMark[boardNumber][lineNumber].contentEquals(winningList)) {
                                // the line is a winning line, all 1's
                                boardsThatHaveWon[boardNumber] = 1
                                if (allBoardsHaveWon(boardsThatHaveWon)) {
                                    return sumOfUnmarkedNumbersOnBoard(
                                        bingoBoards[boardNumber],
                                        boardsToMark[boardNumber]
                                    )
                                } else {
                                    break
                                }
                            } else {
                                if (lineNumber != 0) {
                                    // if we aren't starting the check from the first line the column has a zero in it
                                    break
                                } else {
                                    // starting from the first line, check the lines below in the same column for a 1
                                    for (row in 0 until bingoBoards[boardNumber].size) {
                                        if (boardsToMark[boardNumber][row][column] == 0) {
                                            // the column contains a zero, so break out of loop
                                            break
                                        } else {
                                            // column contains a one
                                            if (row == (bingoBoards[boardNumber].size - 1)) {
                                                // made it to bottom of column and all were 1's
                                                boardsThatHaveWon[boardNumber] = 1
                                                if (allBoardsHaveWon(boardsThatHaveWon)) {
                                                    return sumOfUnmarkedNumbersOnBoard(
                                                        bingoBoards[boardNumber],
                                                        boardsToMark[boardNumber]
                                                    )
                                                } else {
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                            break
                        }
                    }
                }
            }

        }

        return -1
    }

    fun part2(input: List<String>): Int {
        val randomNumbersList = getRandomNumbersList(input[0])
        val bingoBoards = getBingoBoards(input)
        var boardsToMark = Array(bingoBoards.size) {
            Array(
                bingoBoards[0].size
            ) { IntArray(bingoBoards[0][0].size) { 0 } }
        }
        val boardsThatHaveWon = Array(bingoBoards.size) { 0 }


        var score = -1
        for (number in randomNumbersList) {
            boardsToMark = markTheBingoBoards(number, bingoBoards, boardsToMark)

            val winningSum = getSumOfLastWinningBoard(bingoBoards, boardsToMark, boardsThatHaveWon)
            if (winningSum != -1) {
                score = winningSum * number
                break
            }
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}

