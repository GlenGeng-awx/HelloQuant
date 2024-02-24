class KLineOpUtil(private val kLine: KLine) {
    // from and to is in the format of "yyyy-MM-dd"
    fun filterByDateRange(idx: Int, from: String?, to: String?): Boolean {
        val fromSecond = from?.let { dateToSecond(it) } ?: 0
        val toSecond = to?.let { dateToSecond(it) } ?: Long.MAX_VALUE

        return kLine.list[idx].k in fromSecond..toSecond
    }

    // return idx in KLine where k equals or is the first one greater than the specified date.
    fun dateToIdx(date: String): Int {
        val second = dateToSecond(date)
        return kLine.list.indexOfFirst { it.k >= second }
    }
}