
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.*
import kotlin.math.max

private const val ORE = 0
private const val CLAY = 1
private const val GEODE = 3
private const val OBSIDIAN = 2
private const val PART_ONE_EXPECTED = 33
private const val PART_TWO_EXPECTED = 3472

private typealias Pricing = Pair<Int, Int>
private val Pricing.cost: Int get() = second
private val IntArray.string: String get() = "${this[0]}${this[1]}${this[2]}${this[3]}"

private fun IntArray.copy(): IntArray = IntArray(this.size) { i -> this@copy[i] }

private fun IntArray.inc(idx: Int) { this[idx] += 1 }
private fun IntArray.collect(robots: IntArray) { robots.forEachIndexed { i, m -> this@collect[i] += m } }
private fun IntArray.buy(pricing: List<Pricing>) { pricing.forEach { (min, cost) -> this[min] -= cost } }

private fun Int.cutOffTime(quality: Boolean): Int = when(this) {
    ORE -> if (quality) 17 else 23
    CLAY -> if (quality) 7 else 11
    OBSIDIAN -> if (quality) 4 else 7
    else -> 2
}

private fun IntArray.canFulfill(pricing: List<Pricing>): Boolean {
    return pricing.all { (min, cost) -> this[min] >= cost }
}

private fun Map<Int, List<Pricing>>.possibleBots(time: Int, quality: Boolean, robots: IntArray, resources: IntArray, maxRobotsCache: Map<Int, Int>): List<Pair<Int, List<Pricing>>> {
    fun canBuildRobotForMineral(m: Int): Boolean {
        return time >= m.cutOffTime(quality) && robots[m] < maxRobotsCache.getValue(m)
    }

    return buildList {
        for (mineral in 3 downTo 0) {
            val price = this@possibleBots.getValue(mineral)
            if (canBuildRobotForMineral(mineral) && resources.canFulfill(price)) {
                add(mineral to price)
                if (mineral == GEODE) break
            }
        }
    }
}

fun main() {
    Logger.debug = false
    fun compute(blueprints: Map<Int, Map<Int, List<Pricing>>>, partOne: Boolean): Int {
        val maxRobotsCache: Map<Int, Map<Int, Int>> = blueprints.mapValues { (_, map) ->
            buildMap {
                map.forEach { (min, _)  ->
                    when(min) {
                        ORE -> put(ORE, map.entries.filter { it.key != ORE }.maxOf { it.value[0].cost })
                        CLAY -> put(CLAY, map.getValue(OBSIDIAN)[1].cost)
                        OBSIDIAN -> put(OBSIDIAN, map.getValue(GEODE)[1].cost)

                        else -> put(GEODE, Int.MAX_VALUE)
                    }
                }
            }
        }

        fun simulate(bpId: Int, time: Int, robots: IntArray, resources: IntArray, states: MutableSet<Long>): Int {
            var count = resources[GEODE]
            val cache = maxRobotsCache.getValue(bpId)
            val state = "$time${robots.string}${resources.string}".toLong()
            if (time <= 0 || state in states)
                return count

            states.add(state)
            for ((robot, price) in blueprints.getValue(bpId).possibleBots(time, partOne, robots, resources, cache)) {
                val nBots = robots.copy().apply { inc(robot) }
                val nResources = resources.copy().apply { buy(price); collect(robots) }
                count = max(count, simulate(bpId, time - 1, nBots, nResources, states))
            }

            return max(count, simulate(bpId, time - 1, robots, resources.apply { collect(robots) }, states))
        }

        return when(partOne) {
            true  -> blueprints.map { (id, _) -> id * simulate(id, 24, intArrayOf(1, 0, 0, 0), IntArray(4), mutableSetOf()) }.sum()
            false -> blueprints.keys.sorted().take(3).product { simulate(it, 32, intArrayOf(1, 0, 0, 0), IntArray(4), mutableSetOf()) }
        }
    }

    val input = readLines("Day19").let { parse(it) }
    val sample = readLines("Day19-Test").let { parse(it) }

    check(compute(sample, true) == PART_ONE_EXPECTED)
    println(compute(input, true))

    check(compute(sample, false) == PART_TWO_EXPECTED)
    println(compute(input, false))
}

private val id_regex = Regex("""Blueprint (\d+)""")
private val pricing_regex = Regex("""Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")

private fun parse(input: List<String>): Map<Int, Map<Int, List<Pricing>>> {
    return buildMap {
        input.forEach { string ->
            val id = string.matchingGroups(id_regex).first().toInt()
            this[id] = string.matchingGroups(pricing_regex)
                .map { it.toInt() }
                .let { mutableMapOf(ORE to listOf(ORE to it[0]), CLAY to listOf(ORE to it[1]), OBSIDIAN to listOf(ORE to it[2], CLAY to it[3]), GEODE to listOf(ORE to it[4], OBSIDIAN to it[5])) }
        }
    }
}
