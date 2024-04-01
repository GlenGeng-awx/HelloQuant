import indicators.*
import klinestore.KLineStore

fun main() {
//    for (stockName in stockNames) {
//        SandBox(stockName, "day", "2023-12-01", "2024-12-01").run()
//    }
//
    val kLine = KLineStore().load("bili", "day")

    val (upper, ma, lower) = calculateBBand(kLine.list.map { it.c }, 20, 2)

    for (idx in kLine.list.indices) {
        println("${secondToDate(kLine.list[idx].k)} with upper ${upper[idx]}, ma ${ma[idx]}, lower ${lower[idx]}")
    }
}
