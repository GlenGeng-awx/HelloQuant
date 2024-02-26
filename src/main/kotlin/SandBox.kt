/**
 * BottomFishingStrategy
 *
 * Trigger condition: Downward breakthrough of the previous support level, reaching 20% decline.
 * Purchase strategy: Purchase continuously for 10 consecutive days, with 1000 USD each day,
 *                    and hold for 20 days.
 */
class SandBox(
    private val stockName: String,
    private val type: String,
    private val fromDate: String,
    private val toDate: String,
) {
    private val kLine: KLine = KLineStore().load(stockName, type)
    private val triggerCondition: TriggerCondition = TriggerCondition(stockName, type, kLine)
    private val buyStrategy: BuyStrategy = BuyStrategy(stockName, type, kLine)
    private val amountPerDay: Int = 10_000

    private fun triggeredIndices(): MutableList<Int> {
        val indices = mutableListOf<Int>()
        for (idx in 0..<kLine.list.size) {
            if (!KLineOpUtil(kLine).filterByDateRange(idx, fromDate, toDate)) {
                continue
            }

            if (triggerCondition.triggerUpTrend(idx)) {
                indices.add(idx)
            }
        }
        return indices
    }

    private fun varifyAt(idx: Int) {
        val shares1 = buyStrategy.consecutiveBuy(idx, 10, amountPerDay)
        val pnl1 = buyStrategy.profitAndLossAsOf(idx + 30, 10 * amountPerDay, shares1)
        println("$stockName-$type: consecutiveBuy " +
                "purchased $shares1 shares, with $pnl1% on ${secondToDate(kLine.list[idx + 30].k)}")

        val shares2 = buyStrategy.buy(idx, 10 * amountPerDay)
        val pnl2 = buyStrategy.profitAndLossAsOf(idx + 30, 10 * amountPerDay, shares2)
        println("$stockName-$type: allIn " +
                "purchased $shares2 shares, with $pnl2% on ${secondToDate(kLine.list[idx + 30].k)}")
    }

    fun run() {
        val indices = triggeredIndices()
        for (idx in indices) {
            varifyAt(idx)
        }
    }
}
