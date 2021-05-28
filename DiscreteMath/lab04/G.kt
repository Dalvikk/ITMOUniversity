import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("equivalence.in")
private val OUTPUT = FileOutputStream("equivalence.out")
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

class DFA_G(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Pair<Int, Char>>>,
) {
    fun checkEquivalence(other: DFA_G): Boolean {
        val used = Array(n + 1) { BooleanArray(other.n + 1) }
        val ts = BooleanArray(n + 1)
        val ts2 = BooleanArray(other.n + 1)
        terminalStates.forEach { ts[it] = true }
        other.terminalStates.forEach { ts2[it] = true }
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(Pair(1, 1))
        // 0 is demon vertex
        while (q.isNotEmpty()) {
            val (v, u) = q.removeFirst()
            used[v][u] = true
            if (ts[v] != ts2[u]) return false
            val edges = graph[v].associate { Pair(it.second, it.first) }
            val edges2 = other.graph[u].associate { Pair(it.second, it.first) }
            for (i in 'a'..'z') {
                val to = edges.getOrDefault(i, 0)
                val to2 = edges2.getOrDefault(i, 0)
                if (!used[to][to2]) q.add(Pair(to, to2))
            }
        }
        return true
    }
}

fun readDFAG(): DFA_G {
    val (n, m, k) = readInts(3)
    val terminalStates = readIntArray(k)
    val graph = (0..n).map { ArrayList<Pair<Int, Char>>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(Pair(b, c[0]))
    }
    return DFA_G(n, terminalStates, graph)
}

fun main() {
    val DFA = readDFAG()
    val DFA2 = readDFAG()
    output {
        println(if (DFA.checkEquivalence(DFA2)) "YES" else "NO")
    }
}