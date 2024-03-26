package indicators

import KLine

// return (DIF, DEA, MACD)
fun calculateMACD(kLine: KLine,
                  shortN: Int = 12,
                  longN: Int = 26,
                  signalN: Int = 9):
        Triple<List<Double>, List<Double>, List<Double>> {
    // EMA 12
    val shortEMA = calculateEMA(kLine.list.map { it.c }, shortN)

    // EMA 26
    val longEMA = calculateEMA(kLine.list.map { it.c }, longN)

    // DIF = EMA 12 - EMA 26
    val dif = (0..<kLine.list.size).map { shortEMA[it] - longEMA[it] }

    // DEA = EMA 9 of DIF
    val dea = calculateEMA(dif, signalN)

    // MACD = DIF - DEA
    val macd = (0..<kLine.list.size).map { dif[it] - dea[it] }

    return Triple(dif, dea, macd)
}
