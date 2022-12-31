
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import kotlin.math.max

private const val PART_ONE_EXPECTED = 33
private const val PART_TWO_EXPECTED = 3472

private sealed class Material {
    val index: Int get() = when (this) {
        is Ore -> 0
        is Clay -> 1
        is Geode -> 3
        is Obsidian -> 2
    }
    abstract fun timeThreshold(quality: Boolean): Int

    override fun toString(): String {
        return "${this::class.simpleName}"
    }
    companion object {
        fun from(s: String): Material {
            return when (s) {
                "ore" -> Ore
                "clay" -> Clay
                "geode" -> Geode
                "obsidian" -> Obsidian
                else -> error("invalid material provided: $s")
            }
        }
    }
}

private object Ore: Material() {
    override fun timeThreshold(quality: Boolean): Int = if (quality) 17 else 23
}
private object Clay: Material() {
    override fun timeThreshold(quality: Boolean): Int = if (quality) 7 else 11
}
private object Geode: Material() {
    override fun timeThreshold(quality: Boolean): Int = 2
}
private object Obsidian: Material() {
    override fun timeThreshold(quality: Boolean): Int = if (quality) 4 else 5
}

private data class Blueprint(val id: Int, val requirements: Map<Material, List<Int>>) {
    private val oreBots = listOf(Clay, Obsidian, Geode)
    private val priority = mutableListOf(Geode, Obsidian, Clay, Ore)

    val maxOreNeeded = requirements.maxOf { (m, items) -> if (m in oreBots) items[0] else 0 }
    val maxClayNeeded = requirements.maxOf { (m, items) -> if (m is Obsidian) items[1] else 0 }
    val maxObsidianNeeded = requirements.maxOf { (m, items) -> if (m is Geode) items[2] else 0 }

    fun robotsAvailable(resources: Resources, robots: Robots, time: Int, quality: Boolean): List<Pair<Int, List<Int>>> {
        fun canBuild(robot: Material): Boolean {
            val threshold = robot.timeThreshold(quality)
            return when (robot) {
                is Ore -> time >= threshold && robots.ore < maxOreNeeded
                is Clay -> time >= threshold && robots.clay < maxClayNeeded
                is Obsidian -> time >= threshold && robots.obsidian < maxObsidianNeeded
                else -> time >= threshold
            }
        }
        fun Resources.canFulfill(req: List<Int>): Boolean {
            return ore >= req[0] && clay >= req[1] && obsidian >= req[2]
        }

        return buildList {
            for (product in priority) {
                val required = requirements[product] ?: continue
                if (canBuild(product) && resources.canFulfill(required)) {
                    add(product.index to required)
                    if (product is Geode) break
                }
            }
        }
    }
}
private data class Robots(var ore: Int = 1, var clay: Int = 0, var obsidian: Int = 0, var geode: Int = 0) {
    fun add(i: Int) {
        when (i) {
            0 -> ore += 1
            1 -> clay += 1
            2 -> obsidian += 1
            else -> geode += 1
        }
    }
    override fun toString(): String {
        return "$ore$clay$obsidian$geode"
    }
}
private data class Resources(var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geode: Int = 0) {
    fun buy(cost: List<Int>) {
        ore -= cost[0]; clay -= cost[1]; obsidian -= cost[2]
    }
    fun collect(bot: Robots) {
        ore += bot.ore; clay += bot.clay; obsidian += bot.obsidian; geode += bot.geode
    }

    override fun toString(): String {
        return "$ore$clay$obsidian$geode"
    }
}

fun main() {
    Logger.debug = false
    data class State(val time: Int, val robots: String, val resources: String)
    fun simulate(time: Int, print: Blueprint, bots: Robots = Robots(), res: Resources = Resources(), quality: Boolean, visited: MutableSet<State>): Int {
        var count = res.geode
        val state = State(time, bots.toString(), res.toString())

        if (time <= 0 || state in visited)
            return count

        visited.add(state)
        for ((robot, cost) in print.robotsAvailable(res, bots, time, quality)) {
            val nBot = bots.copy().apply { add(robot) }
            val nRes = res.copy().apply { buy(cost); collect(bots) }
            count = max(count, simulate(time - 1, print, nBot, nRes, quality, visited))
        }

        return max(count, simulate(time - 1, print, bots, res.apply { collect(bots) }, quality, visited))
    }

    fun part1(input: List<String>): Int {
        return parse(input).sumOf { b ->
            b.id * simulate(24, b, quality = true, visited = mutableSetOf())
        }
    }
    fun part2(input: List<String>): Int {
        return parse(input)
            .take(3)
            .product { b ->
                simulate(32, b, quality = false, visited = mutableSetOf())
            }
    }

    val input = readLines("Day19")
    val sample = readLines("Day19-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}

private val idRgx = Regex("""Blueprint (\d+)""")
private val rulesRgx = Regex("""Each (\w+) robot costs (\d+) (\w+)( and (\d+) (\w+))?""")

private fun parse(input: List<String>): List<Blueprint> {
    return input.map { string ->
        val id = string.matchingGroups(idRgx).first().toInt()
        val recipes = string
            .substringAfter(":").trim().split(".").dropLast(1).map { it.matchingGroups(rulesRgx) }
        Blueprint(id, recipes.associate { (r, c1, _, _, c2, m2) ->
            Material.from(r) to listOf(c1.toInt(), if (m2 == "clay") c2.toInt() else 0, if (m2 == "obsidian") c2.toInt() else 0)
        })
    }
}
