import java.io.PrintWriter
import java.lang.AssertionError
import java.util.*
import kotlin.collections.ArrayList
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

class F {
    class Treap {
        class Node(_x: Int, _data: Int) {
            var x = _x
            val y = Random.nextInt()
            val data = _data
            var add = 0
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
            node.size = 1
            if (node.leftChildren != null) node.size += node.leftChildren!!.size
            if (node.rightChildren != null) node.size += node.rightChildren!!.size
        }

        private fun push(node: Node?) {
            if (node == null) return
            node.x += node.add
            if (node.leftChildren != null) node.leftChildren!!.add += node.add
            if (node.rightChildren != null) node.rightChildren!!.add += node.add
            node.add = 0
        }

        private fun split(key: Int, cur: Node?): Pair<Node?, Node?> {
            push(cur)
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

        private fun split_kth(k: Int, cur: Node?): Pair<Node?, Node?> {
            if (k > getSize(cur)) throw AssertionError("Split's size is greater than elements exists")
            if (k < 0) throw AssertionError("Size cannot be negative")
            push(cur)
            return when {
                cur == null -> return Pair(null, null)
                getSize(cur.leftChildren) + 1 <= k -> {
                    val tmp = split_kth(k - getSize(cur.leftChildren) - 1, cur.rightChildren)
                    cur.rightChildren = tmp.first
                    updateNode(cur)
                    Pair(cur, tmp.second)
                }
                else -> {
                    val tmp = split_kth(k, cur.leftChildren)
                    cur.leftChildren = tmp.second
                    updateNode(cur)
                    Pair(tmp.first, cur)
                }
            }
        }

        private fun merge(node1: Node?, node2: Node?): Node? {
            if (node1 == null) return node2
            if (node2 == null) return node1
            push(node1)
            push(node2)
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
            push(cur)
            return when {
                cur == null -> false
                key == cur.x -> true
                key < cur.x -> exists(key, cur.leftChildren)
                else -> exists(key, cur.rightChildren)
            }
        }

        fun insert(key: Int, data: Int, to: Int) {
            val subtree = split(key - 1, root)
            val subtree2 = split(to, subtree.second)
            if (subtree2.first != null && exists(key, subtree2.first)) {
                subtree2.first!!.add += 1
            }
            root = merge(merge(merge(subtree.first, Node(key, data)), subtree2.first), subtree2.second)
        }

        fun getMaxKey(): Int {
            if (getSize(root) == 0) throw IndexOutOfBoundsException()
            val subtree = split_kth(getSize(root) - 1, root)
            val ans = subtree.second?.x ?: 0
            root = merge(subtree.first, subtree.second)
            return ans
        }

        fun deleteMin(startIdx: Int): Node? {
            val subtree = split(startIdx - 1, root)
            val subtree2 = split_kth(1, subtree.second)
            root = merge(subtree.first, subtree2.second)
            return subtree2.first
        }

        fun saveNodes(list: ArrayList<Node>, cur: Node? = root) {
            if (cur == null) return
            push(cur)
            saveNodes(list, cur.leftChildren)
            list.add(cur)
            saveNodes(list, cur.rightChildren)
        }
    }
}

fun main() {
    val (n, m) = readInts(2)
    val zeroTreap = F.Treap()
    val dataTreap = F.Treap()
    for (i in 1..(n + m)) {
        zeroTreap.insert(i, 0, 0)
    }
    for ((idx, value) in readInts(n).withIndex()) {
        val nearestZero = zeroTreap.deleteMin(value)!!.x
        dataTreap.insert(value, idx + 1, nearestZero)
    }
    val maxNonEmpty = dataTreap.getMaxKey()
    output {
        println(maxNonEmpty)
        val list = ArrayList<F.Treap.Node>()
        dataTreap.saveNodes(list)
        for (i in 0 until list.size) {
            val prev = if (i == 0) 0 else list[i - 1].x
            for (j in prev + 1 until list[i].x) {
                print("0 ")
            }
            print("${list[i].data} ")
        }
    }
}