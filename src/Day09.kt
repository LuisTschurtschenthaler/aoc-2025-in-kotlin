import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        return input.findLargestRectangle()
    }

    fun part2(input: List<String>): Long {
        return input.findLargestRectangleInside()
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private data class Point(val x: Int, val y: Int)

private fun Point.rectangleArea(other: Point): Long {
    val width = abs(x - other.x).toLong() + 1
    val height = abs(y - other.y).toLong() + 1
    return (width * height)
}

private fun List<String>.findLargestRectangle(): Long {
    val redTiles = map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        Point(x, y)
    }

    var maxArea = 0L

    for(i in redTiles.indices) {
        for(j in i + 1 until redTiles.size) {
            val area = redTiles[i].rectangleArea(redTiles[j])
            maxArea = maxOf(maxArea, area)
        }
    }

    return maxArea
}

private fun List<String>.findLargestRectangleInside(): Long {
    val vertices = map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        Point(x, y)
    }

    val xCoords = vertices.map { it.x }.distinct().sorted()
    val yCoords = vertices.map { it.y }.distinct().sorted()

    val xIndex = xCoords.withIndex().associate { it.value to it.index }
    val yIndex = yCoords.withIndex().associate { it.value to it.index }

    val n = xCoords.size
    val m = yCoords.size

    val vertexSet = vertices.toSet()
    val edges = vertices.indices.map { i ->
        vertices[i] to vertices[(i + 1) % vertices.size]
    }

    val intersectionValid = Array(n) { i ->
        BooleanArray(m) { j ->
            val x = xCoords[i]
            val y = yCoords[j]
            isPointValid(x, y, vertexSet, edges, vertices)
        }
    }

    val cellValid = Array(n - 1) { i ->
        BooleanArray(m - 1) { j ->
            val cx = (xCoords[i].toDouble() + xCoords[i + 1]) / 2.0
            val cy = (yCoords[j].toDouble() + yCoords[j + 1]) / 2.0
            isPointInPolygon(cx, cy, vertices)
        }
    }

    var maxArea = 0L

    for(i in vertices.indices) {
        for(k in i + 1 until vertices.size) {
            val p1 = vertices[i]
            val p2 = vertices[k]

            val minXIdx = min(xIndex[p1.x]!!, xIndex[p2.x]!!)
            val maxXIdx = max(xIndex[p1.x]!!, xIndex[p2.x]!!)
            val minYIdx = min(yIndex[p1.y]!!, yIndex[p2.y]!!)
            val maxYIdx = max(yIndex[p1.y]!!, yIndex[p2.y]!!)

            val allIntersectionsValid = (minXIdx..maxXIdx).all { xi ->
                (minYIdx..maxYIdx).all { yi ->
                    intersectionValid[xi][yi]
                }
            }

            val allCellsValid = allIntersectionsValid && (minXIdx until maxXIdx).all { xi ->
                (minYIdx until maxYIdx).all { yi ->
                    cellValid[xi][yi]
                }
            }

            if(allCellsValid) {
                val area = p1.rectangleArea(p2)
                maxArea = maxOf(maxArea, area)
            }
        }
    }

    return maxArea
}

private fun isPointValid(
    x: Int,
    y: Int,
    vertexSet: Set<Point>,
    edges: List<Pair<Point, Point>>,
    vertices: List<Point>
): Boolean {
    if(Point(x, y) in vertexSet) return true

    for((v1, v2) in edges) {
        if(isOnSegment(x, y, v1, v2)) return true
    }

    return isPointInPolygon(x.toDouble(), y.toDouble(), vertices)
}

private fun isOnSegment(x: Int, y: Int, p1: Point, p2: Point): Boolean {
    return when {
        p1.x == p2.x -> x == p1.x && y in min(p1.y, p2.y)..max(p1.y, p2.y)
        p1.y == p2.y -> y == p1.y && x in min(p1.x, p2.x)..max(p1.x, p2.x)
        else -> false
    }
}

private fun isPointInPolygon(x: Double, y: Double, vertices: List<Point>): Boolean {
    var inside = false
    val n = vertices.size

    var j = n - 1
    for(i in 0 until n) {
        val xi = vertices[i].x.toDouble()
        val yi = vertices[i].y.toDouble()
        val xj = vertices[j].x.toDouble()
        val yj = vertices[j].y.toDouble()

        if(((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) {
            inside = !inside
        }

        j = i
    }

    return inside
}
