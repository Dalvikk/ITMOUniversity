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

class LinkedMap<K, V> {
    private class Node<K, V>(_key: K, _value: V, _next: Node<K, V>? = null, _prev: Node<K, V>? = null) {
        val key = _key
        var value = _value
        var next = _next
        var prev = _prev
    }

    private val maxLoadFactor = 2
    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null
    private var list = Array<ArrayList<Node<K, V>>>(16) { ArrayList() }
    private var size = 0

    private fun hash(x: K): Int {
        return abs(x.hashCode() % list.size)
    }

    private fun rebuild() {
        if (size > maxLoadFactor * list.size) {
            val old = list
            list = Array(old.size * 2) { ArrayList() }
            val list = toList()
            size = 0
            head = null
            tail = null
            for ((key, value) in list) {
                put(key, value)
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

    fun get(key: K): V? {
        val idx = indexByKey(key)
        return if (idx == -1) {
            null
        } else {
            list[hash(key)][idx].value
        }
    }

    fun put(key: K, value: V) {
        rebuild()
        val idx = indexByKey(key)
        if (idx != -1) {
            list[hash(key)][idx].value = value
        } else {
            val node = Node(key, value, _next = null, _prev = tail)
            list[hash(key)].add(node)
            tail?.next = node
            tail = node
            if (size == 0) {
                head = node
            }
            size++
        }
    }

    fun deleteByKey(key: K) {
        rebuild()
        val idx = indexByKey(key)
        if (idx != -1) {
            val node = list[hash(key)][idx]
            node.prev?.next = node.next
            node.next?.prev = node.prev
            list[hash(key)].removeAt(idx)
            size--
            if (size == 0) {
                head = null
                tail = null
            } else if (node === head) {
                head = node.next
            } else if (node === tail) {
                tail = node.prev
            }
        }
    }

    fun prev(key: K): V? {
        val idx = indexByKey(key)
        if (idx == -1) return null
        return list[hash(key)][idx].prev?.value
    }

    fun next(key: K): V? {
        val idx = indexByKey(key)
        if (idx == -1) return null
        return list[hash(key)][idx].next?.value
    }

    fun toList(): List<Pair<K, V>> {
        val list = ArrayList<Pair<K, V>>()
        var cur = head
        while (cur != tail) {
            if (cur == null) {
                throw RuntimeException()
            }
            list.add(Pair(cur.key, cur.value))
            cur = cur.next
        }
        if (tail != null) {
            list.add(Pair(tail!!.key, tail!!.value))
        }
        return list
    }
}

fun main() {
    val linkedMap = LinkedMap<String, String>()
    output {
        var line = readLine();
        while (line != null) {
            val input = line.split(" ")
            when (input[0]) {
                "put" -> linkedMap.put(input[1], input[2])
                "delete" -> linkedMap.deleteByKey(input[1])
                "get" -> println(linkedMap.get(input[1]) ?: "none")
                "prev" -> println(linkedMap.prev(input[1]) ?: "none")
                else -> println(linkedMap.next(input[1]) ?: "none")
            }
            line = readLine()
        }
    }
}