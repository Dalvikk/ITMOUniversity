import java.io.PrintWriter
import java.util.*

private val INPUT = System.`in`
private val OUTPUT = System.out
private val _reader = INPUT.bufferedReader()
private val _writer = PrintWriter(OUTPUT, false)
private var _tokenizer: StringTokenizer = StringTokenizer("")

private fun read(): String {
    while (_tokenizer.hasMoreTokens().not()) {
        _tokenizer = StringTokenizer(_reader.readLine() ?: return "", " ")
    }
    return _tokenizer.nextToken()
}

private fun readLn() = _reader.readLine()!!
private fun readInt() = read().toInt()
private fun readInts(n: Int) = List(n) { read().toInt() }

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

fun main() {
    val n = 500_000
    val graph = Array(n) { ArrayList<Int>() }
    val queries = ArrayList<Pair<Int, Int>>()
    val k = readInt()
    for (i in 1..k) {
        val cmd = read()
        val (a, b) = readInts(2)
        if (cmd == "ADD") {
            graph[a - 1].add(b - 1)
        } else {
            queries.add(Pair(a - 1, b - 1))
        }
    }
    val tree = TreeK(n, graph)
    tree.initLCA()
    output {
        for ((u, v) in queries) {
            println(tree.LCA(u, v) + 1)
        }
    }
}

class TreeK(
    private val n: Int,
    private val graph: Array<ArrayList<Int>>,
) {
    private val timeIn = IntArray(n)
    private val timeOut = IntArray(n)
    private val depth = IntArray(n)
    private var counter = 0

    private var sparseTable: SparseTableInt? = null
    private var eulerTour: IntArray? = null

    fun initLCA() {
        val eulerTour_ = IntArray(2 * n - 1)
        dfs(0, 0, eulerTour_, 0)
        sparseTable = SparseTableInt(eulerTour_) { a, b -> if (depth[a] <= depth[b]) a else b }
        eulerTour = eulerTour_
    }

    private fun dfs(v: Int, curDepth: Int, list: IntArray, idx: Int): Int {
        depth[v] = curDepth
        timeIn[v] = counter++
        list[idx] = v
        var nextIdx = idx + 1
        for (u in graph[v]) {
            val newIdx = dfs(u, curDepth + 1, list, nextIdx)
            list[newIdx] = v
            nextIdx = newIdx + 1
        }
        timeOut[v] = counter++
        return nextIdx
    }

    fun isAncestor(u: Int, v: Int) = timeIn[u] <= timeIn[v] && timeOut[u] >= timeOut[v]

    fun LCA(u: Int, v: Int): Int {
        if (isAncestor(u, v)) return u
        if (isAncestor(v, u)) return v
        val flag = timeIn[u] < timeIn[v]
        val a = if (flag) u else v
        val b = if (flag) v else u
        return sparseTable!!.get(timeOut[a], timeIn[b])
    }


    class SparseTableInt(
        private val array: IntArray,
        private val f: (Int, Int) -> Int,
    ) {
        private val log = calcLog(array.size)
        private val sparseTable = calcSparseTable(array, log, f)

        fun get(l: Int, r: Int): Int {
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

            private fun calcSparseTable(array: IntArray, log: IntArray, f: (Int, Int) -> Int): Array<IntArray> {
                val n = array.size
                val table = Array(n) { IntArray(log[n] + 1) }
                for ((index, list) in table.withIndex()) {
                    list[0] = array[index]
                }
                for (k in 1..log[n]) {
                    for (index in 0 until n - (1 shl (k - 1))) {
                        table[index][k] = f(
                            table[index][k - 1],
                            table[index + (1 shl (k - 1))][k - 1]
                        )
                    }
                }
                return table
            }
        }
    }
}
