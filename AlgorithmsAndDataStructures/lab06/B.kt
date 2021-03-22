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

class HashMap {
    private val maxLoadFactor = 0.75
    private var list = Array<ArrayList<Pair<String, String>>>(16) { ArrayList() }
    private var size = 0

    private fun hash(x: String): Int {
        return abs(x.hashCode() % list.size)
    }

    private fun rebuild() {
        if (size > maxLoadFactor * list.size) {
            val old = list
            list = Array(old.size * 2) { ArrayList() }
            size = 0
            for (pairList in old) {
                for ((key, value) in pairList) {
                    put(key, value)
                }
            }
        }
    }

    private fun indexByKey(x: String): Int {
        for (i in 0 until list[hash(x)].size) {
            if (list[hash(x)][i].first == x) {
                return i
            }
        }
        return -1
    }

    fun existsKey(x: Pair<String, String>): Boolean {
        return indexByKey(x.first) != -1
    }

    fun get(x: String): String {
        val idx = indexByKey(x)
        return if (idx == -1) {
            "none"
        } else {
            list[hash(x)][idx].second
        }
    }

    fun put(key: String, value: String) {
        rebuild()
        val idx = indexByKey(key)
        if (idx != -1) {
            list[hash(key)][idx] = Pair(key, value)
        } else {
            list[hash(key)].add(Pair(key, value))
            size++
        }
    }

    fun deleteByKey(x: String) {
        rebuild()
        val idx = indexByKey(x)
        if (idx != -1) {
            list[hash(x)].removeAt(idx)
            size--
        }
    }
}

fun main() {
    val map = HashMap()
    output {
        var line = readLine();
        while (line != null) {
            val input = line.split(" ")
            when (input[0]) {
                "put" -> map.put(input[1], input[2])
                "get" -> println(map.get(input[1]))
                else -> map.deleteByKey(input[1])
            }
            line = readLine()
        }
    }
}