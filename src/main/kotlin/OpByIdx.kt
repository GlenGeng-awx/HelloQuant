data class OpByIdx(val kLine: KLine) {
    // idx is local max
    private fun isLocalMax(idx: Int): Boolean {
        if (idx == 0 || idx == kLine.list.size - 1) {
            return false
        }

        return kLine.list[idx].h > kLine.list[idx - 1].h && kLine.list[idx].h > kLine.list[idx + 1].h
    }

    // idx is local min
    private fun isLocalMin(idx: Int): Boolean {
        if (idx == 0 || idx == kLine.list.size - 1) {
            return false
        }

        return kLine.list[idx].l < kLine.list[idx - 1].l && kLine.list[idx].l < kLine.list[idx + 1].l
    }

    // idx is lowest in [idx - range1, idx + range2]
    private fun isLowestIn(idx: Int, range1: Int, range2: Int): Boolean {
        if (idx - range1 < 0 || idx + range2 >= kLine.list.size) {
            return false
        }

        return (idx - range1..idx + range2).all { kLine.list[it].l >= kLine.list[idx].l }
    }

    // idx is highest in [idx - range1, idx + range2]
    private fun isHighestIn(idx: Int, range1: Int, range2: Int): Boolean {
        if (idx - range1 < 0 || idx + range2 >= kLine.list.size) {
            return false
        }

        return (idx - range1..idx + range2).all { kLine.list[it].h <= kLine.list[idx].h }
    }

    // idx is the highest price in [idx - x, idx + y], where x + y = 30
    fun resistanceLevel(idx: Int): Boolean {
        if (!isLocalMax(idx)) {
            return false
        }

        for (x in 0..30) {
            if (isHighestIn(idx, x, 30 - x)) {
                return true
            }
        }

        return false
    }

    // idx is the lowest price in [idx - x, idx + y], where x + y = 30
    fun supportLevel(idx: Int): Boolean {
        if (!isLocalMin(idx)) {
            return false
        }

        for (x in 0..30) {
            if (isLowestIn(idx, x, 30 - x)) {
                return true
            }
        }

        return false
    }

    // return the lowest idx in [idx, idx + len]
    fun lowestIndexIn(idx: Int, range: Int): Int? {
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
    fun highestIndexIn(idx: Int, range: Int): Int? {
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

    // Achieve a higher price every day for [idx + 1, idx + range]
    fun consecutiveHigherPrice(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].h > kLine.list[it - 1].h }
    }

    // Achieve a lower price every day for [idx + 1, idx + range]
    fun consecutiveLowerPrice(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].l < kLine.list[it - 1].l }
    }

    // Achieve a higher volume every day for [idx + 1, idx + range]
    fun consecutiveHigherVolume(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].v > kLine.list[it - 1].v }
    }

    // Achieve a lower volume every day for [idx + 1, idx + range]
    fun consecutiveLowerVolume(idx: Int, range: Int): Boolean {
        if (idx + range >= kLine.list.size) {
            return false
        }

        return (idx + 1..idx + range).all { kLine.list[it].v < kLine.list[it - 1].v }
    }
}