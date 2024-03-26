package strategy

import KLine

data class OpByIdx(val kLine: KLine) {

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

    // idx is the highest price in [idx - 30, idx + 10]
    fun resistanceLevel(idx: Int): Boolean {
        return OpByIdx(kLine).highestIn(idx, 30, 10)
    }

    // idx is the lowest price in [idx - 30, idx + 10]
    fun supportLevel(idx: Int): Boolean {
        return OpByIdx(kLine).lowestIn(idx, 30, 10)
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