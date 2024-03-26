
fun secondToDate(second: Long): String {
    val date = java.util.Date(second * 1000)
    val format = java.text.SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}

fun dateToSecond(dateStr: String): Long {
    val format = java.text.SimpleDateFormat("yyyy-MM-dd")
    val date = format.parse(dateStr)
    return date.time / 1000
}

fun currentDate(): String {
    val date = java.util.Date()
    val format = java.text.SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}

// from and to is in the format of "yyyy-MM-dd"
fun filterByDateRange(kLine: KLine, idx: Int, from: String?, to: String?): Boolean {
    val fromSecond = from?.let { dateToSecond(it) } ?: 0
    val toSecond = to?.let { dateToSecond(it) } ?: Long.MAX_VALUE

    return kLine.list[idx].k in fromSecond..toSecond
}

// return idx in KLine where k equals or is the first one greater than the specified date.
fun dateToIdx(kLine: KLine, date: String): Int {
    val second = dateToSecond(date)
    return kLine.list.indexOfFirst { it.k >= second }
}