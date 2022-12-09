
enum class Direction {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    companion object {
        val all: List<Direction> = Direction.values().toList()
        val cardinals: List<Direction> = listOf(NORTH, EAST, SOUTH, WEST)
        val ordinals: List<Direction> = listOf(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST)
    }
}
