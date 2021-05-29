package dataStructures

class SparseTable<T>(private val array: Array<T>,
                                     private val f: (T, T) -> T) {
    private val log = calcLog(array.size)
    private val sparseTable = calcSparseTable(array, log, f)

    fun get(l: Int, r: Int): T {
        val log = log[r - l + 1]
        return f(sparseTable[l][log], sparseTable[r - (1 shl log) + 1][log])
    }

    companion object {
        private fun calcLog(n: Int): IntArray {
            val log = IntArray(n + 1)
            for (i in 2..n) {
                log[i] = log[i / 2] + 1
            }
            return log
        }

        private fun <T> calcSparseTable(array: Array<T>, log: IntArray, f: (T, T) -> T): List<List<T>> {
            val n = array.size
            val table = MutableList<MutableList<T>> (n) { ArrayList(log[n] + 1)}
            for ((index, list) in table.withIndex()) {
                list.add(array[index])
            }
            for (k in 1..log[n]) {
                for (index in 0 until n - (1 shl (k - 1))) {
                    val list = table[index]
                    list.add(f(list.last(),
                        table[index + (1 shl (k - 1))].last()))
                }
            }
            return table
        }
    }
}
