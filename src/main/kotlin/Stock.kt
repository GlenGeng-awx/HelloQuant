import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

data class Stock(val stockName: String, val type: String) {
    private var kline: KLine? = null

    private var localMin: MutableList<Item>? = null
    private var localMax: MutableList<Item>? = null

    private var supportLevels: MutableList<SupportLevel>? = null
    private var resistanceLevels: MutableList<ResistanceLevel>? = null

    private fun loadKline() {
        val url = buildUrl(stockName, type)
        val headers = mapOf(
            "Futu-X-Csrf-Token" to getFutuToken(),
            "Quote-Token" to getQuoteToken(stockName, type)
        )

        val response = runBlocking { callUrlWithHeaders(url, headers) }
        kline = Json.decodeFromString<Response>(response).data
    }

    private fun calculateLocalMin(fromDate: String? = null, toDate: String? = null) {
        val fromSecond = fromDate?.let { dateToSecond(it) } ?: 0
        val toSecond = toDate?.let { dateToSecond(it) } ?: Long.MAX_VALUE

        val list = kline!!.list
        localMin = mutableListOf()

        for (i in 1..<list.size - 1) {
            if (list[i].k < fromSecond || list[i].k > toSecond) {
                continue
            }
            if (list[i].l < list[i - 1].l && list[i].l < list[i + 1].l) {
//                println("calculateLocalMin hit ${secondToDate(list[i].k)}")
                localMin!!.add(list[i])
            }
        }
    }

    private fun calculateLocalMax(fromDate: String? = null, toDate: String? = null) {
        val fromSecond = fromDate?.let { dateToSecond(it) } ?: 0
        val toSecond = toDate?.let { dateToSecond(it) } ?: Long.MAX_VALUE

        val list = kline!!.list
        localMax = mutableListOf()

        for (i in 1..<list.size - 1) {
            if (list[i].k < fromSecond || list[i].k > toSecond) {
                continue
            }
            if (list[i].h > list[i - 1].h && list[i].h > list[i + 1].h) {
//                println("calculateLocalMax hit ${secondToDate(list[i].k)}")
                localMax!!.add(list[i])
            }
        }
    }

    private fun calculateSupportLevel() {
        val sortedLocalMin = localMin!!.sortedBy { it.l }
        supportLevels = mutableListOf()

        var supportLevel = SupportLevel()
        for (item in sortedLocalMin) {
            if (!supportLevel.add(item)) {
                supportLevels!!.add(supportLevel)
                supportLevel = SupportLevel(mutableListOf(item))
            }
        }
        supportLevels!!.add(supportLevel)

        println("Support levels:${supportLevels!!.joinToString("\n")}")
    }

    private fun calculateResistanceLevel() {
        val sortedLocalMax = localMax!!.sortedBy { it.h }
        resistanceLevels = mutableListOf()

        var resistanceLevel = ResistanceLevel()
        for (item in sortedLocalMax) {
            if (!resistanceLevel.add(item)) {
                resistanceLevels!!.add(resistanceLevel)
                resistanceLevel = ResistanceLevel(mutableListOf(item))
            }
        }
        resistanceLevels!!.add(resistanceLevel)

        println("Resistance levels:${resistanceLevels!!.joinToString("\n")}")
    }

    fun test() {
        loadKline()

        calculateLocalMin("2023-05-01", "2024-02-01")
        calculateSupportLevel()

        calculateLocalMax("2023-05-01", "2024-02-01")
        calculateResistanceLevel()
    }
}