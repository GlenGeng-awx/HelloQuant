import indicators.calculateDownSAR
import indicators.calculateSAR
import klinestore.KLineStore

fun main() {
//    for (stockName in stockNames) {
//        SandBox(stockName, "day", "2023-12-01", "2024-12-01").run()
//    }
//
    val kLine = KLineStore().load("tsla", "day")

    calculateSAR(kLine, "tsla", "day")

}
