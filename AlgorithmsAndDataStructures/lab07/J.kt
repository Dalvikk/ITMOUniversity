import java.util.*
import kotlin.math.log2

private val INPUT = System.`in`
private val _reader = INPUT.bufferedReader()
private var _tokenizer: StringTokenizer = StringTokenizer("")

private fun read(): String {
    while (_tokenizer.hasMoreTokens().not()) {
        _tokenizer = StringTokenizer(_reader.readLine() ?: return "", " ")
    }
    return _tokenizer.nextToken()
}

private fun readInt() = read().toInt()

data class Rectangle(var x1: Int, var y1: Int, var x2: Int, var y2: Int)

fun readRectangle(): Rectangle {
    val x1 = readInt()
    val y1 = readInt()
    val x2 = readInt() + 1
    val y2 = readInt()
    return Rectangle(x1, y1, x2, y2)
}

fun compress(rectangles: Array<Rectangle>): Pair<IntArray, IntArray> {
    val allX = ArrayList<Int>()
    val allY = ArrayList<Int>()
    for (r in rectangles) {
        allX.add(r.x1)
        allX.add(r.x2)
        allY.add(r.y1)
        allY.add(r.y2)
    }
    allX.sort()
    allY.sort()
    val realX = allX.distinct().toIntArray()
    val realY = allY.distinct().toIntArray()
    for (r in rectangles) {
        r.x1 = realX.binarySearch(r.x1)
        r.x2 = realX.binarySearch(r.x2)
        r.y1 = realY.binarySearch(r.y1)
        r.y2 = realY.binarySearch(r.y2)
    }
    return Pair(realX, realY)
}

fun max(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
    return if (a.first >= b.first) {
        a
    } else {
        b
    }
}

fun main() {
    val n = readInt()
    val rectangles = Array(n) { readRectangle() }
    val (realX, realY) = compress(rectangles)
    val events = Array(realX.size) { ArrayList<Triple<Int, Int, Int>>() }
    for (r in rectangles) {
        events[r.x1].add(Triple(r.y1, r.y2, 1))
        events[r.x2].add(Triple(r.y1, r.y2, -1))
    }
    val tree = IntervalTreeBulkUpdates_J(IntArray(realY.size))
    var ansValue = 0
    var ansX = 0
    var ansY = 0
    for (i in events.indices) {
        for ((y, y2, add) in events[i]) {
            tree.add(y + 1, y2 + 1, add)
        }
        val curAns = tree.get(1, realY.size)
        if (curAns.first > ansValue) {
            ansValue = curAns.first
            ansX = realX[i]
            ansY = realY[curAns.second - 1]
        }
    }
    println(ansValue)
    println("$ansX $ansY")
}

class IntervalTreeBulkUpdates_J(private val array: IntArray) {

    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = IntArray(n)
    private val tIndex = IntArray(n)

    // 0 - nothing, 1 - add, 2 - set
    private val updateType = IntArray(n)
    private val updateValue = IntArray(n)

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = array[l - 1]
                tIndex[v] = l
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
        if (t[v * 2] >= t[v * 2 + 1]) {
            t[v] = t[v * 2]
            tIndex[v] = tIndex[v * 2]
        } else {
            t[v] = t[v * 2 + 1]
            tIndex[v] = tIndex[v * 2 + 1]
        }
    }

    // Будем считать что если update[v] не пусто, то эта операция уже применена
    private fun push(v: Int) {
        val type = updateType[v]
        val x = updateValue[v]
        if (type != 0) {
            if (v * 2 < n) updateBulk(v * 2, x, type)
            if (v * 2 + 1 < n) updateBulk(v * 2 + 1, x, type)
            updateType[v] = 0
        }
    }

    private fun updateBulk(v: Int, x: Int, type: Int) {
        require(v < n)
        when (type) {
            2 -> updateSet(v, x)
            1 -> updateAdd(v, x)
            else -> throw AssertionError()
        }
    }

    private fun updateSet(v: Int, x: Int) {
        t[v] = x
        updateType[v] = 2
        updateValue[v] = x
    }

    private fun updateAdd(v: Int, x: Int) {
        when (updateType[v]) {
            2 -> {
                val newValue = updateValue[v] + x
                t[v] = newValue
                updateType[v] = 2
                updateValue[v] = newValue
            }
            1 -> {
                val newAdd = updateValue[v] + x
                t[v] += x
                updateType[v] = 1
                updateValue[v] = newAdd
            }
            else -> {
                t[v] += x
                updateType[v] = 1
                updateValue[v] = x
            }
        }
    }

    private fun update(l: Int, r: Int, x: Int, type: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size) {
        if (l == tl && r == tr) {
            updateBulk(v, x, type)
            return
        }
        push(v)
        val tm = (tl + tr) / 2
        when {
            r <= tm -> update(l, r, x, type, v * 2, tl, tm)
            l <= tm -> {
                update(l, tm, x, type, v * 2, tl, tm)
                update(tm + 1, r, x, type, v * 2 + 1, tm + 1, tr)
            }
            else -> update(l, r, x, type, v * 2 + 1, tm + 1, tr)
        }
        updateNode(v)
    }

    fun set(i: Int, j: Int, x: Int) {
        update(i, j, x, 2)
    }

    fun add(i: Int, j: Int, x: Int) {
        update(i, j, x, 1)
    }

    fun get(l: Int, r: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): Pair<Int, Int> {
        if (l == tl && r == tr) return Pair(t[v], tIndex[v])
        push(v)
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, v * 2, tl, tm)
            l <= tm -> max(get(l, tm, v * 2, tl, tm), get(tm + 1, r, v * 2 + 1, tm + 1, tr))
            else -> get(l, r, v * 2 + 1, tm + 1, tr)
        }
    }
}
