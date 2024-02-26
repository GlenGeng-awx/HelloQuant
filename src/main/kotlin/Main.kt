
fun main() {
    for (stockName in stockNames) {
        SandBox(stockName, "day", "2023-12-01", "2024-12-01").run()
    }
}
