class KLineStore(
    private val fileStore: KLineFileStore = KLineFileStore(),
    private val futuStore: KLineFutuStore = KLineFutuStore(),
) {

    fun load(stockName: String, type: String): KLine {
        var kLine = fileStore.load(stockName, type)
        if (kLine != null) {
            return kLine
        }

        kLine = futuStore.load(stockName, type)
        fileStore.store(stockName, type, kLine)
        return kLine
    }
}

val stockNames = listOf(
    "bili",
    "tsla",
    "coinbase",
    "baba",
    "bidu",
    "mrna",
    "edu",
    "meta",
    "pdd",
    "rivn",
)

val types = listOf("day", "week", "month")