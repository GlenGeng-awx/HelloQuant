package indicators

fun calculateEMA(line: List<Double>, n: Int): List<Double> {
    val alpha: Double = 2 / (n + 1).toDouble()

    val ema = mutableListOf<Double>()
    ema.add(line[0])

    for (idx in 1..<line.size) {
        ema.add(ema[idx - 1] * (1 - alpha) + line[idx] * alpha)
    }
    return ema
}
