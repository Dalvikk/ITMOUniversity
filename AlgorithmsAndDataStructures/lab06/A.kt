import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

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

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

class HashSet {
    private val maxLoadFactor = 10
    private var list = Array(16) { IntList() }
    private var size = 0

    private fun rebuild() {
        if (size > maxLoadFactor * list.size) {
            val old = list
            list = Array(list.size * 2) { IntList() }
            size = 0
            for (intList in old) {
                for (i in intList.toList()) {
                    insert(i)
                }
            }
        }
    }

    private fun hash(x: Int): Int {
        return abs(x.hashCode() % list.size)
    }

    fun exists(x: Int): Boolean {
        return list[hash(x)].contains(x)
    }

    fun insert(x: Int) {
        if (!list[hash(x)].contains(x)) {
            rebuild()
            list[hash(x)].add(x)
            size++
        }
    }

    fun delete(x: Int) {
        if (list[hash(x)].contains(x)) {
            list[hash(x)].remove(x)
            size--
        }
    }

    private class IntList {
        private var size = 0
        private var a = IntArray(2)
        private var exists = BooleanArray(2)

        fun contains(x: Int): Boolean {
            val idx = a.indexOf(x)
            return idx != -1 && exists[idx]
        }

        private fun ensureCapacity(size: Int) {
            if (a.size < size) {
                a = a.copyOf(2 * size)
                exists = exists.copyOf(2 * size)
            }
        }

        fun add(x: Int) {
            for (i in 0 until size) {
                if (a[i] == x) {
                    exists[i] = true
                    return
                }
            }
            ensureCapacity(size + 1)
            a[size] = x
            exists[size] = true
            size++
        }

        fun remove(x: Int) {
            for (i in 0 until size) {
                if (a[i] == x) {
                    exists[i] = false
                    return
                }
            }
        }

        fun toList(): List<Int> {
            val ans = ArrayList<Int>()
            for (i in 0 until size) {
                if (exists[i]) {
                    ans.add(a[i])
                }
            }
            return ans
        }
    }
}

fun main() {
    val set = HashSet()
    output {
        var line = readLine()
        var i = 0
        while (line != null) {
            val (cmd, x) = line.split(" ")
            when (cmd) {
                "insert" -> set.insert(x.toInt())
                "delete" -> set.delete(x.toInt())
                else -> println(set.exists(x.toInt()))
            }
            line = readLine();
            i++
            if (i % 100_000 == 0) {
                System.gc()
            }
        }
    }
}