data class KLinePattern(val kLine: KLine) {
    fun doubleBottom(idx: Int, range1: Int, range2: Int): Boolean {
        if (!KLineOps(kLine).lowestIn(idx, range1, range2)) {
            return false
        }

        val low = kLine.list[idx].l
        val lowPlus3Percent = low * 1.03

        return KLineOps(kLine).fallTo(lowPlus3Percent, idx, range2)
    }

    fun doubleTop(idx: Int, range1: Int, range2: Int): Boolean {
        if (!KLineOps(kLine).highestIn(idx, range1, range2)) {
            return false
        }

        val high = kLine.list[idx].h
        val highMinus3Percent = high * 0.97

        return KLineOps(kLine).riseTo(highMinus3Percent, idx, range2)
    }

    // idx is local min,
    // achieve a higher price every day for 3 consecutive days starting from idx + 1
    fun reversalUpwards(idx: Int): Boolean {
        if (!KLineOps(kLine).localMin(idx)) {
            return false
        }

        return KLineOps(kLine).higherPriceEveryDay(idx, 3)
    }

    // idx is the highest price in [idx - 30, idx + 10]
    fun resistanceLevel(idx: Int): Boolean {
        return KLineOps(kLine).highestIn(idx, 30, 10)
    }

    // idx is the lowest price in [idx - 30, idx + 10]
    fun supportLevel(idx: Int): Boolean {
        return KLineOps(kLine).lowestIn(idx, 30, 10)
    }
}