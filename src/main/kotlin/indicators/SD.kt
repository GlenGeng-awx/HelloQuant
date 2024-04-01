package indicators

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * SD is Standard Deviation
 */
fun calculateSD(line: List<Double>, n: Int): List<Double> {
    val sd = mutableListOf<Double>()
    for (idx in line.indices) {
        if (idx < n - 1) {
            sd.add(0.0)
        } else {
            val mean = line.subList(idx - n + 1, idx + 1).average()
            val sum = line.subList(idx - n + 1, idx + 1).sumOf { (it - mean).pow(2) }
            sd.add(sqrt(sum / n))
        }
    }
    return sd
}