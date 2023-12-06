
import com.github.leokash.adventofcode.utils.*
import kotlin.math.abs

private const val PART_ONE_EXPECTED = 35L
private const val PART_TWO_EXPECTED = 46L

private val seedsRegex = """seeds:((\s\d+)+)""".toRegex()

private data class Entry(val src: Long, val dst: Long, val size: Int) {
    companion object {
        fun from(string: String): Entry {
            return string
                .trim()
                .split("\\s+".toRegex())
                .let { Entry(it[0].toLong(), it[1].toLong(), it[2].toInt()) }
        }
    }
}

fun main() {
    fun String.entries(key: String): List<Entry> {
        val rgx = """$key map:\n((\d+\s?)+)\n?""".toRegex()
        return matchingGroups(rgx)
            .first()
            .replace("$key map: ", "")
            .split("\n")
            .filter { it.isNotBlank() }
            .map { Entry.from(it) }
    }

    fun getId(src: Long, list: List<Entry>): Long {
        for (entry in list) {
            if (src >= entry.dst && src <= (entry.dst + entry.size))
                return entry.src + abs(entry.dst - src)
        }

        return src
    }

    fun compute(input: String, partOne: Boolean): Long {
        val starters = input.matchingGroups(seedsRegex)
            .first()
            .replace("seeds: ", "")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }

        val seedToSoil = input.entries("seed-to-soil")
        val soilToFertilizer = input.entries("soil-to-fertilizer")
        val fertilizerToWater = input.entries("fertilizer-to-water")
        val waterToLight = input.entries("water-to-light")
        val lightToTemperature = input.entries("light-to-temperature")
        val temperatureToHumidity = input.entries("temperature-to-humidity")
        val humidityToLocation = input.entries("humidity-to-location")

        fun getLocation(start: Long): Long {
            val sId = getId(start, seedToSoil)
            val fId = getId(sId, soilToFertilizer)
            val wId = getId(fId, fertilizerToWater)
            val lId = getId(wId, waterToLight)
            val tId = getId(lId, lightToTemperature)
            val hId = getId(tId, temperatureToHumidity)
            return getId(hId, humidityToLocation)
        }

        if (partOne) {
            return starters.minOf { getLocation(it) }
        }

        return starters.chunked(2).minOf { (start, length) ->
            (0 ..< length).minOf {
                getLocation(start + it)
            }
        }
    }

    fun part1(input: String): Long = compute(input, true)
    fun part2(input: String): Long = compute(input, false)

    val input = readText("Day05")
    val inputTest = readText("Day05-Test")

    check(part1(inputTest) == PART_ONE_EXPECTED)
    check(part2(inputTest) == PART_TWO_EXPECTED)

    println(part1(input))
    println(part2(input))
}
