
@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

private val loader = {}::class.java.classLoader
fun readText(name: String) = loader.getResource("${name}.txt").readText()
fun readLines(name: String) = readText(name).lines()
