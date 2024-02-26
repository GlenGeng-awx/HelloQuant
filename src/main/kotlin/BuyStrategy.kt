class BuyStrategy(
    private val stockName: String,
    private val type: String,
    private val kLine: KLine,
) {
    // at idx
    fun buy(idx: Int, amount: Int): Int {
        val share = (amount / kLine.list[idx].o).toInt()
//        println("$stockName-$type: buy $share share with " +
//                "${kLine.list[idx].o} on ${secondToDate(kLine.list[idx].k)}")
        return share
    }

    // at [idx, idx + range)
    fun consecutiveBuy(idx: Int, range: Int, amountPerDay: Int): Int {
        var shares = 0
        for (i in idx..<idx + range) {
            shares += buy(i, amountPerDay)
        }
        return shares
    }

    // as of idx
    fun profitAndLossAsOf(idx: Int, totalAmount: Int, shares: Int): Int {
        return ((shares * kLine.list[idx].o - totalAmount) * 100 / totalAmount).toInt()
    }
}