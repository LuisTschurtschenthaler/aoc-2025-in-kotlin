fun main() {
    fun part1(input: List<String>): Long {
        return input[0].split(",")
            .flatMap { range ->
                val (start, end) = range.split("-").map { it.toLong() }
                (start..end).filter { it.isInvalidIdPart1() }
            }
            .sum()
    }

    fun part2(input: List<String>): Long {
        return input[0].split(",")
            .flatMap { range ->
                val (start, end) = range.split("-").map { it.toLong() }
                (start..end).filter { it.isInvalidIdPart2() }
            }
            .sum()
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun Long.isInvalidIdPart1(): Boolean {
    val str = toString()
    if(str.length % 2 != 0) return false

    val mid = str.length / 2
    return (str.take(mid) == str.substring(mid))
}

private fun Long.isInvalidIdPart2(): Boolean {
    val str = toString()
    val length = str.length

    val candidates = 1..(length / 2)
    return candidates
        .filter { length % it == 0 }
        .any { patternLength ->
            val pattern = str.take(patternLength)
            str == pattern.repeat(length / patternLength)
        }
}
