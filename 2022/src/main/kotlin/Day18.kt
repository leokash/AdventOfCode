
import com.github.leokash.adventofcode.utils.*

private const val PART_ONE_EXPECTED = 64
private const val PART_TWO_EXPECTED = 58

private data class Vec3(val x: Int, val y: Int, val z: Int) {
    fun neighbors(): List<Vec3> {
        return buildList { // only need cardinals for now...
            add(Vec3(x - 1, y, z)) // (0, 2, 3),
            add(Vec3(x + 1, y, z)) // (2, 2, 3),
            add(Vec3(x, y - 1, z)) // (1, 1, 3),
            add(Vec3(x, y + 1, z)) // (1, 3, 3),
            add(Vec3(x, y, z - 1)) // (1, 2, 2),
            add(Vec3(x, y, z + 1)) // (1, 2, 4),
            // add(Vec3(-1,  0,  1)) // (0, 2, 4),
            // add(Vec3(-1,  1, -1)) // (0, 3, 2),
            // add(Vec3(-1,  1,  0)) // (0, 3, 3),
            // add(Vec3(-1,  1,  1)) // (0, 3, 4),
            // add(Vec3( 0, -1, -1)) // (1, 1, 2),
            // add(Vec3( 0, -1,  1)) // (1, 1, 4),
            // add(Vec3( 0,  1, -1)) // (1, 3, 2),
            // add(Vec3( 0,  1,  1)) // (1, 3, 4),
            // add(Vec3( 1, -1, -1)) // (2, 1, 2),
            // add(Vec3( 1, -1,  0)) // (2, 1, 3),
            // add(Vec3( 1, -1,  1)) // (2, 1, 4),
            // add(Vec3( 1,  0, -1)) // (2, 2, 2),
        }
    }

    operator fun plus(rhs: Vec3): Vec3 {
        return Vec3(x + rhs.x, y + rhs.y, z + rhs.z)
    }
    operator fun minus(rhs: Vec3): Vec3 {
        return Vec3(x - rhs.x, y - rhs.y, z - rhs.z)
    }
    operator fun times(rhs: Vec3): Vec3 {
        return Vec3(x - rhs.x, y - rhs.y, z - rhs.z)
    }
}

fun main() {
    Logger.debug = true
    fun parse(input: List<String>): List<Vec3> {
        return input.map { val (x, y, z) = it.split(","); Vec3(x.toInt(), y.toInt(), z.toInt()) }
    }
    fun computeOne(input: List<String>): Int {
        return with(parse(input)) {
            sumOf { v -> 6 - v.neighbors().count { it in this } }
            .also { log { "surface area: $it" } }
        }
    }
    fun computeTwo(input: List<String>): Int {
        val vectors = parse(input)
        val airCache = mutableMapOf<Vec3, Boolean>()
        fun trappedAir(vec: Vec3): Boolean {
            val trapped = airCache[vec]
            if (trapped != null) return trapped

            val seen = mutableSetOf<Vec3>()
            val queue = ArrayDeque<Vec3>().apply { add(vec) }
            while (queue.isNotEmpty()) {
                val tmp = queue.removeFirst()
                if (tmp in vectors || tmp in seen) continue
                seen.add(tmp)
                if (seen.size > 7500) { // arbitrary number that seem to work for me... points can see the outside so not trapped
                    airCache.putAll(seen.map { it to false })
                    return false
                }

                queue.addAll(tmp.neighbors())
            }

            airCache.putAll(seen.map { it to true })
            return true
        }

        return vectors
            .sumOf { it.neighbors().count { v -> !trappedAir(v) } }
            .also { log { "found: $it" } }
    }

    fun part1(input: List<String>) = computeOne(input)
    fun part2(input: List<String>) = computeTwo(input)

    val input = readLines("Day18")
    val sample = readLines("Day18-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}
    