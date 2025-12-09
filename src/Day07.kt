fun main() {
    fun part1(input: List<String>): Int {
        return input.countUniqueHitSplitters()
    }

    fun part2(input: List<String>): Long {
        return input.countQuantumTimelines()
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.countUniqueHitSplitters(): Int {
    val height = size
    val width = this[0].length

    val startRow = indexOfFirst { it.contains('S') }
    val startCol = this[startRow].indexOf('S')

    val queue = mutableListOf(startRow + 1 to startCol)
    val visitedBeams = mutableSetOf<Pair<Int, Int>>()
    val hitSplitters = mutableSetOf<Pair<Int, Int>>()

    while (queue.isNotEmpty()) {
        val (beamRow, beamCol) = queue.removeFirst()

        if(beamCol !in 0 ..< width || beamRow >= height) continue
        if((beamRow to beamCol) in visitedBeams) continue
        visitedBeams.add(beamRow to beamCol)

        for(row in beamRow until height) {
            if(this[row][beamCol] == '^') {
                hitSplitters.add(row to beamCol)
                queue.add(row + 1 to beamCol - 1)
                queue.add(row + 1 to beamCol + 1)
                break
            }
        }
    }

    return hitSplitters.size
}

private fun List<String>.countQuantumTimelines(): Long {
    val height = size
    val width = this[0].length

    val startRow = indexOfFirst { it.contains('S') }
    val startCol = this[startRow].indexOf('S')

    val memo = mutableMapOf<Pair<Int, Int>, Long>()

    fun countPathsFrom(row: Int, col: Int): Long {
        if(col !in 0 ..< width || row >= height) return 1L

        memo[row to col]?.let { return it }

        for(r in row until height) {
            if(this[r][col] == '^') {
                val leftPaths = countPathsFrom(r + 1, col - 1)
                val rightPaths = countPathsFrom(r + 1, col + 1)
                val result = leftPaths + rightPaths
                memo[row to col] = result
                return result
            }
        }

        memo[row to col] = 1L
        return 1L
    }

    return countPathsFrom(startRow + 1, startCol)
}
