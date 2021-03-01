import java.io.PrintWriter
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

class B {
    class Treap {
        class Node(_x: Int) {
            val x = _x
            val y = Random.nextInt()
            var leftChildren: Node? = null
            var rightChildren: Node? = null
        }

        private var root: Node? = null

        private fun split(key: Int, cur: Node?): Pair<Node?, Node?> {
            return when {
                cur == null -> return Pair(null, null)
                cur.x <= key -> {
                    val tmp = split(key, cur.rightChildren)
                    cur.rightChildren = tmp.first
                    Pair(cur, tmp.second)
                }
                else -> {
                    val tmp = split(key, cur.leftChildren)
                    cur.leftChildren = tmp.second
                    Pair(tmp.first, cur)
                }
            }
        }

        private fun merge(node1: Node?, node2: Node?): Node? {
            if (node1 == null) return node2
            if (node2 == null) return node1
            return if (node1.y < node2.y) {
                node1.rightChildren = merge(node1.rightChildren, node2)
                node1
            } else {
                node2.leftChildren = merge(node1, node2.leftChildren)
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
    }
}

fun main() {
    val lines = generateSequence { readLine() }
    val tree = B.Treap()
    for (line in lines) {
        val words = line.split(" ")
        val (cmd, x) = Pair(words[0], words[1].toInt())
        when (cmd) {
            "insert" -> tree.insert(x)
            "delete" -> tree.delete(x)
            "exists" -> println(tree.exists(x))
            "next" -> println(tree.next(x) ?: "none")
            "prev" -> println(tree.prev(x) ?: "none")
            else -> assert(false)
        }
    }
}