fun main() {
    fun part1(input: List<String>): Int {
        return input.countAccessibleRolls()
    }

    fun part2(input: List<String>): Int {
        return input.removeAccessibleRolls()
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private val directions = listOf(
    -1 to -1, -1 to 0, -1 to 1,  // top row
    0 to -1,          0 to 1,   // middle row (left and right)
    1 to -1,  1 to 0,  1 to 1   // bottom row
)

private fun List<String>.countAccessibleRolls(): Int {
    return indices.sumOf { row ->
        this[row].indices.count { col ->
            this[row][col] == '@' && isAccessible(row, col)
        }
    }
}

private fun List<String>.isAccessible(row: Int, col: Int): Boolean {
    val neighborCount = directions.count { (dr, dc) ->
        val newRow = row + dr
        val newCol = col + dc
        newRow in indices && newCol in this[0].indices && this[newRow][newCol] == '@'
    }

    return (neighborCount < 4)
}

private fun List<String>.removeAccessibleRolls(): Int {
    val rolls = indices.flatMap { row ->
        this[row].indices.mapNotNull { col ->
            if (this[row][col] == '@') row to col else null
        }
    }.toMutableSet()

    var totalRemoved = 0

    while(true) {
        val accessible = rolls.filter { (row, col) ->
            rolls.isAccessible(row, col)
        }

        if(accessible.isEmpty()) break

        totalRemoved += accessible.size
        rolls.removeAll(accessible.toSet())
    }

    return totalRemoved
}

private fun Set<Pair<Int, Int>>.isAccessible(row: Int, col: Int): Boolean {
    val neighborCount = directions.count { (dr, dc) ->
        (row + dr) to (col + dc) in this
    }

    return (neighborCount < 4)
}
