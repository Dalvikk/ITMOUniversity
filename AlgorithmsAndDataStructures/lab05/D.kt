import java.io.PrintWriter
import java.lang.AssertionError
import java.util.*
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

class D {
    class KthMaximumTreap {
        class Node(_x: Int) {
            val x = _x
            val y = Random.nextInt()
            var size = 1
            var leftChildren: Node? = null
            var rightChildren: Node? = null
        }

        private var root: Node? = null

        private fun getSize(node: Node?): Int {
            return node?.size ?: 0
        }

        private fun updateNode(node: Node?) {
            if (node == null) return
            node.size = 1 + getSize(node.leftChildren) + getSize(node.rightChildren)
        }

        private fun split(key: Int, cur: Node?): Pair<Node?, Node?> {
            return when {
                cur == null -> return Pair(null, null)
                cur.x <= key -> {
                    val tmp = split(key, cur.rightChildren)
                    cur.rightChildren = tmp.first
                    updateNode(cur)
                    Pair(cur, tmp.second)
                }
                else -> {
                    val tmp = split(key, cur.leftChildren)
                    cur.leftChildren = tmp.second
                    updateNode(cur)
                    Pair(tmp.first, cur)
                }
            }
        }

        private fun merge(node1: Node?, node2: Node?): Node? {
            if (node1 == null) return node2
            if (node2 == null) return node1
            return if (node1.y < node2.y) {
                node1.rightChildren = merge(node1.rightChildren, node2)
                updateNode(node1)
                node1
            } else {
                node2.leftChildren = merge(node1, node2.leftChildren)
                updateNode(node2)
                node2
            }
        }

        fun exists(key: Int, cur: Node? = root): Boolean {
            return when {
                cur == null -> false
                key == cur.x -> true
                key < cur.x -> exists(key, cur.leftChildren)
                else -> exists(key, cur.rightChildren)
            }
        }

        fun insert(key: Int) {
            if (exists(key, root)) return
            val subtree = split(key, root)
            root = merge(merge(subtree.first, Node(key)), subtree.second)
        }

        fun delete(key: Int) {
            if (!exists(key, root)) return
            val subtree = split(key, root)
            val subtree2 = split(key - 1, subtree.first)
            root = merge(subtree2.first, subtree.second)
        }

        fun prev(key: Int, cur: Node? = root, result: Int? = null): Int? {
            if (cur == null) return result
            val newResult = if (cur.x < key && (result == null || result < cur.x)) cur.x else result
            return if (cur.x < key) {
                prev(key, cur.rightChildren, newResult)
            } else {
                prev(key, cur.leftChildren, newResult)
            }
        }

        fun next(key: Int, cur: Node? = root, result: Int? = null): Int? {
            if (cur == null) return result
            val newResult = if (key < cur.x && (result == null || cur.x < result)) cur.x else result
            return if (cur.x <= key) {
                next(key, cur.rightChildren, newResult)
            } else {
                next(key, cur.leftChildren, newResult)
            }
        }

        fun KthMaximum(k: Int, cur: Node? = root): Int {
            if (getSize(cur) < k || cur == null) throw AssertionError("k is greater than elements exists")
            if (k < 0) throw AssertionError("k cannot be negative")
            return when {
                getSize(cur.rightChildren) + 1 == k -> cur.x
                getSize(cur.rightChildren) >= k -> KthMaximum(k, cur.rightChildren)
                else -> KthMaximum(k - getSize(cur.rightChildren) - 1, cur.leftChildren)
            }
        }
    }
}


fun main() {
    val tree = D.KthMaximumTreap()
    val n = readInt()
    for (i in 1..n) {
        val (cmd, k) = readInts(2)
        when (cmd) {
            1 -> tree.insert(k)
            0 -> println(tree.KthMaximum(k))
            -1 -> tree.delete(k)
        }
    }
}