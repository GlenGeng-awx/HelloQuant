import indicators.calculateMACD
import klinestore.KLineStore

fun main() {
//    for (stockName in stockNames) {
//        SandBox(stockName, "day", "2023-12-01", "2024-12-01").run()
//    }

    val kLine = KLineStore().load("tsla", "day")
    val (dif, dea, macd) = calculateMACD(kLine)

    for (i in 0..<kLine.list.size) {
        val date = secondToDate(kLine.list[i].k)
        println("$date: ${dif[i]} ${dea[i]} ${macd[i]}")
    }
}
