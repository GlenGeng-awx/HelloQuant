data class KLineUtil(val kLine: KLine) {

    // idx is lowest in [idx - range1, idx + range2]
    fun lowestIn(idx: Int, range1: Int, range2: Int): Boolean {
        val beg = maxOf(0, idx - range1)
        val end = minOf(kLine.list.size - 1, idx + range2)

        return (beg..end).all { kLine.list[it].l >= kLine.list[idx].l }
    }

    // idx is highest in [idx - range1, idx + range2]
    fun highestIn(idx: Int, range1: Int, range2: Int): Boolean {
        val beg = maxOf(0, idx - range1)
        val end = minOf(kLine.list.size - 1, idx + range2)

        return (beg..end).all { kLine.list[it].h <= kLine.list[idx].h }
    }

    // low is fell to in [idx + 1, idx + range]
    fun fallTo(low: Double, idx: Int, range: Int): Boolean {
        val beg = idx + 1
        val end = minOf(kLine.list.size - 1, idx + range)

        return (beg..end).any { kLine.list[it].l <= low }
    }

    // high is rose to in [idx + 1, idx + range]
    fun riseTo(high: Double, idx: Int, range: Int): Boolean {
        val beg = idx + 1
        val end = minOf(kLine.list.size - 1, idx + range)

        return (beg..end).any { kLine.list[it].h >= high }
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
}