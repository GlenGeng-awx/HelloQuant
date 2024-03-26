package klinestore

import KLine
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FutuStore {
    private fun getTypeId(type: String): String {
        return when (type.lowercase()) {
            "day" -> "2"
            "week" -> "3"
            "month" -> "4"
            else -> TODO()
        }
    }

    private fun getStockId(stockName: String): String {
        return when (stockName.lowercase()) {
            "bili" -> "75630079330475"
            "tsla" -> "201335"
            "coinbase" -> "80328773540970"
            "baba" -> "210182"
            "bidu" -> "205354"
            "mrna" -> "76721001012871"
            "edu" -> "201034"
            "meta" -> "82252918909550"
            "pdd" -> "76141180439059"
            "rivn" -> "81179177086489"
            "zoom" -> "77283641740655"
            "snap" -> "73929272278628"
            "snow" -> "79512729758316"
            else -> TODO()
        }
    }

    private fun getMarketCode(stockName: String): String {
        return when (stockName.lowercase()) {
            "bili", "tsla", "coinbase", "bidu", "mrna", "meta", "pdd", "rivn", "zoom" -> "11" // nasdaq
            "baba", "edu", "snap", "snow" -> "10" // nyse
            else -> TODO()
        }
    }

    private fun buildUrl(stockName: String, type: String): String {
        return """
            https://www.futunn.com/quote-api/quote-v2/get-kline?stockId=${getStockId(stockName)}
            &marketType=2
            &type=${getTypeId(type)}
            &marketCode=${getMarketCode(stockName)}
            &instrumentType=3
            """.trimIndent()
    }

    private fun getQuoteToken(stockName: String, type: String): String {
        return when (stockName.lowercase() to type) {
            "bili" to "day" -> "34205e3b8f"
            "bili" to "week" -> "5f99c78bf1"
            "bili" to "month" -> "cd66e1ec0e"

            "tsla" to "day" -> "5e2a894394"
            "tsla" to "week" -> "324d91015c"
            "tsla" to "month" -> "72f7c91687"

            "coinbase" to "day" -> "18b77e6eb6"
            "coinbase" to "week" -> "9d423c2925"
            "coinbase" to "month" -> "3c65813f89"

            "baba" to "day" -> "c0329b954c"
            "baba" to "week" -> "93acf52bbf"
            "baba" to "month" -> "957d45ddae"

            "bidu" to "day" -> "7a02640ce5"
            "bidu" to "week" -> "f4edea1d8a"
            "bidu" to "month" -> "805c4d2a0a"

            "mrna" to "day" -> "d4686d3a30"
            "mrna" to "week" -> "d48d6a8ebf"
            "mrna" to "month" -> "9fafac17da"

            "edu" to "day" -> "1543fa5626"
            "edu" to "week" -> "72f1659615"
            "edu" to "month" -> "b362ff6fbf"

            "meta" to "day" -> "144d2d1f70"
            "meta" to "week" -> "fcbc4d6497"
            "meta" to "month" -> "afa338c0cb"

            "pdd" to "day" -> "39ebf8c60f"
            "pdd" to "week" -> "d4b0b97e92"
            "pdd" to "month" -> "98fa60d856"

            "rivn" to "day" -> "ebf05ad4df"
            "rivn" to "week" -> "1dece6f245"
            "rivn" to "month" -> "6e71e571ac"

            "zoom" to "day" -> "2738fd1b9f"
            "zoom" to "week" -> "006a97739c"
            "zoom" to "month" -> "93b07ac80f"

            "snap" to "day" -> "96db030807"
            "snap" to "week" -> "3f223e2716"
            "snap" to "month" -> "f9b8223eff"

            "snow" to "day" -> "d7dddcd2ea"
            "snow" to "week" -> "0e72a4facc"
            "snow" to "month" -> "cc1f882335"

            else -> TODO()
        }
    }

    private fun getFutuToken() = "7xZ4UtJ6yXdz3p4W4cAg-mai"

    private suspend fun callUrlWithHeaders(url: String, headers: Map<String, String> = emptyMap()): String {
        println("Calling url: $url with headers: $headers")

        val client = HttpClient(CIO)
        return try {
            val response: HttpResponse = client.get(url) {
                headers {
                    headers.forEach { (key, value) -> append(key, value) }
                }
            }
            if (response.status.value == 200) {
                response.readText()
            } else {
                "Error: ${response.status.value}"
            }
        } catch (e: Exception) {
            "Exception occurred: ${e.message}"
        } finally {
            client.close()
        }
    }

    /**
     * {
     *     "code": 0,
     *     "message": "成功",
     *     "data": {
     *         "list": [
     *             {
     *                 "k": 1522209600,
     *                 "o": "9.8",
     *                 "c": "11.24",
     *                 "h": "11.26",
     *                 "l": "9.62",
     *                 "v": 23929559,
     *                 "t": 251469858.431,
     *                 "r": 0.07678,
     *                 "lc": 11.5,
     *                 "cp": "-0.26"
     *             },
     *             {
     *                 "k": 1522296000,
     *                 "o": "11.5",
     *                 "c": "11",
     *                 "h": "11.8",
     *                 "l": "10.65",
     *                 "v": 5863898,
     *                 "t": 65582116.699,
     *                 "r": 0.01882,
     *                 "lc": 11.24,
     *                 "cp": "-0.24"
     *             }
     *         ]
     *      }
     * }
     */
    @Serializable
    data class Response(
        val code: Int,
        val message: String,
        val data: KLine
    )

    fun load(stockName: String, type: String): KLine {
        val url = buildUrl(stockName, type)
        val headers = mapOf(
            "Futu-X-Csrf-Token" to getFutuToken(),
            "Quote-Token" to getQuoteToken(stockName, type)
        )

        val ts1 = System.currentTimeMillis()
        val response = runBlocking { callUrlWithHeaders(url, headers) }
        val ts2 = System.currentTimeMillis()
        println("Finish loading KLine: $stockName.$type with time cost ${(ts2 - ts1) / 1000}s")

        return Json.decodeFromString<Response>(response).data
    }
}