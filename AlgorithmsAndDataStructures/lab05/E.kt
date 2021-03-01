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
    val n = readInt()
    val x = IntArray(n + 1)
    val y = IntArray(n + 1)
    val p = IntArray(n + 1)
    val left = IntArray(n + 1)
    val right = IntArray(n + 1)

    val nodes = IntArray(n)

    for (i in 1..n) {
        val (a, b) = readInts(2)
        x[i] = a
        y[i] = b
        nodes[i - 1] = i
    }
    val sorted = nodes.sortedBy { x[it] }

    val stack = Stack<Int>()
    for (node in sorted) {
        var last = 0
        while (!stack.empty() && y[stack.peek()] > y[node]) {
            last = stack.pop()
        }
        if (!stack.empty()) {
            right[stack.peek()] = node
            p[node] = stack.peek()
        }
        stack.add(node)
        if (last != 0) {
            p[last] = node
            left[node] = last
        }
    }
    output {
        println("YES")
        for (node in 1..n) {
            println(p[node].toString() + " " + left[node] + " " + right[node])
        }
    }
}

