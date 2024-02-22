class KLineStore(
    private val fileStore: KLineFileStore,
    private val futuStore: KLineFutuStore,
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