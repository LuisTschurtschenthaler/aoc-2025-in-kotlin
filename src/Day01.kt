fun main() {
    fun part1(input: List<String>): Int {
        return input.runningFold(50) { position, instruction ->
            position.rotate(instruction)
        }.count { it == 0 }
    }

    fun part2(input: List<String>): Int {
        return input.fold(50 to 0) { (position, totalZeros), instruction ->
            val zerosCrossed = position.countZerosCrossed(instruction)
            val newPosition = position.rotate(instruction)
            newPosition to (totalZeros + zerosCrossed)
        }.second
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun Int.rotate(instruction: String): Int {
    val direction = instruction[0]
    val steps = instruction.drop(1).toInt()
    return when (direction) {
        'L' -> (this - steps).mod(100)
        'R' -> (this + steps).mod(100)
        else -> this
    }
}

private fun Int.countZerosCrossed(instruction: String): Int {
    val direction = instruction[0]
    val steps = instruction.drop(1).toInt()

    return when (direction) {
        'R' -> (this + steps) / 100
        'L' -> when {
            this == 0 -> steps / 100
            steps >= this -> 1 + (steps - this) / 100
            else -> 0
        }
        else -> 0
    }
}
