
fun getTypeId(type: String): String {
    return when (type.lowercase()) {
        "day" -> "2"
        "week" -> "3"
        "month" -> "4"
        else -> TODO()
    }
}

fun getStockId(stockName: String): String {
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
        else -> TODO()
    }
}

fun getQuoteToken(stockName: String, type: String): String {
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

        else -> TODO()
    }
}

fun getFutuToken() = "7xZ4UtJ6yXdz3p4W4cAg-mai"

fun buildUrl(stockName: String, type: String): String {
    return """
        https://www.futunn.com/quote-api/quote-v2/get-kline?stockId=${getStockId(stockName)}
        &marketType=2
        &type=${getTypeId(type)}
        &marketCode=11
        &instrumentType=3
    """.trimIndent()
}