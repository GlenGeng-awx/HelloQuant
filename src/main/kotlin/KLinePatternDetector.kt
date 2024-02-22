data class KLinePatternDetector(val kLine: KLine) {
    fun doubleBottom(idx: Int, range1: Int, range2: Int): Boolean {
        if (!KLineUtil(kLine).lowestIn(idx, range1, range2)) {
            return false
        }

        val low = kLine.list[idx].l
        val lowPlus3Percent = low * 1.03

        return KLineUtil(kLine).fallTo(lowPlus3Percent, idx, range2)
    }

    fun doubleTop(idx: Int, range1: Int, range2: Int): Boolean {
        if (!KLineUtil(kLine).highestIn(idx, range1, range2)) {
            return false
        }

        val high = kLine.list[idx].h
        val highMinus3Percent = high * 0.97

        return KLineUtil(kLine).riseTo(highMinus3Percent, idx, range2)
    }
}