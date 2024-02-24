class KLineOpByRange(private val kLine: KLine) {

    // return the lowest idx in [idx, idx + len]
    fun lowestIn(idx: Int, range: Int): Int? {
        if (idx + range >= kLine.list.size) {
            return null
        }
        var minIdx = idx
        for (i in idx + 1..idx + range) {
            if (kLine.list[i].l < kLine.list[minIdx].l) {
                minIdx = i
            }
        }
        return minIdx
    }

    // return the highest idx in [idx, idx + len]
    fun highestIn(idx: Int, range: Int): Int? {
        if (idx + range >= kLine.list.size) {
            return null
        }
        var maxIdx = idx
        for (i in idx + 1..idx + range) {
            if (kLine.list[i].h > kLine.list[maxIdx].h) {
                maxIdx = i
            }
        }
        return maxIdx
    }
}