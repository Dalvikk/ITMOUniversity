import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("isomorphism.in")
private val OUTPUT = FileOutputStream("isomorphism.out")
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

class DFA_F(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Pair<Int, Char>>>
) {
    private val mod = 1_000_000_007

    fun checkIsomorphism(other: DFA_F): Boolean {
        if (n != other.n) return false
        val assoc = IntArray(n + 1)
        val used = BooleanArray(n + 1)
        val ts = BooleanArray(n + 1)
        val ts2 = BooleanArray(n + 1)
        terminalStates.forEach { ts[it] = true }
        other.terminalStates.forEach { ts2[it] = true }
        return checkIsoDfs(other, 1, 1, assoc, used, ts, ts2)
    }

    private fun checkIsoDfs(
        other: DFA_F,
        v: Int,
        u: Int,
        assoc: IntArray,
        used: BooleanArray,
        ts: BooleanArray,
        ts2: BooleanArray
    ): Boolean {
        assoc[v] = u
        used[v] = true
        val edges = graph[v].sortedBy { it.second }
        val edges2 = other.graph[u].sortedBy { it.second }
        if (edges.size != edges2.size || ts[v] != ts2[u]) return false
        for (i in edges.indices) {
            val (to, ch) = edges[i]
            val (to2, ch2) = edges2[i]
            if (ch != ch2) return false
            if (used[to] && assoc[to] != to2) {
                return false
            } else if (!used[to]) {
                if (!checkIsoDfs(other, to, to2, assoc, used, ts, ts2)) return false
            }
        }
        return true
    }
}

fun readDFA(): DFA_F {
    val (n, m, k) = readInts(3)
    val terminalStates = readIntArray(k)
    val graph = (0..n).map { ArrayList<Pair<Int, Char>>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(Pair(b, c[0]))
    }
    return DFA_F(n, terminalStates, graph)
}

fun main() {
    val DFA = readDFA()
    val DFA2 = readDFA()
    output {
        println(if (DFA.checkIsomorphism(DFA2)) "YES" else "NO")
    }
}