
private val map = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

private typealias Cursor = Int

private fun String.skip(char: Char, cursor: Cursor): Cursor {
    for (i in (cursor until length)) {
        if (char != this[i])
            return cursor
    }

    return length
}

private fun String.int(cursor: Cursor, size: Int): Pair<Cursor, Int> {
    val end = cursor + size
    return if (end >= length) end to -1 else end to substring(cursor, end).toInt(2)
}
private fun String.literal(cursor: Cursor): Pair<Cursor, Long?> {
    for (i in (cursor .. length) step 5) {
        if (this[i] == '0') {
            val end = i + 5
            return end to substring(cursor, end).chunked(5).joinToString("") { it.takeLast(4) }.toLong(2)
        }
    }

    return cursor to null
}

enum class Op {
    Eq, Lt, Gt, Min, Max, Sum, Prod;

    companion object {
        fun from(idx: Int): Op {
            return when (idx) {
                0 -> Sum
                1 -> Prod
                2 -> Min
                3 -> Max
                5 -> Gt
                6 -> Lt
                7 -> Eq
                else -> throw RuntimeException("Invalid Operation: $idx")
            }
        }
    }
}
sealed interface Packet {
    val version: Int

    fun eval(): Long
    fun count(): Long = 1
}
data class Literal(override val version: Int, val value: Long): Packet {
    override fun eval(): Long {
        return value
    }
}
data class Container(override val version: Int, val operation: Op, val packets: List<Packet>): Packet {
    override fun eval(): Long {
        return when (operation) {
            Op.Eq -> if (packets[0].eval() == packets[1].eval()) 1 else 0
            Op.Lt -> if (packets[0].eval()  < packets[1].eval()) 1 else 0
            Op.Gt -> if (packets[0].eval()  > packets[1].eval()) 1 else 0
            Op.Min -> packets.minOf { it.eval() }
            Op.Max -> packets.maxOf { it.eval() }
            Op.Sum -> packets.sumOf { it.eval() }
            Op.Prod -> packets.fold(1L) { acc, packet -> acc * packet.eval() }
        }
    }
    override fun count(): Long {
        return packets.sumOf { it.count() }
    }
}

private fun decodeBinary(binary: String): List<Packet> {
    return buildList {
        var next = 0
        while (true) {
            val (idx, version) = binary.int(next, 3)
            if (version == -1) break
            decodeBinary(version, binary, idx).let { (cur, packet) ->
                next = cur.let { binary.skip('0', it) }
                packet?.let { add(it) }
            }
        }
    }
}
private fun decodeBinary(version: Int, binary: String, cursor: Cursor): Pair<Cursor, Packet?> {
    val (tCursor, typeId) = binary.int(cursor, 3)
    return when (typeId) {
        4 -> {
            val (idx, literal) = binary.literal(tCursor)
            idx to literal?.let { Literal(version, it) }
        }

        else -> {
            val (iCursor, iType) = binary.int(tCursor, 1)
            when (iType) {
                0 -> {
                    val (idx, size) = binary.int(iCursor, 15)
                    if (size == -1) idx to null else (idx+size) to Container(version, Op.from(typeId), decodeBinary(binary.substring(idx, idx+size)))
                }
                1 -> {
                    val (idx,needed) = binary.int(iCursor, 11)
                    val (next, packets) = decodeBinary(binary, idx, needed)
                    next to Container(version, Op.from(typeId), packets)
                }
                else -> iCursor to null
            }
        }
    }
}
private fun decodeBinary(binary: String, cursor: Cursor, needed: Int): Pair<Cursor, List<Packet>> {
    var idx = cursor
    val packets = mutableListOf<Packet>()
    while (true) {
        val (vCur, version) = binary.int(idx, 3)
        if (version == -1) break
        val (cur, packet) = decodeBinary(version, binary, vCur)

        idx = cur
        packet?.let { packets.add(it) }
        if (packets.size == needed) break
    }

    return idx to packets
}

fun main() {
    fun part1(input: String): Int {
        fun Packet.versionSum(): Int {
            return when (this) {
                is Literal -> version
                is Container -> version + packets.sumOf { it.versionSum() }
            }
        }
        return decodeBinary(input.trim().map { map[it] }.joinToString("")).sumOf { it.versionSum() }
    }
    fun part2(input: String): Long {
        return decodeBinary(input.trim().map { map[it] }.joinToString("")).sumOf { it.eval() }
    }

    check(part1("D2FE28") == 6)
    check(part1("38006F45291200") == 9)
    check(part1("EE00D40C823060") == 14)
    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    check(part2("C200B40A82") == 3L) //finds the sum of 1 and 2, resulting in the value 3.
    check(part2("04005AC33890") == 54L) //finds the product of 6 and 9, resulting in the value 54.
    check(part2("880086C3E88112") == 7L) //finds the minimum of 7, 8, and 9, resulting in the value 7.
    check(part2("CE00C43D881120") == 9L) //finds the maximum of 7, 8, and 9, resulting in the value 9.
    check(part2("D8005AC2A8F0") == 1L) //produces 1, because 5 is less than 15.
    check(part2("F600BC2D8F") == 0L) //produces 0, because 5 is not greater than 15.
    check(part2("9C005AC2F8F0") == 0L) //produces 0, because 5 is not equal to 15.
    check(part2("9C0141080250320F1802104A08") == 1L) //produces 1, because 1 + 3 = 2 * 2.

    val input = readText("Day16")

    println(part1(input))
    println(part2(input))
}
