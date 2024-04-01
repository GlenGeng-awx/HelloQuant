package indicators

fun calculateMA(line: List<Double>, n: Int): List<Double> {
    val ma = mutableListOf<Double>()
    for (idx in line.indices) {
        if (idx < n - 1) {
            ma.add(0.0)
        } else {
            ma.add(line.subList(idx - n + 1, idx + 1).average())
        }
    }
    return ma
}