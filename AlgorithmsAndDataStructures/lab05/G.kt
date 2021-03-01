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

class G {
    class Treap {
        class Node(_x: Int) {
            val x = _x
            val y = Random.nextInt()
            var sum: Long = _x.toLong()
            var leftChildren: Node? = null
            var rightChildren: Node? = null
        }

        private var root: Node? = null

        private fun getSum(node: Node?): Long {
            return node?.sum ?: 0
        }

        private fun updateNode(node: Node?) {
            if (node == null) return
            node.sum = node.x + getSum(node.leftChildren) + getSum(node.rightChildren)
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


        fun sum(l: Int, r: Int): Long {
            val subtree = split(r, root)
            val subtree2 = split(l - 1, subtree.first)
            val ans = getSum(subtree2.second)
            root = merge(merge(subtree2.first, subtree2.second), subtree.second)
            return ans
        }
    }
}

fun main() {
    val tree = G.Treap()
    val n = readInt()
    var last: Long = 0
    for (i in 1..n) {
        val line = readLn().split(" ")
        when {
            line[0] == "+" -> {
                tree.insert(((line[1].toInt() + last) % 1000000000).toInt())
                last = 0
            }
            line[0] == "?" -> {
                last = tree.sum(line[1].toInt(), line[2].toInt())
                println(last)
            }
            else -> throw AssertionError("???????")
        }
    }
}