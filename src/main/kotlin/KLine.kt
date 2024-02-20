import kotlinx.serialization.Serializable

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
