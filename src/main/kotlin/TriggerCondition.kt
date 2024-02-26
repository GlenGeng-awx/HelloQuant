class TriggerCondition(
    private val stockName: String,
    private val type: String,
    private val kLine: KLine
) {
    private fun getLastSupportLevel(idx: Int): Int? {
        for (i in idx - 1 downTo 0) {
            if (KLineOpByIdx(kLine).supportLevel(i)) {
                return i
            }
        }
        return null
    }

    private fun getLastResistanceLevel(idx: Int): Int? {
        for (i in idx - 1 downTo 0) {
            if (KLineOpByIdx(kLine).resistanceLevel(i)) {
                return i
            }
        }
        return null
    }

    /**
     * Downward breakthrough of the previous support level, reaching 20% decline.
     */
    fun triggerBottomFishing(idx: Int): Int? {
        val supportIdx = getLastSupportLevel(idx) ?: return null

        if (kLine.list[idx].l * 1.2 > kLine.list[supportIdx].l) {
            return null
        }

//        println("\n" + "$stockName-$type: " +
//                "${secondToDate(kLine.list[idx].k)} with price ${kLine.list[idx].l} broke the support level " +
//                "${secondToDate(kLine.list[supportIdx].k)} with price ${kLine.list[supportIdx].l}")
        return supportIdx
    }

    /**
     * Breakthrough of the previous resistance level, reaching 5% increase.
     */
    fun triggerUpTrend(idx: Int): Int? {
        val resistanceIdx = getLastResistanceLevel(idx) ?: return null

        if (kLine.list[idx].h < kLine.list[resistanceIdx].h * 1.05) {
            return null
        }

//        println("\n" + "$stockName-$type: " +
//                "${secondToDate(kLine.list[idx].k)} with price ${kLine.list[idx].h} broke the resistance level " +
//                "${secondToDate(kLine.list[resistanceIdx].k)} with price ${kLine.list[resistanceIdx].h}")
        return resistanceIdx
    }
}