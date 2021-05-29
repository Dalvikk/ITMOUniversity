import java.util.*
import kotlin.math.min

private val INPUT = System.`in`
private val _reader = INPUT.bufferedReader()
private var _tokenizer: StringTokenizer = StringTokenizer("")

private fun read(): String {
    while (_tokenizer.hasMoreTokens().not()) {
        _tokenizer = StringTokenizer(_reader.readLine() ?: return "", " ")
    }
    return _tokenizer.nextToken()
}

private fun readInts(n: Int) = List(n) { read().toInt() }

class SparseTable(private val array: IntArray, private val f: (Int, Int) -> Int) {
    private val log = calcLog(array.size)
    private val sparseTable = calcSparseTable(array, log, f)

    fun get(l: Int, r: Int): Int {
        val log = log[r - l + 1]
        return f(sparseTable[l - 1][log], sparseTable[r - (1 shl log)][log])
    }

    companion object {
        private fun calcLog(n: Int): IntArray {
            val log = IntArray(n + 1)
            for (i in 2..n) {
                log[i] = log[i / 2] + 1
            }
            return log
        }

        private fun calcSparseTable(array: IntArray, log: IntArray, f: (Int, Int) -> Int): Array<IntArray> {
            val n = array.size // (1 shl k) <= n - it
            val table = Array(n) { IntArray(log[n - it] + 1) { 0 } }
            for ((index, cur) in table.withIndex()) {
                cur[0] = array[index]
            }
            for (k in 1..log[n]) {
                for (index in 0..n - (1 shl k)) {
                    val cur = table[index]
                    cur[k] = f(
                            cur[k - 1],
                            table[index + (1 shl (k - 1))][k - 1]
                        )
                }
            }
            return table
        }
    }
}

fun main() {

    val (n, m, a1, u1, v1) = readInts(5)
    val array = IntArray(n)
    array[0] = a1
    for (i in 1 until n) {
        array[i] = (23 * array[i - 1] + 21563) % 16714589
    }
    val table = SparseTable(array, ::min)
    var u = u1
    var v = v1
    var ans = table.get(u, v)
    for (i in 2..m) {
        u = ((17 * u + 751 + ans + 2 * i - 2) % n) + 1
        v = ((13 * v + 593 + ans + 5 * i - 5) % n) + 1
        ans = if (u <= v) table.get(u, v) else table.get(v, u)
    }
    println("$u $v $ans")
}
