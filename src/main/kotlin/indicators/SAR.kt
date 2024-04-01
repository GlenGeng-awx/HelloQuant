package indicators

import KLine
import OpByIdx
import dateToIdx
import secondToDate
import kotlin.math.max
import kotlin.math.min

/**
 * SARn = SARn-1 + AFn * (EPn-1 - SARn-1)
 *
 * AF1 = 0.02
 *
 * if direction is down
 *   SAR0 = highest of 9 days before the start date
 *   AFn = min(0.2, AFn-1 + 0.02), if new low is made
 *   EPn-1 = min(low of n-1, EPn-2)
 *
 * if direction is up
 *   SAR0 = lowest of 9 days before the start date
 *   AFn = min(0.2, AFn-1 + 0.02), if new high is made
 *   EPn-1 = max(high of n-1, EPn-2)
 */

private val sarConfig = mapOf(
    Pair("tsla", "day") to Pair("2022-03-01", "up"),
    Pair("bili", "day") to Pair("2021-11-18", "down"),
)

fun calculateSAR(kLine: KLine, stockName: String, type: String) {
    var (startDate, direction) = sarConfig[stockName to type]!!
    var startIdx = dateToIdx(kLine, startDate)
    var prevStartIdx = 0

    // padding
    val sar = mutableListOf<Double>()
    for (i in 0..startIdx - 2) {
        sar.add(0.0)
    }

    while (true) {
        val prevLen = startIdx - prevStartIdx
        prevStartIdx = startIdx

        if (direction == "down") {
            startIdx = calculateDownSAR(kLine, startIdx, prevLen, sar)
            direction = "up"
        } else {
            startIdx = calculateUpSAR(kLine, startIdx, prevLen, sar)
            direction = "down"
        }

        if (startIdx >= kLine.list.size - 1) {
            break
        }
    }
}

fun calculateDownSAR(kLine: KLine, startIdx: Int, prevLen: Int, sar: MutableList<Double>): Int {
    assert(startIdx == sar.size)

    // SAR0
    val highestIdx = OpByIdx(kLine).highestIndexIn(startIdx - 9, 9)!!
    val sar0Hint = kLine.list[highestIdx].h

    var sar0 = 0.0
    for (idx in startIdx - 9..<startIdx) {
        if (OpByIdx(kLine).isLocalMax(idx)) {
            sar0 = max(sar0, kLine.list[idx].h)
        }
    }

    if (sar0 != sar0Hint) {
        println("---->[1] hack down sar0 at ${secondToDate(kLine.list[startIdx].k)} $sar0Hint -> $sar0 ")
    }

    if (prevLen == 1) {
        println("---->[0] hack down prevLen 1 at ${secondToDate(kLine.list[startIdx].k)} $sar0 -> ${kLine.list[startIdx - 1].h} ")
        sar0 = kLine.list[startIdx - 1].h
    }

    sar.add(sar0)

    // EP0
    val ep0 = kLine.list[startIdx].l

    // start from SAR1
    var af = 0.02
    var ep = ep0
    var currIdx = startIdx + 1

    while (true) {
        // SARn-1
        val prevSAR = sar.last()

        // AFn: not AF1 && new high is made
        if (currIdx != startIdx + 1 && kLine.list[currIdx - 1].l < ep) {
            af = min(0.2, af + 0.02)
        }

        // EPn-1
        ep = min(kLine.list[currIdx - 1].l, ep)

        // SARn = SARn-1 + AFn * (EPn-1 - SARn-1)
        var currSAR = prevSAR + af * (ep - prevSAR)

        // check if breaking down
        if (kLine.list[currIdx].h >= currSAR) {
            println("-->[2] breaking down at ${secondToDate(kLine.list[currIdx].k)} by high ${kLine.list[currIdx].h}, " +
                    "currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")
            return currIdx
        }

        // hack SARn
        val alterSAR = max(kLine.list[currIdx - 1].h, kLine.list[currIdx - 2].h)

        if (currSAR < alterSAR) {
            println("---->[3] down hack at ${secondToDate(kLine.list[currIdx].k)} $currSAR -> $alterSAR ")
            currSAR = alterSAR
        }

        sar.add(currSAR)
        println("down ${secondToDate(kLine.list[currIdx].k)}: currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")

        currIdx += 1

        if (currIdx == kLine.list.size) {
            return currIdx
        }
    }
}

fun calculateUpSAR(kLine: KLine, startIdx: Int, prevLen: Int, sar: MutableList<Double>): Int {
    assert(startIdx == sar.size)

    // SAR0
    val lowestIdx = OpByIdx(kLine).lowestIndexIn(startIdx - 9, 9)!!
    val sar0Hint = kLine.list[lowestIdx].l

    var sar0 = Double.MAX_VALUE
    for (idx in startIdx - 9..<startIdx) {
        if (OpByIdx(kLine).isLocalMin(idx)) {
            sar0 = min(sar0, kLine.list[idx].l)
        }
    }

    if (sar0 != sar0Hint) {
        println("---->[1] hack up sar0 at ${secondToDate(kLine.list[startIdx].k)} $sar0Hint -> $sar0 ")
    }

    if (prevLen == 1) {
        println("---->[0] hack up prevLen 1 at ${secondToDate(kLine.list[startIdx].k)} $sar0 -> ${kLine.list[startIdx - 1].l} ")
        sar0 = kLine.list[startIdx - 1].l
    }

    sar.add(sar0)

    // EP0
    val ep0 = kLine.list[startIdx].h

    // start from SAR1
    var af = 0.02
    var ep = ep0
    var currIdx = startIdx + 1

    while (true) {
        // SARn-1
        val prevSAR = sar.last()

        // AFn: not AF1 && new high is made
        if (currIdx != startIdx + 1 && kLine.list[currIdx - 1].h > ep) {
            af = min(0.2, af + 0.02)
        }

        // EPn-1
        ep = max(kLine.list[currIdx - 1].h, ep)

        // SARn = SARn-1 + AFn * (EPn-1 - SARn-1)
        var currSAR = prevSAR + af * (ep - prevSAR)

        // check if breaking up
        if (kLine.list[currIdx].l <= currSAR) {
            println("-->[2] breaking up at ${secondToDate(kLine.list[currIdx].k)} by low ${kLine.list[currIdx].l}, " +
                    "currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")
            return currIdx
        }

        // hack SARn
        val alterSAR = min(kLine.list[currIdx - 1].l, kLine.list[currIdx - 2].l)

        if (currSAR > alterSAR) {
            println("---->[3] up hack at ${secondToDate(kLine.list[currIdx].k)} $currSAR -> $alterSAR ")
            currSAR = alterSAR
        }

        sar.add(currSAR)
        println("up ${secondToDate(kLine.list[currIdx].k)}: currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")

        currIdx += 1

        if (currIdx == kLine.list.size) {
            return currIdx
        }
    }
}