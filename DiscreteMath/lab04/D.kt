import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("problem4.in")
private val OUTPUT = FileOutputStream("problem4.out")
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

class DFA_D(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Int>>
) {
    private val mod = 1_000_000_007

    fun count(length: Int): Int {
        val dp = Array(length + 1) { IntArray(n + 1) }
        dp[0][1] = 1
        for (i in 0 until length) {
            for (j in 1..n) {
                graph[j].forEach {
                    dp[i + 1][it] += dp[i][j]
                    dp[i + 1][it] %= mod
                }
            }
        }
        return terminalStates.map { dp[length][it] }.reduce { acc, i -> (acc + i) % mod }
    }
}

fun main() {
    val (n, m, k, l) = readInts(4)
    val terminalStates = readIntArray(k)
    val graph = (0..n).map { ArrayList<Int>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(b)
    }
    val nfa = DFA_D(n, terminalStates, graph)
    output {
        println(nfa.count(l))
    }
}
