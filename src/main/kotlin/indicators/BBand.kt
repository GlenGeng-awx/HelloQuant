package indicators

/**
 * BBand is Bollinger Bands
 *   upper = MA + k * SD
 *   lower = MA - k * SD
 */
fun calculateBBand(line: List<Double>, n: Int, k: Int): Triple<List<Double>, List<Double>, List<Double>> {
    val ma = calculateMA(line, n)
    val sd = calculateSD(line, n)

    val upper = mutableListOf<Double>()
    val lower = mutableListOf<Double>()

    for (idx in line.indices) {
        if (idx < n - 1) {
            upper.add(0.0)
            lower.add(0.0)
        } else {
            upper.add(ma[idx] + k * sd[idx])
            lower.add(ma[idx] - k * sd[idx])
        }
    }

    return Triple(upper, ma, lower)
}