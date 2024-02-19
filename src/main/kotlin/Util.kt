import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun callUrlWithHeaders(url: String, headers: Map<String, String> = emptyMap()): String {
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