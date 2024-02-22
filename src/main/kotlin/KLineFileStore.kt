import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class KLineFileStore {
    fun load(stockName: String, type: String): KLine? {
        val filePath = "./data/$stockName.$type.${currentDate()}"
        val file = File(filePath)

        if (!file.exists()) {
            return null
        }

        println("Load KLine from file: $filePath")
        val readJsonString = file.readText()
        return Json.decodeFromString<KLine>(readJsonString)
    }

    fun store(stockName: String, type: String, kLine: KLine) {
        val filePath = "./data/$stockName.$type.${currentDate()}"
        val file = File(filePath)

        if (file.exists()) {
            return
        }

        println("Store KLine to file: $filePath")
        val writeJsonString = Json.encodeToString(kLine)
        file.writeText(writeJsonString)
    }
}
