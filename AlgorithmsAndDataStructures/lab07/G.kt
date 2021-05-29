import java.io.PrintWriter
import java.util.*
import kotlin.math.log2
import kotlin.math.min
import kotlin.random.Random

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
private fun readInt() = read().toInt()
private fun readLongArray(n: Int) = LongArray(n) { read().toLong() }

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}


fun main() {
    val tree = IntervalTreeBulkUpdates(readLongArray(readInt()))
    var line = readLine()?.split(" ")
    output {
        var index = 0
        while (line != null) {
            val i = line!![1].toInt()
            val j = line!![2].toInt()
            when (line!![0]) {
                "min" -> println(tree.get(i, j))
                "set" -> tree.set(i, j, line!![3].toLong())
                else -> tree.add(i, j, line!![3].toLong())
            }
            line = readLine()?.split(" ")
            index++
        }
    }
//    randomTests()
}

class IntervalTreeBulkUpdates(private val array: LongArray) {

    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = LongArray(n)
    // 0 - nothing, 1 - add, 2 - set
    private val updateType = IntArray(n)
    private val updateValue = LongArray(n)

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = array[l - 1]
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
        t[v] = min(t[v * 2], t[v * 2 + 1])
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

    private fun updateBulk(v: Int, x: Long, type: Int) {
        require(v < n)
        when (type) {
            2 -> updateSet(v, x)
            1 -> updateAdd(v, x)
            else -> throw AssertionError()
        }
    }

    private fun updateSet(v: Int, x: Long) {
        t[v] = x
        updateType[v] = 2
        updateValue[v] = x
    }

    private fun updateAdd(v: Int, x: Long) {
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

    private fun update(l: Int, r: Int, x: Long, type: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size) {
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

    fun set(i: Int, j: Int, x: Long) {
        update(i, j, x, 2)
    }

    fun add(i: Int, j: Int, x: Long) {
        update(i, j, x, 1)
    }

    fun get(l: Int, r: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): Long {
        if (l == tl && r == tr) return t[v]
        push(v)
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, v * 2, tl, tm)
            l <= tm -> min(get(l, tm, v * 2, tl, tm), get(tm + 1, r, v * 2 + 1, tm + 1, tr))
            else -> get(l, r, v * 2 + 1, tm + 1, tr)
        }
    }
}

fun randomTests(m: Int = 100000, maxN: Int = 10000, maxValue: Long = 10000000000) {
    val a = LongArray(Random.nextInt(1, maxN + 1)) { Random.nextLong(1, maxValue) }
    println("a = ${a.contentToString()}")
    val tree = IntervalTreeBulkUpdates(a)
    val stupid = StupidSolution(a.copyOf())
    for (i_ in 1..m) {
        val cmd = Random.nextInt(3)
        var i = Random.nextInt(1, a.size + 1)
        var j = Random.nextInt(1, a.size + 1)
        if (i > j) {
            i = j.also { j = i }
        }
        when (cmd) {
            0 -> {
                val ans1 = tree.get(i, j)
                val ans2 = stupid.get(i, j)
                println("min($i, $j): True answer = $ans2, Your answer = $ans1")
                require(ans1 == ans2)
            }
            1 -> {
                val x = Random.nextLong(1, maxValue)
                tree.add(i, j, x)
                stupid.add(i, j, x)
                println("add($i, $j, $x). True array now: ${stupid.array.contentToString()}")
            }
            2 -> {
                val x = Random.nextLong(1, maxValue)
                tree.set(i, j, x)
                stupid.set(i, j, x)
                println("set($i, $j, $x). True array now: ${stupid.array.contentToString()}")
            }
        }
    }
}

class StupidSolution(val array: LongArray) {
    fun set(i: Int, j: Int, x: Long) = ((i - 1) until j).forEach { array[it] = x }

    fun add(i: Int, j: Int, x: Long) = ((i - 1) until j).forEach { array[it] += x }

    fun get(i: Int, j: Int) = ((i - 1) until j).map { array[it] }.minOrNull()!!
}
