
@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

private object Dummy
fun readInput(name: String) = Dummy::class.java.classLoader.getResource("${name}.txt").readText().lines()
