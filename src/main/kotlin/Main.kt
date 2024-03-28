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

//    val s: String = "834ccefc-ed7d-4233-bdff-ab89256f2dd6 HKD/2024-03-27/PENDING/FEE/att_sgdvxqkqbgunndn5l9q_dn16ba"
//    val t: String = "834ccefc-ed7d-4233-bdff-ab89256f2dd6 HKD"
//    val l = t.split('/')
//
//    println("${t.contains('/')}")
//
//    l.forEach { println(it) }
//    println(l.size)
}
