fun main() {
    fun part1(input: List<String>): Long {
        return input.parseProblems().sumOf { it.solve() }
    }

    fun part2(input: List<String>): Long {
        return input.parseProblemsRightToLeft().sumOf { it.solve() }
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private data class Problem(
    val numbers: List<Long>,
    val operation: Char
)

private fun List<String>.parseProblems(): List<Problem> {
    val width = maxOf { it.length }
    val problems = mutableListOf<Problem>()

    var col = 0
    while(col < width) {
        if(all { it.getOrNull(col)?.isWhitespace() != false }) {
            col++
            continue
        }

        var endCol = col
        while(endCol < width && any { it.getOrNull(endCol)?.isWhitespace() == false }) {
            endCol++
        }

        val problemText = map { it.substring(col, minOf(endCol, it.length)).padEnd(endCol - col) }
        val numbers = problemText.dropLast(1)
            .mapNotNull { it.trim().toLongOrNull() }
        val operation = problemText.last().trim().firstOrNull() ?: '+'

        problems.add(Problem(numbers, operation))
        col = endCol
    }

    return problems
}

private fun List<String>.parseProblemsRightToLeft(): List<Problem> {
    val width = maxOf { it.length }
    val problems = mutableListOf<Problem>()

    var col = 0
    while(col < width) {
        if(all { it.getOrNull(col)?.isWhitespace() != false }) {
            col++
            continue
        }

        var endCol = col
        while(endCol < width && any { it.getOrNull(endCol)?.isWhitespace() == false }) {
            endCol++
        }

        val problemText = map { it.substring(col, minOf(endCol, it.length)).padEnd(endCol - col) }
        val operation = problemText.last().trim().firstOrNull() ?: '+'

        val numbers = (endCol - col - 1 downTo 0).mapNotNull { colIndex ->
            val digitChars = problemText.dropLast(1).map { it[colIndex] }
            val numberStr = digitChars.joinToString("").trim()
            numberStr.toLongOrNull()
        }

        problems.add(Problem(numbers, operation))
        col = endCol
    }

    return problems
}

private fun Problem.solve(): Long {
    return when (operation) {
        '+' -> numbers.sum()
        '*' -> numbers.reduce { acc, n -> acc * n }
        else -> 0
    }
}
