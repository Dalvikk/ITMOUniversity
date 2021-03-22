import java.io.PrintWriter
import java.lang.RuntimeException
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

class MultiMap<K, V> {
    private class Node<K, V>(_key: K, _value: V) {
        val key = _key
        var values = Set<V>()

        init {
            values.insert(_value)
        }
    }

    private val maxLoadFactor = 10
    private var list = Array<ArrayList<Node<K, V>>>(100000) { ArrayList() }
    private var size = 0

    private fun hash(x: K): Int {
        return abs(x.hashCode() % list.size)
    }

    private fun rebuild() {
        if (size > maxLoadFactor * list.size) {
            val old = list
            list = Array(old.size * 2) { ArrayList() }
            for (oldList in old) {
                for (node in oldList) {
                    list[hash(node.key)].add(node)
                }
            }
        }
    }

    private fun indexByKey(key: K): Int {
        for (i in 0 until list[hash(key)].size) {
            if (list[hash(key)][i].key == key) {
                return i
            }
        }
        return -1
    }

    fun existsKey(x: K): Boolean {
        return indexByKey(x) != -1
    }

    fun get(key: K): List<V> {
        val idx = indexByKey(key)
        if (idx == -1) return ArrayList()
        return list[hash(key)][idx].values.toList()
    }

    fun put(key: K, value: V) {
        rebuild()
        val idx = indexByKey(key)
        if (idx != -1) {
            list[hash(key)][idx].values.insert(value)
        } else {
            list[hash(key)].add(Node(key, value))
            size++
        }
    }

    fun deleteByKey(key: K, value: V) {
        val idx = indexByKey(key)
        if (idx != -1) {
            list[hash(key)][idx].values.delete(value)
        }
    }

    fun deleteAll(key: K) {
        val idx = indexByKey(key)
        if (idx != -1) {
            list[hash(key)].removeAt(idx)
            size--
        }
    }
}

fun main() {
    val multiMap = MultiMap<String, String>()
    output {
        var line = readLine();
        while (line != null) {
            val input = line.split(" ")
            when (input[0]) {
                "put" -> multiMap.put(input[1], input[2])
                "delete" -> multiMap.deleteByKey(input[1], input[2])
                "deleteall" -> multiMap.deleteAll(input[1])
                else -> {
                    val list = multiMap.get(input[1])
                    println(list.joinToString(separator = " ", prefix = "${list.size} "))
                }
            }
            line = readLine()
        }
    }
}

class Set<V> {
    private val maxLoadFactor = 1
    private var list = Array<ArrayList<V>>(16) { ArrayList() }
    private var size = 0

    private fun hash(x: V): Int {
        return abs(x.hashCode() % list.size)
    }

    private fun rebuild() {
        if (size > maxLoadFactor * list.size) {
            val old = list
            list = Array(old.size * 2) { java.util.ArrayList() }
            size = 0
            for (intList in old) {
                for (i in intList.toList()) {
                    insert(i)
                }
            }
        }
    }

    fun exists(x: V): Boolean {
        return list[hash(x)].contains(x)
    }

    fun insert(x: V) {
        rebuild()
        if (!exists(x)) {
            list[hash(x)].add(x)
            size++
        }
    }

    fun delete(x: V) {
        rebuild()
        if (exists(x)) {
            list[hash(x)].remove(x)
            size--
        }
    }

    fun toList(): List<V> {
        val ans = ArrayList<V>()
        for (arrayList in list) {
            ans.addAll(arrayList)
        }
        return ans
    }
}