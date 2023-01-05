
import com.github.leokash.adventofcode.utils.Logger
import com.github.leokash.adventofcode.utils.geometry.points.ints.Point
import com.github.leokash.adventofcode.utils.readLines
import kotlin.math.max
import kotlin.math.min

private const val PART_ONE_EXPECTED = 110
private const val PART_TWO_EXPECTED = 20

fun main() {
    Logger.debug = true

    fun part1(input: List<String>): Int {
        val broker = ElfBroker()
        val elfList = parse(input, broker)

        repeat(10) {
            elfList.onEach { elf -> elf.computeProposal() }
            broker.finalizeRound()
        }

        return broker.score()
    }
    fun part2(input: List<String>): Int {
        var round = 1
        val broker = ElfBroker()
        val elfList = parse(input, broker)
        while (true) {
            elfList.onEach { it.computeProposal() }
            if (!broker.finalizeRound()) return round
            round++
        }
    }

    val input = readLines("Day23")
    val sample = readLines("Day23-Test")

    check(part1(sample) == PART_ONE_EXPECTED)
    println(part1(input))

    check(part2(sample) == PART_TWO_EXPECTED)
    println(part2(input))
}

private sealed class MoveRule {
    abstract val offset: Point
    abstract val directions: List<Dirs>

    fun validate(neighbors: List<Pair<Dirs, Point>>, provider: TileStateProvider): Boolean {
        return neighbors
            .filter { (d, _) -> d in directions }
            .none { (_, p) -> provider.tileOccupied(p) }
    }
}
private sealed class Dirs(val point: Point) {
    object East: Dirs(Point(1, 0))
    object West: Dirs(Point(-1, 0))
    object North: Dirs(Point(0, -1))
    object South: Dirs(Point(0, 1))
    object NorthEast: Dirs(Point(1, -1))
    object NorthWest: Dirs(Point(-1, -1))
    object SouthEast: Dirs(Point(1, 1))
    object SouthWest: Dirs(Point(-1, 1))

    companion object {
        val all: List<Dirs> get() = listOf(East, West, North, South, NorthEast, NorthWest, SouthEast, SouthWest)
    }
}

private object East: MoveRule() {
    override val offset: Point = Dirs.East.point
    override val directions = listOf(Dirs.East, Dirs.NorthEast, Dirs.SouthEast)
}
private object West: MoveRule() {
    override val offset: Point = Dirs.West.point
    override val directions = listOf(Dirs.West, Dirs.NorthWest, Dirs.SouthWest)
}
private object North: MoveRule() {
    override val offset: Point = Dirs.North.point
    override val directions = listOf(Dirs.North, Dirs.NorthEast, Dirs.NorthWest)
}
private object South: MoveRule() {
    override val offset: Point = Dirs.South.point
    override val directions = listOf(Dirs.South, Dirs.SouthEast, Dirs.SouthWest)
}

private fun Point.tiles(): List<Pair<Dirs, Point>> {
    return Dirs.all.map { it to (this + it.point) }
}

private interface Candidate {
    fun attemptProposedMove(location: Point)
    fun stateChangedInLocation(location: Point, approved: Boolean)
}
private interface TileStateProvider {
    fun tileOccupied(location: Point): Boolean
}

private class Elf(
    private var current: Point,
    private val broker: ElfBroker,
    rules: List<MoveRule> = listOf(North, South, West, East)
): Candidate {
    private var proposed: Point? = null
    private val destinationRules = rules.toMutableList()

    init {
        broker.register(current)
    }

    fun computeProposal() {
        fun rotateRules() {
            destinationRules.removeFirst().also { destinationRules.add(it) }
        }

        val neighbors = current.tiles()
        if (neighbors.none { (_, p) -> broker.tileOccupied(p) }) { rotateRules(); return }
        val rule = destinationRules.firstOrNull { it.validate(neighbors, broker) }.also { _ -> rotateRules() } ?: return
        proposed = (current + rule.offset).also { broker.processProposedLocation(it, this) }
    }

    override fun attemptProposedMove(location: Point) {
        proposed?.let {
            proposed = null
            broker.acknowledgeMove(current, location)
            current = location
        }
    }
    override fun stateChangedInLocation(location: Point, approved: Boolean) {
        proposed?.let {
            proposed = if (approved) location else null
        }
    }
}

private class ElfBroker: TileStateProvider {
    private class Tile(
        var occupied: Boolean = false,
        var candidates: MutableList<Candidate> = mutableListOf()
    )

    private val locations = mutableMapOf<Point, Tile>()

    fun score(): Int {
        var count = 0
        val (xBounds, yBounds) = ranges()
        yBounds.forEach { y ->
            xBounds.forEach { x ->
                if (locations[Point(x, y)]?.occupied != true) count++
            }
        }

        return count
     }
    private fun ranges(): Pair<IntRange, IntRange> {
        var minX = 0; var maxX = 0
        var minY = 0; var maxY = 0
        for ((p, tile) in locations) {
            if (!tile.occupied) continue
            minX = min(p.x, minX); maxX = max(p.x, maxX)
            minY = min(p.y, minY); maxY = max(p.y, maxY)
        }

        return Pair(minX..maxX, minY..maxY)
    }

    fun finalizeRound(): Boolean {
        var moved = false
        for ((p, tile) in locations) {
            if (tile.occupied) continue
            if (tile.candidates.size == 1) {
                moved = true
                tile.candidates.first().attemptProposedMove(p)
            }
            tile.candidates.clear()
        }

        return moved
    }
    fun register(location: Point) {
        locations[location] = Tile(true)
    }
    fun acknowledgeMove(src: Point, dest: Point) {
        locations[src] = Tile()
        locations[dest] = Tile(true)
    }
    fun processProposedLocation(location: Point, candidate: Candidate) {
        with (locations.getOrPut(location) { Tile() }) {
            if (!occupied) { candidates.add(candidate) }
            val valid = !occupied && candidates.size == 1
            candidates.onEach { it.stateChangedInLocation(location, valid) }
        }
    }

    override fun tileOccupied(location: Point): Boolean {
        return locations.getOrPut(location) { Tile() }.occupied
    }
}

private fun parse(input: List<String>, broker: ElfBroker): List<Elf> {
    return input.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, c -> if (c == '#') Elf(Point(x, y), broker) else null }
    }
}
