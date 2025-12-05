fun main() {
    fun part1(input: List<String>): Int {
        val (ranges, ids) = input.parseDatabase()
        return ids.count { id -> ranges.any { range -> id in range } }
    }

    fun part2(input: List<String>): Long {
        val (ranges, _) = input.parseDatabase()
        return ranges.mergeRanges().sumOf { it.last - it.first + 1 }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parseDatabase(): Pair<List<LongRange>, List<Long>> {
    val blankIndex = indexOf("")
    val ranges = take(blankIndex).map { line ->
        val (start, end) = line.split("-").map { it.toLong() }
        (start..end)
    }

    val ids = drop(blankIndex + 1).map { it.toLong() }
    return (ranges to ids)
}

private fun List<LongRange>.mergeRanges(): List<LongRange> {
    if(isEmpty()) return emptyList()

    val sorted = sortedBy { it.first }
    val merged = mutableListOf(sorted.first())

    for(range in sorted.drop(1)) {
        val last = merged.last()
        if(range.first <= last.last + 1) {
            merged[merged.lastIndex] = last.first..maxOf(last.last, range.last)
        } else {
            merged.add(range)
        }
    }

    return merged
}
