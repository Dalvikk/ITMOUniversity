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

private fun readLine(): String? = _reader.readLine()
private fun readLn() = _reader.readLine()!!
private fun readInt() = read().toInt()
private fun readDouble() = read().toDouble()
private fun readLong() = read().toLong()
private fun readStrings(n: Int) = List(n) { read() }
private fun readLines(n: Int) = List(n) { readLn() }
private fun readInts(n: Int) = List(n) { read().toInt() }
private fun readIntArray(n: Int) = IntArray(n) { read().toInt() }
private fun readDoubles(n: Int) = List(n) { read().toDouble() }
private fun readDoubleArray(n: Int) = DoubleArray(n) { read().toDouble() }
private fun readLongs(n: Int) = List(n) { read().toLong() }
private fun readLongArray(n: Int) = LongArray(n) { read().toLong() }

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

fun main() {
    val (n, m) = readInts(2)
    val parents = readIntArray(n - 1)
    val graph = List(n) { ArrayList<Int>() }
    for ((i, value) in parents.withIndex()) {
        graph[value].add(i + 1)
    }
    val (a1, a2, x, y, z) = readInts(5)
    val tree = Tree(n, graph)
    val a = createA(a1, a2, x, y, z, m, n)
    var ans = 0L
    var v = 0
    tree.initLCA()
    for (i in 0 until m) {
        val newV = tree.LCA((a[2 * i] + v) % n, a[2 * i + 1])
        v = newV
        ans += newV
    }
    println(ans)
}

fun createA(a1: Int, a2: Int, x: Int, y: Int, z: Int, m: Int, n: Int): IntArray {
    val ans = IntArray(2 * m)
    ans[0] = a1
    ans[1] = a2
    for (i in 2 until 2 * m) {
        val cur = x.toLong() * ans[i - 2] + y.toLong() * ans[i - 1] + z
        ans[i] = (cur % n).toInt()
    }
    return ans
}

class Tree(
    private val n: Int,
    private val graph: List<List<Int>>,
) {
    private val timeIn = IntArray(n)
    private val timeOut = IntArray(n)
    private var counter = 0

    private var sparseTable: SparseTable_M<Pair<Int, Int>>? = null
    private var eulerTour: List<Pair<Int, Int>>? = null

    fun initLCA() {
        val eulerTour_ = ArrayList<Pair<Int, Int>>()
        dfs(0, 0, eulerTour_)
        sparseTable = SparseTable_M(eulerTour_.toTypedArray()) { a, b -> if (a.first <= b.first) a else b }
        eulerTour = eulerTour_
    }

    private fun dfs(v: Int, depth: Int, list: MutableList<Pair<Int, Int>>) {
        timeIn[v] = counter++
        list.add(Pair(depth, v))
        for (u in graph[v]) {
            dfs(u, depth + 1, list)
            list.add(Pair(depth, v))
        }
        timeOut[v] = counter++
    }

    fun isAncestor(u: Int, v: Int) = timeIn[u] <= timeIn[v] && timeOut[u] >= timeOut[v]

    fun LCA(u: Int, v: Int): Int {
        if (isAncestor(u, v)) return u
        if (isAncestor(v, u)) return v
        val flag = timeIn[u] < timeIn[v]
        val a = if (flag) u else v
        val b = if (flag) v else u
        return sparseTable!!.get(timeOut[a], timeIn[b]).second
    }
}

class SparseTable_M<T>(
    private val array: Array<T>,
    private val f: (T, T) -> T,
) {
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
            val table = MutableList<MutableList<T>>(n) { ArrayList(log[n] + 1) }
            for ((index, list) in table.withIndex()) {
                list.add(array[index])
            }
            for (k in 1..log[n]) {
                for (index in 0 until n - (1 shl (k - 1))) {
                    val list = table[index]
                    list.add(
                        f(
                            list.last(),
                            table[index + (1 shl (k - 1))].last()
                        )
                    )
                }
            }
            return table
        }
    }
}
