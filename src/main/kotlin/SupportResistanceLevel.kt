data class SupportLevel(var items: MutableList<Item> = mutableListOf()) {
    fun getSupportLevel() = items.sumOf { it.l } / items.size

    fun add(item: Item): Boolean {
        if (items.isEmpty() || getSupportLevel() * 1.03 >= item.l) {
            items.add(item)
            return true
        }
        return false
    }

    override fun toString(): String {
        val items = items.sortedBy { it.k }.joinToString { (secondToDate(it.k) to it.l).toString() }
        return "SupportLevel(${getSupportLevel()}, items=$items)"
    }
}

data class ResistanceLevel(var items: MutableList<Item> = mutableListOf()) {
    fun getResistanceLevel() = items.sumOf { it.h } / items.size

    fun add(item: Item): Boolean {
        if (items.isEmpty() || getResistanceLevel() * 1.03 >= item.h) {
            items.add(item)
            return true
        }
        return false
    }

    override fun toString(): String {
        val items = items.sortedBy { it.k }.joinToString { (secondToDate(it.k) to it.h).toString() }
        return "ResistanceLevel(${getResistanceLevel()}, items=$items)"
    }
}