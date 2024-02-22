
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