package graphs

data class Edge<T>(val source: Node<T>, val destination: Node<T>, val weight: Double? = null)
