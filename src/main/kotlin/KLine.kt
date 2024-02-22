import kotlinx.serialization.Serializable

@Serializable
data class KLine(
    val list: List<Item>
)

@Serializable
data class Item(
    val k: Long,        // timestamp
    val o: Double,      // open
    val c: Double,      // close
    val h: Double,      // high
    val l: Double,      // low
    val v: Long,        // volume
    val t: Double,      // turnover
    val r: Double,      // turnover%
    val lc: Double,     // last close
    val cp: String      // chg
)
