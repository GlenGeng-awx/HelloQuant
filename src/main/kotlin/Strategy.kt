
class Strategy {
//    fun run(stockName: String, type: String) {
//        val kLine = KLineStore().load(stockName, type)
//
//        for (idx in 0..<kLine.list.size) {
//            if (!KLineOpUtil(kLine).filterByDateRange(idx, "2023-01-01", "2024-12-31")) {
//                continue
//            }
//
//            if (KLinePattern(kLine).resistanceLevel(idx)) {
//                println("$stockName-$type: ${secondToDate(kLine.list[idx].k)} with high price ${kLine.list[idx].h} as resistance level")
//            }
//
//            if (KLinePattern(kLine).supportLevel(idx)) {
//                println("$stockName-$type: ${secondToDate(kLine.list[idx].k)} with low price ${kLine.list[idx].l} as support level")
//            }
//        }
//    }

    fun run(stockName: String, type: String) {
        val kLine = KLineStore().load(stockName, type)

        for (idx in 0..<kLine.list.size) {
            if (!KLineOpUtil(kLine).filterByDateRange(idx, "2023-01-01", "2024-12-31")) {
                continue
            }

            if (KLineOpByIdx(kLine).consecutiveHigherVolume(idx, 3)
                    && KLineOpByIdx(kLine).consecutiveHigherPrice(idx, 3)) {
                println("detect ${secondToDate(kLine.list[idx].k)} with price ${kLine.list[idx].h}")
            }
        }
    }
}
