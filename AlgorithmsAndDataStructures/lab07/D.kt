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

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

fun main() {
    val n = readInt()
    val array = Array(n) { readInt() }
    val tree = IntervalTree(array, ::min)
    val lines = generateSequence { readLine() }
    output {
        for (line in lines) {
            val (cmd, a, b) = line.split(" ")
            if (cmd == "min") {
                println(tree.get(a.toInt(), b.toInt()))
            } else {
                tree.update(a.toInt(), b.toInt())
            }
        }
    }
}


class IntervalTree<T>(private val array: Array<T>, private val f: (T, T) -> T) {
    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = MutableList<T?>(n) { null }

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = array[l - 1]
                return
            }
            val m = (l + r) / 2
            build(v * 2, l, m)
            build(v * 2 + 1, m + 1, r)
            t[v] = f(t[v * 2]!!, t[v * 2 + 1]!!)
        }
        build()
    }

    fun update(index: Int, value: T, v: Int = 1, l: Int = 1, r: Int = array.size) {
        if (l == r) {
            t[v] = value
            return
        }
        val m = (l + r) / 2
        if (index <= m) {
            update(index, value, v * 2, l, m)
        } else {
            update(index, value, v * 2 + 1, m + 1, r)
        }
        t[v] = f(t[v * 2]!!, t[v * 2 + 1]!!)
    }

    fun get(l: Int, r: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): T {
        // Inv: tl <= l <= r <= tr
        if (l == tl && r == tr) return t[v]!!
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, v * 2, tl, tm)
            l <= tm ->  // l <= tm < r
                f(
                    get(l, tm, v * 2, tl, tm),
                    get(tm + 1, r, v * 2 + 1, tm + 1, tr)
                )
            else ->  // tm < l <= r
                get(l, r, v * 2 + 1, tm + 1, tr)
        }
    }
}
