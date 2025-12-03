fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { it.maxJoltage() }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { it.maxJoltageWithCount(12) }
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun String.maxJoltage(): Long {
    return indices.flatMap { i ->
        (i + 1 until length).map { j ->
            "${this[i]}${this[j]}".toLong()
        }
    }.max()
}

private fun String.maxJoltageWithCount(count: Int): Long {
    var currentIndex = 0
    return (0 until count)
        .map { position ->
            val remainingNeeded = (count - position)
            val searchEnd = (length - remainingNeeded + 1)

            val maxIndex = (currentIndex until searchEnd).maxBy { this[it] }
            currentIndex = maxIndex + 1
            this[maxIndex]
        }
        .joinToString("")
        .toLong()
}
