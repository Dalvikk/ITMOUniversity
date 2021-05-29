import java.util.*
import kotlin.math.log2

private val INPUT = System.`in`
private val OUTPUT = System.out
private val _reader = INPUT.bufferedReader()
private var _tokenizer: StringTokenizer = StringTokenizer("")

private fun read(): String {
    while (_tokenizer.hasMoreTokens().not()) {
        _tokenizer = StringTokenizer(_reader.readLine() ?: return "", " ")
    }
    return _tokenizer.nextToken()
}

private fun readInt() = read().toInt()

var a = 0u
var b = 0u
var cur = 0u

fun nextRand17(): Int {
    cur = cur * a + b
    return (cur shr 15).toInt()
}

fun nextRand24(): Int {
    cur = cur * a + b
    return (cur shr 8).toInt()
}

fun main() {
    val q = readInt()
    a = readInt().toUInt()
    b = readInt().toUInt()
    val tree = MergeIntervalTree(LongArray(1 shl 17) { nextRand24().toLong() })
    var ans = 0u
    for (i in 1..q) {
        var l = nextRand17()
        var r = nextRand17()
        if (l > r) l = r.also { r = l }
        var x = nextRand24()
        var y = nextRand24()
        if (x > y) x = y.also { y = x }
        val curAns = tree.get(l + 1, r + 1, x, y)
        b += curAns.toUInt()
        ans += curAns.toUInt()
    }
    println(ans)
}

class MergeIntervalTree(private val array: LongArray) {
    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = Array(n) { LongArray(0) }

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = longArrayOf(array[l - 1])
                return
            }
            val m = (l + r) / 2
            build(v * 2, l, m)
            build(v * 2 + 1, m + 1, r)
            updateNode(v)
        }
        build()
    }

    private fun updateNode(v: Int) {
        val n = t[v * 2].size
        val m = t[v * 2 + 1].size
        t[v] = LongArray(n + m)
        var i = 0
        var j = 0
        while (i + j < n + m) {
            if (i < n && (j == m || t[v * 2][i] <= t[v * 2 + 1][j])) {
                t[v][i + j] = t[v * 2][i]
                i++
            } else {
                t[v][i + j] = t[v * 2 + 1][j]
                j++
            }
        }
    }

    fun update(index: Int, value: Long, v: Int = 1, l: Int = 1, r: Int = array.size) {
        if (l == r) {
            t[v] = longArrayOf(value)
            return
        }
        val m = (l + r) / 2
        if (index <= m) {
            update(index, value, v * 2, l, m)
        } else {
            update(index, value, v * 2 + 1, m + 1, r)
        }
        updateNode(v)
    }

    fun get(l: Int, r: Int, x: Int, y: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): Int {
        if (l == tl && r == tr) {
            return upperBound(t[v], y) - lowerBound(t[v], x)
        }
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, x, y, v * 2, tl, tm)
            l <= tm -> get(l, tm, x, y, v * 2, tl, tm) + get(tm + 1, r, x, y, v * 2 + 1, tm + 1, tr)
            else -> get(l, r, x, y, v * 2 + 1, tm + 1, tr)
        }
    }

    private fun lowerBound(a: LongArray, key: Int): Int {
        var left = -1
        var right = a.size
        while (left + 1 != right) {
            val m = (left + right) / 2
            if (a[m] >= key) {
                right = m
            } else {
                left = m
            }
        }
        return right
    }

    private fun upperBound(a: LongArray, key: Int): Int {
        var left = -1
        var right = a.size
        while (left + 1 != right) {
            val m = (left + right) / 2
            if (a[m] > key) {
                right = m
            } else {
                left = m
            }
        }
        return right
    }
}
