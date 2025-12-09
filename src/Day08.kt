import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Long {
        return input.connectJunctionBoxes(1000)
    }

    fun part2(input: List<String>): Long {
        return input.findLastConnection()
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
)

private fun Point3D.distanceTo(other: Point3D): Double {
    val dx = (x - other.x).toDouble()
    val dy = (y - other.y).toDouble()
    val dz = (z - other.z).toDouble()
    return sqrt(dx * dx + dy * dy + dz * dz)
}

private class UnionFind(size: Int) {
    private val parent = IntArray(size) { it }
    private val rank = IntArray(size) { 0 }

    fun find(x: Int): Int {
        if(parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)

        if(rootX == rootY) return false

        when {
            rank[rootX] < rank[rootY] -> parent[rootX] = rootY
            rank[rootX] > rank[rootY] -> parent[rootY] = rootX
            else -> {
                parent[rootY] = rootX
                rank[rootX]++
            }
        }
        return true
    }

    fun getCircuitSizes(): List<Int> {
        val circuits = mutableMapOf<Int, Int>()
        for(i in parent.indices) {
            val root = find(i)
            circuits[root] = (circuits[root] ?: 0) + 1
        }
        return circuits.values.toList()
    }

    fun getNumCircuits(): Int {
        val roots = mutableSetOf<Int>()
        for(i in parent.indices) {
            roots.add(find(i))
        }
        return roots.size
    }
}

private fun List<String>.connectJunctionBoxes(numAttempts: Int): Long {
    val boxes = map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Point3D(x, y, z)
    }

    val edges = mutableListOf<Pair<Double, Pair<Int, Int>>>()
    for(i in boxes.indices) {
        for(j in i + 1 until boxes.size) {
            val distance = boxes[i].distanceTo(boxes[j])
            edges.add(distance to (i to j))
        }
    }

    edges.sortBy { it.first }

    val uf = UnionFind(boxes.size)
    var attempts = 0

    for((_, pair) in edges) {
        if(attempts >= numAttempts) break
        val (i, j) = pair
        uf.union(i, j)
        attempts++
    }

    val sizes = uf.getCircuitSizes().sortedDescending()
    return sizes[0].toLong() * sizes[1].toLong() * sizes[2].toLong()
}

private fun List<String>.findLastConnection(): Long {
    val boxes = map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Point3D(x, y, z)
    }

    val edges = mutableListOf<Pair<Double, Pair<Int, Int>>>()
    for(i in boxes.indices) {
        for(j in i + 1 until boxes.size) {
            val distance = boxes[i].distanceTo(boxes[j])
            edges.add(distance to (i to j))
        }
    }

    edges.sortBy { it.first }

    val uf = UnionFind(boxes.size)

    for((_, pair) in edges) {
        val (i, j) = pair

        if(!uf.union(i, j)) continue
        if(uf.getNumCircuits() != 1) continue
        return boxes[i].x.toLong() * boxes[j].x.toLong()
    }

    return 0
}
