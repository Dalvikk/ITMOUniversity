import java.io.PrintWriter
import java.lang.Integer.min
import java.util.*
import kotlin.math.log2

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
    val n = readInt()
    val graph = List(n + 1) { ArrayList<Pair<Int, Int>>() }
    for (i in 2..n) {
        val (x, y) = readInts(2)
        graph[x].add(Pair(i, y))
    }
    val tree = TreeO(n, graph)
    System.gc()
    tree.initFun()
    val m = readInt()
    output {
        for (i in 1..m) {
            val (x, y) = readInts(2)
            println(tree.minInPath(x, y))
        }
    }
}


class TreeO(
    private val n: Int,
    private val graph: List<List<Pair<Int, Int>>>,
) {
    private val timeIn = IntArray(n + 1)
    private val timeOut = IntArray(n + 1)
    private var counter = 0
    private val lg = log2(n.toDouble()).toInt()
    private val parent = Array(n + 1) { IntArray(lg + 1) }
    private val weight = Array(n + 1) { IntArray(lg + 1) }
    private val INF = 1_000_000

    fun initFun() {
        parent[1][0] = 1
        weight[1][0] = INF
        dfs(1)
    }

    private fun dfs(v: Int) {
        timeIn[v] = counter++
        for (i in 1..lg) {
            val p = parent[v][i - 1]
            parent[v][i] = parent[p][i - 1]
            weight[v][i] = min(weight[v][i - 1], weight[p][i - 1])
        }
        for ((u, w) in graph[v]) {
            parent[u][0] = v
            weight[u][0] = w
            dfs(u)
        }
        timeOut[v] = counter++
    }

    fun isAncestor(u: Int, v: Int) = timeIn[u] <= timeIn[v] && timeOut[u] >= timeOut[v]

    fun minInPath(u: Int, v: Int): Int {
        require(u != v)
        var ans = INF
        var a = u
        var b = v
        for (i in lg downTo 0) {
            val p = parent[a][i]
            val w = weight[a][i]
            if (!isAncestor(p, b)) {
                a = p
                ans = min(ans, w)
            }
        }
        for (i in lg downTo 0) {
            val p = parent[b][i]
            val w = weight[b][i]
            if (!isAncestor(p, a)) {
                b = p
                ans = min(ans, w)
            }
        }
        return when {
            isAncestor(a, b) -> {
                min(ans, weight[b][0])
            }
            isAncestor(b, a) -> {
                min(ans, weight[a][0])
            }
            else -> {
                min(ans, min(weight[a][0], weight[b][0]))
            }
        }
    }
}
