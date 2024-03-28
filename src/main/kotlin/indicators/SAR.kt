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
 *   SAR0 = highest of 10 days before the start date
 *   AFn = min(0.2, AFn-1 + 0.02), if new low is made
 *   EPn-1 = min(low of n-1, EPn-2)
 *
 * if direction is up
 *   SAR0 = lowest of 10 days before the start date
 *   AFn = min(0.2, AFn-1 + 0.02), if new high is made
 *   EPn-1 = max(high of n-1, EPn-2)
 */

private val sarConfig = mapOf(
//    Pair("tsla", "day") to Pair("2022-06-10", "down"),
//    Pair("tsla", "day") to Pair("2022-04-25", "down")
    Pair("tsla", "day") to Pair("2022-03-01", "up")
)

fun calculateSAR(kLine: KLine, stockName: String, type: String) {
    var (startDate, direction) = sarConfig[stockName to type]!!
    var startIdx = dateToIdx(kLine, startDate)

    // padding
    val sar = mutableListOf<Double>()
    for (i in 0..startIdx - 2) {
        sar.add(0.0)
    }

    while (true) {
        if (direction == "down") {
            startIdx = calculateDownSAR(kLine, startIdx, sar)
            direction = "up"
        } else {
            startIdx = calculateUpSAR(kLine, startIdx, sar)
            direction = "down"
        }

        if (startIdx >= kLine.list.size - 1) {
            break
        }
    }
}

fun calculateDownSAR(kLine: KLine, startIdx: Int, sar: MutableList<Double>): Int {
    assert(startIdx == sar.size)

    // SAR0
    val highestIdx = OpByIdx(kLine).highestIndexIn(startIdx - 10, 10)!!
    val sar0 = kLine.list[highestIdx].h
    sar.add(sar0)

    // EP0
    val ep0 = kLine.list[startIdx].l

    // start from SAR1
    var af = 0.02
    var ep = ep0
    var currIdx = startIdx + 1

    while (true) {
        val prevSAR = sar.last()

        // update AFn: not AF1 && new low is made
        if (currIdx != startIdx + 1 && kLine.list[currIdx - 1].l < ep) {
            af = min(0.2, af + 0.02)
        }

        // update EPn-1
        ep = min(kLine.list[currIdx - 1].l, ep)

        val currSAR = prevSAR + af * (ep - prevSAR)

        if (kLine.list[currIdx].h >= currSAR) {
            return currIdx
        }

        sar.add(currSAR)
        println("down ${secondToDate(kLine.list[currIdx].k)}: currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")

        currIdx += 1

        if (currIdx == kLine.list.size) {
            return currIdx
        }
    }
}

fun calculateUpSAR(kLine: KLine, startIdx: Int, sar: MutableList<Double>): Int {
    assert(startIdx == sar.size)

    // SAR0
    val lowestIdx = OpByIdx(kLine).lowestIndexIn(startIdx - 10, 10)!!
    val sar0 = kLine.list[lowestIdx].l
    sar.add(sar0)

    // EP0
    val ep0 = kLine.list[startIdx].h

    // start from SAR1
    var af = 0.02
    var ep = ep0
    var currIdx = startIdx + 1

    while (true) {
        val prevSAR = sar.last()

        // update AFn: not AF1 && new high is made
        if (currIdx != startIdx + 1 && kLine.list[currIdx - 1].h > ep) {
            af = min(0.2, af + 0.02)
        }

        // update EPn-1
        ep = max(kLine.list[currIdx - 1].h, ep)

        val currSAR = prevSAR + af * (ep - prevSAR)

        if (kLine.list[currIdx].l <= currSAR) {
            return currIdx
        }

        sar.add(currSAR)
        println("up ${secondToDate(kLine.list[currIdx].k)}: currSAR:$currSAR, ep:$ep, prevSAR:$prevSAR, af:$af")

        currIdx += 1

        if (currIdx == kLine.list.size) {
            return currIdx
        }
    }
}