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

fun queryAdd(rands: Sequence<UInt>, iterator: Iterator<UInt>): Triple<UInt, Int, Int> {
    val add = iterator.next() shr 8
    var l = iterator.next() shr 8
    var r = iterator.next() shr 8
    if (l > r) {
        val temp = l; l = r; r = temp
    }
    return Triple(add, l.toInt(), r.toInt())
}

fun querySum(rands: Sequence<UInt>, iterator: Iterator<UInt>): Pair<Int, Int> {
    var l = iterator.next() shr 8
    var r = iterator.next() shr 8
    if (l > r) {
        val temp = l; l = r; r = temp
    }
    return Pair(l.toInt(), r.toInt())
}


fun main() {
    val (m, q, a, b) = readInts(4)
    val rands = generateSequence(0u) { (a.toUInt() * it + b.toUInt()) }
    val iterator = rands.iterator()
    iterator.next() // seed
    val array = UIntArray((1 shl 24) + 1)
    for (i in 1..m) {
        val (add, l, r) = queryAdd(rands, iterator)
        array[l] += add
        array[r + 1] -= add
    }
    var current = 0u
    array.forEachIndexed { i, value ->
        current += value
        array[i] = (if (i > 0) array[i - 1] else 0u) + current
    }
    var sum = 0u
    for (i in 1..q) {
        val (l, r) = querySum(rands, iterator)
        sum += array[r] - (if (l > 0) array[l - 1] else 0u)
    }
    println(sum)
}

