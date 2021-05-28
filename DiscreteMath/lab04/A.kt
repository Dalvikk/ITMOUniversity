import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("problem1.in")
private val OUTPUT = FileOutputStream("problem1.out")
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

class DFA(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Pair<Int, Char>>>
) {
    fun accept(word: String, start: Int = 1): Boolean {
        var current = start;
        for (c in word) {
            current = graph[current].find { it.second == c }?.first ?: 0
        }
        return current in terminalStates
    }
}

fun main() {
    val s = read()
    val (n, m, k) = readInts(3)
    val terminalStates = readIntArray(k)
    val graph = (0..n).map { ArrayList<Pair<Int, Char>>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(Pair(b, c[0]))
    }
    val dfa = DFA(n, terminalStates, graph)
    output {
        println(if (dfa.accept(s)) "Accepts" else "Rejects")
    }
}
