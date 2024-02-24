/**
 * BottomFishingStrategy
 *
 * Trigger condition: Downward breakthrough of the previous support level, reaching 20% decline.
 * Purchase strategy: Purchase continuously for 10 consecutive days, with 1000 USD each day,
 *                    and hold for 20 days.
 */
class BottomFishingStrategy(
    private val stockName: String,
    private val type: String,
    private val fromDate: String,
    private val toDate: String,
) {
    private val kLine: KLine = KLineStore().load(stockName, type)

    private fun getLastSupportLevel(idx: Int): Int? {
        for (i in idx - 1 downTo 0) {
            if (KLinePattern(kLine).supportLevel(i)) {
                return i
            }
        }
        return null
    }

    private fun triggerCondition(idx: Int): Boolean {
        val supportIdx = getLastSupportLevel(idx) ?: return false

        if (kLine.list[idx].l * 1.2 > kLine.list[supportIdx].l) {
            return false
        }

        println("\n" + "$stockName-$type: " +
                "${secondToDate(kLine.list[idx].k)} with price ${kLine.list[idx].l} broke the support level " +
                "${secondToDate(kLine.list[supportIdx].k)} with price ${kLine.list[supportIdx].l}")
        return true
    }

    private fun rangeBuy(idx: Int, range: Int, amountPerDay: Int): Int {
        var shares = 0
        for (i in idx..<idx + range) {
            val share = (amountPerDay / kLine.list[i].o).toInt()
            shares += share
//            println("Buy $share share with ${kLine.list[i].o} on ${secondToDate(kLine.list[i].k)}")
        }
        return shares
    }

    private fun allIn(idx: Int, amount: Int): Int {
        val share = (amount / kLine.list[idx].o).toInt()
//        println("Buy $share share with ${kLine.list[idx].o} on ${secondToDate(kLine.list[idx].k)}")
        return share
    }

    private fun profitAndLossAsOf(idx: Int, totalAmount: Int, shares: Int): Int {
        return ((shares * kLine.list[idx].o - totalAmount) / totalAmount * 100).toInt()
    }

    fun run() {
        var idx = 0
        while (idx < kLine.list.size) {
            if (!KLineOpUtil(kLine).filterByDateRange(idx, fromDate, toDate)) {
                ++idx
                continue
            }

            if (!triggerCondition(idx)) {
                ++idx
                continue
            }

            val offset = 10
            for (i in 0..<offset) {
                val shares1 = rangeBuy(idx + i, 10, 1000)
                val pnl1 = profitAndLossAsOf(idx + i + 20, 10 * 1000, shares1)
                println("$stockName-$type: rangeBuy, purchased $shares1 shares, with profit and loss $pnl1% on ${secondToDate(kLine.list[idx + i + 20].k)}")

                val shares2 = allIn(idx + i, 10 * 1000)
                val pnl2 = profitAndLossAsOf(idx + i + 20, 10 * 1000, shares2)
                println("$stockName-$type: allIn, purchased $shares2 shares, with profit and loss $pnl2% on ${secondToDate(kLine.list[idx + i + 20].k)}")
            }
            idx += offset
        }
    }
}
