import java.io.PrintWriter
import java.util.*

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

private fun readInt() = read().toInt()
private fun readInts(n: Int) = List(n) { read().toInt() }

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

class Solution(_n: Int, _graph: Array<out List<Int>>) {
    private val graph = _graph
    private val n = _n

    var maxDistance: Int = 0
    var longVertex: Int = 0
    var center: Int = -1
    var ans = false

    fun bfs(v: Int) {
        val used = BooleanArray(n) { false }
        val distance = IntArray(n) { -1 }
        val parent = IntArray(n) { -1 }
        used[v] = true
        distance[v] = 0
        val queue = ArrayDeque<Int>()
        queue.push(v)
        while (!queue.isEmpty()) {
            val cur = queue.pop()
            for (to in graph[cur]) {
                if (!used[to]) {
                    used[to] = true
                    distance[to] = distance[cur] + 1
                    parent[to] = cur
                    queue.push(to)
                }
            }
        }
        longVertex = 0
        maxDistance = distance[0]
        for (i in 0 until n) {
            if (distance[i] > maxDistance) {
                longVertex = i
                maxDistance = distance[i]
            }
        }
        center = longVertex
        for (i in 0 until (maxDistance / 2)) {
            center = parent[center]
        }
    }

    fun dfs(v: Int, used: BooleanArray, isRoot: Boolean = false): Long {
        used[v] = true
        val childrenHashs = ArrayList<Long>()
        var hash1 = 1L
        var hash2 = 0L
        for (to in graph[v]) {
            if (!used[to]) {
                childrenHashs.add(dfs(to, used))
                hash1 *= childrenHashs[childrenHashs.size - 1]
                hash2 += childrenHashs[childrenHashs.size - 1]
            }
        }
        if (isRoot) {
            ans = childrenHashs[0] == childrenHashs[1]
        }
        return hash1 + hash2;
    }

    fun solve(): Boolean {
        bfs(0)
        bfs(longVertex)
        if (maxDistance % 2 == 1 || graph[center].size != 2) {
            return false
        }
        val used = BooleanArray(n) { false }
        dfs(center, used, true)
        return ans
    }
}

fun main() {
    val n = readInt()
    val graph = Array<ArrayList<Int>>(n) { ArrayList() }
    for (i in 1 until n) {
        val (a, b) = readInts(2)
        graph[a - 1].add(b - 1)
        graph[b - 1].add(a - 1)
    }
    print(if (Solution(n, graph).solve()) "YES\n" else "NO\n")
}
