
import com.github.leokash.adventofcode.utils.*
import com.github.leokash.adventofcode.utils.collections.product
import com.github.leokash.adventofcode.utils.math.context.Context

private const val PART_ONE_EXPECTED_A = 32000000L
private const val PART_ONE_EXPECTED_B = 11687500L

enum class Pulse { LOW, HIGH }

private interface Module {
    val name: String
    val destinations: List<String>

    fun handle(pulse: Pulse, sender: String, response: (String, String, Pulse) -> Unit)

    companion object {
        operator fun invoke(string: String, inputs: Map<String, Int>): Module {
            val (m, c) = string.split(" -> ")
            val connections = c.trim().split(",").map { it.trim() }
            return when (m[0]) {
                '%' -> FlipFlop(m.drop(1).trim(), connections)
                '&' -> m.drop(1).trim().let { Conjunction(inputs.getValue(it), it, connections) }
                else -> if (m == "broadcaster") Broadcaster(connections) else Output(m)
            }
        }
    }
}

data class Output(override val name: String): Module {
    private var lastPulse: Pulse? = null
    override val destinations: List<String> = emptyList()

    override fun handle(pulse: Pulse, sender: String, response: (String, String, Pulse) -> Unit) {
        lastPulse = pulse
    }
}

data class Broadcaster(override val destinations: List<String>): Module {
    override val name: String = "broadcaster"

    override fun handle(pulse: Pulse, sender: String, response: (String, String, Pulse) -> Unit) {
        destinations.forEach { name ->
            response(this.name, name, pulse)
        }
    }
}

data class FlipFlop(override val name: String, override val destinations: List<String>): Module {
    private var state = false

    override fun handle(pulse: Pulse, sender: String, response: (String, String, Pulse) -> Unit) {
        if (pulse == Pulse.LOW) {
            state = !state
            val np = if (state) Pulse.HIGH else Pulse.LOW
            destinations.forEach { response(name, it, np) }
        }
    }
}

data class Conjunction(val inputs: Int, override val name: String, override val destinations: List<String>): Module {
    private var pulses = mutableMapOf<String, Pulse>()

    override fun handle(pulse: Pulse, sender: String,  response: (String, String, Pulse) -> Unit) {
        pulses[sender] = pulse
        val np = if (pulses.values.count { it == Pulse.HIGH } == inputs) Pulse.LOW else Pulse.HIGH
        destinations.forEach { response(name, it, np) } }
}

fun main() {
    Logger.debug = true
    data class Machine(private val modules: Map<String, Module>) {
        private var lows: Long = 0
        private var highs: Long = 0
        val score: Long get() = lows * highs

        fun simulate(listener: (String, String, Pulse) -> Unit = { _, _, _ -> }) {
            val queue = mutableListOf(
                Triple("button","broadcaster", Pulse.LOW)
            )

            while (queue.isNotEmpty()) {
                val (src, dest, pulse) = queue.removeFirst()

                when (pulse) {
                    Pulse.LOW -> lows++
                    Pulse.HIGH -> highs++
                }

                listener(src, dest, pulse)
                modules[dest]?.handle(pulse, src) { ns, nd, np ->
                    queue.add(Triple(ns, nd, np))
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        return Machine(parse(input))
            .apply { (0..<1000).onEach { simulate() } }
            .score
    }

    fun part2(input: List<String>): Long = with(parse(input)) {
        var count = 0L
        var foundAll = false

        val cycles = mutableListOf<Long>()
        val machine = Machine(this)
        val sources = mutableSetOf<String>()
        val rxInput = this.values.first { "rx" in it.destinations }.name
        val modulesToWatch = this.values.filter { rxInput in it.destinations }.map { it.name }

        while (!foundAll) {
            count++
            machine.simulate { source, dest, pulse ->
                if (dest == rxInput && pulse == Pulse.HIGH && source in modulesToWatch) {
                    cycles += count
                    sources += source
                    if (sources.size == modulesToWatch.size) {
                        foundAll = true
                    }
                }
            }
        }

        return cycles.product(Context<Long>()) { it }
    }

    val input = readLines("Day20")
    check(part1(readLines("Day20-Test1A")) == PART_ONE_EXPECTED_A)
    check(part1(readLines("Day20-Test1B")) == PART_ONE_EXPECTED_B)

    println(part1(input))
    println(part2(input))
}

private fun parse(input: List<String>): Map<String, Module> {
    fun processInputs(): Map<String, Int> = buildMap {
        input.forEach { string ->
            val (_, dest) = string.split(" -> ")
            dest.split(",").map { it.trim() }.forEach { m ->
                this[m] = this.getOrDefault(m, 0) + 1
            }
        }
    }

    val inputs = processInputs()
    return input.map { Module(it, inputs) }.associateBy { it.name }
}
