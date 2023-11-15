import com.github.leokash.adventofcode.utils.readLines

private fun List<Char>.parse(): Char {
    return when (size) {
        2 -> '1'
        3 -> '7'
        4 -> '4'
        7 -> '8'
        else -> {
            when(val sorted = sorted().joinToString("")) {
                "abcefg" -> '0'
                "acdeg"  -> '2'
                "acdfg"  -> '3'
                "abdfg"  -> '5'
                "abdefg" -> '6'
                "abcdfg" -> '9'
                else -> throw RuntimeException("Invalid digit: $this, sorted: $sorted")
            }
        }
    }
}

private fun parseSegments(input: List<String>): Map<Char, Char> {
    return buildMap {
        val array = Array(7) { ' ' }
        val groups = input.groupBy { it.length }

        var bd = "  "
        val cf = groups[2]?.first()?.also { cf ->
            groups[3]?.first()?.let { acf ->
                array[0] = acf.first { it !in cf }
            }
            groups[4]?.first()?.let { tmp ->
                bd = tmp.filter { it !in cf }
            }
        } ?: "  "
        groups[6]?.find { cf[0] in it != cf[1] in it }?.let { string ->
            //6: missing => C
            array[2] = if (cf[0] in string) cf[1] else cf[0]
            array[5] = if (cf[0] in string) cf[0] else cf[1]
        }
        groups[6]?.find { bd[0] in it != bd[1] in it }?.let { string ->
            //0: missing D
            array[3] = if (bd[0] in string) bd[1] else bd[0]
        }
        groups[4]?.first()?.let { string ->
            array[1] = string.first { it !in array }
        }

        var eg = "  "
        if (array.count { it != ' ' } == 5) {
            eg = ('a'..'g').filter { it !in array }.joinToString("")
        }
        groups[6]?.find { eg[0] in it != eg[1] in it }?.let { string ->
            //9: missing => E
            array[4] = if (eg[0] in string) eg[1] else eg[0]
        }

        if (array.count { it != ' ' } == 6) {
            array[array.indexOf(' ')] = ('a'..'g').first { it !in array }
        }

        for (i in (0 until 7)) {
            when (i) {
               0 -> this[array[i]] = 'a'
               1 -> this[array[i]] = 'b'
               2 -> this[array[i]] = 'c'
               3 -> this[array[i]] = 'd'
               4 -> this[array[i]] = 'e'
               5 -> this[array[i]] = 'f'
               6 -> this[array[i]] = 'g'
            }
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input
            .flatMap { it.trim().split('|')[1].trim().split(' ') }
            .count { num -> num.length.let { it == 2 || it == 3 || it == 4 || it == 7 } }
    }
    fun part2(input: List<String>): Int {
        return input.fold(0) { sum, string ->
            val (data, numbers) = string.split('|')
            val segments = parseSegments(data.split(" ").map { it.trim() })
            sum + numbers.trim().split(' ').fold("") { acc, s -> acc + s.mapNotNull { segments[it] }.parse() }.toInt()
        }
    }

    val input = readLines("Day08")
    val inputTest1 = readLines("Day08-Test")
    val inputTest2 = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")//com.github.leokash.adventofcode.utils.readLines("Day08-Test")

    check(part1(inputTest1) == 26)
    check(part2(inputTest2) == 5353)
    check(part2(inputTest1) == 61229)

    println(part1(input))
    println(part2(input))
}
