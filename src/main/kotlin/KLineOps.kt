data class KLineOps(val kLine: KLine) {

    // idx is lowest in [idx - range1, idx + range2]
    fun lowestIn(idx: Int, range1: Int, range2: Int): Boolean {
        if (idx - range1 < 0 || idx + range2 >= kLine.list.size) {
            return false
        }

        return (idx - range1..idx + range2).all { kLine.list[it].l >= kLine.list[idx].l }
    }

    // idx is highest in [idx - range1, idx + range2]
    fun highestIn(idx: Int, range1: Int, range2: Int): Boolean {
        if (idx - range1 < 0 || idx + range2 >= kLine.list.size) {
            return false
        }

        return (idx - range1..idx + range2).all { kLine.list[it].h <= kLine.list[idx].h }
    }

    // low is fell to in [idx + 1, idx + range]
    fun fallTo(low: Double, idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).any { kLine.list[it].l <= low }
    }

    // high is rose to in [idx + 1, idx + range]
    fun riseTo(high: Double, idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).any { kLine.list[it].h >= high }
    }

    // idx is local min
    fun localMin(idx: Int): Boolean {
        if (idx == 0 || idx == kLine.list.size - 1) {
            return false
        }

        return kLine.list[idx].l < kLine.list[idx - 1].l && kLine.list[idx].l < kLine.list[idx + 1].l
    }

    // idx is local max
    fun localMax(idx: Int): Boolean {
        if (idx == 0 || idx == kLine.list.size - 1) {
            return false
        }

        return kLine.list[idx].h > kLine.list[idx - 1].h && kLine.list[idx].h > kLine.list[idx + 1].h
    }

    // Achieve a higher price every day for [idx + 1, idx + range]
    fun higherPriceEveryDay(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].h > kLine.list[it - 1].h }
    }

    // Achieve a lower price every day for [idx + 1, idx + range]
    fun lowerPriceEveryDay(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].l < kLine.list[it - 1].l }
    }

    // Achieve a higher volume every day for [idx + 1, idx + range]
    fun higherVolumeEveryDay(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].v > kLine.list[it - 1].v }
    }

    // Achieve a lower volume every day for [idx + 1, idx + range]
    fun lowerVolumeEveryDay(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].v < kLine.list[it - 1].v }
    }

    // from and to is in the format of "yyyy-MM-dd"
    fun filterByDateRange(idx: Int, from: String?, to: String?): Boolean {
        val fromSecond = from?.let { dateToSecond(it) } ?: 0
        val toSecond = to?.let { dateToSecond(it) } ?: Long.MAX_VALUE

        return kLine.list[idx].k in fromSecond..toSecond
    }
}