

fun initKLine() {
    val kLineFileStore = KLineFileStore()
    val kLineFutuStore = KLineFutuStore()
    val kLineStore = KLineStore(kLineFileStore, kLineFutuStore)

    val stockName = listOf("bili", "tsla", "coinbase", "baba", "bidu", "mrna", "edu", "meta", "pdd")
    val type = listOf("day", "week", "month")

    for (s in stockName) {
        for (t in type) {
            kLineStore.load(s, t)
        }
    }
}


fun main() {
    initKLine()
}
