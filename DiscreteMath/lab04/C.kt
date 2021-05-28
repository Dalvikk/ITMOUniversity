import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("problem3.in")
private val OUTPUT = FileOutputStream("problem3.out")
private val _reader = INPUT.bufferedReader()
private val _writer = PrintWriter(OUTPUT, false)
private var _tokenizer: StringTokenizer = StringTokenizer("")

private fun read(): String {
    while (_tokenizer.hasMoreTokens().not()) {
        _tokenizer = StringTokenizer(_reader.readLine() ?: return "", " ")
    }
    return _tokenizer.nextToken()
}

private fun readInts(n: Int) = List(n) { read().toInt() }
private fun readIntArray(n: Int) = IntArray(n) { read().toInt() }

private inline fun output(block: PrintWriter.() -> Unit) {
    _writer.apply(block).flush()
}

class DFAC(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Pair<Int, Char>>>
) {
    private val mod = 1_000_000_007
    private val reverseGraph = calcReverseGraph()

    private fun calcReverseGraph(): List<List<Pair<Int, Char>>> {
        val ans = List(n + 1) { ArrayList<Pair<Int, Char>>() }
        graph.forEachIndexed { it, v ->
            v.forEach { it2 -> ans[it2.first].add(Pair(it, it2.second)) }
        }
        return ans
    }

    private fun dfs(v: Int, used: BooleanArray, graph: List<List<Pair<Int, Char>>>) {
        used[v] = true
        for ((to, _) in graph[v]) {
            if (!used[to]) {
                dfs(to, used, graph)
            }
        }
    }

    private fun goodNodes(): Collection<Int> {
        val ans = HashSet<Int>()
        val used = BooleanArray(n + 1)
        val used2 = BooleanArray(n + 1)
        dfs(1, used, graph)
        for (state in terminalStates) {
            if (!used2[state]) {
                dfs(state, used2, reverseGraph)
            }
        }
        for (i in 1..n) {
            if (used[i] && used2[i]) {
                ans.add(i)
            }
        }
        return ans
    }

    private fun dfs_calc(
        v: Int,
        color: IntArray,
        count: IntArray,
        graph: List<List<Pair<Int, Char>>>,
        good: Collection<Int>
    ) {
        color[v] = 1
        for ((to, _) in graph[v]) {
            if (to !in good) continue
            if (color[to] == 1) {
                throw AssertionError()
            }
            if (color[to] == 0) {
                dfs_calc(to, color, count, graph, good)
            }
            count[v] += count[to]
            count[v] %= mod
        }
        color[v] = 2
    }


    fun count(): Int {
        val nodes = goodNodes()
        val color = IntArray(n + 1)
        val count = IntArray(n + 1)
        count[1] = 1
        try {
            for (state in terminalStates) {
                if (color[state] == 0) {
                    dfs_calc(state, color, count, reverseGraph, nodes)
                }
            }
        } catch (e: AssertionError) {
            return -1
        }
        return terminalStates.map { count[it] }.reduce { acc, i -> (acc + i) % mod }
    }
}

fun main() {
    val (n, m, k) = readInts(3)
    val terminalStates = readIntArray(k)
    val graph = List(n + 1) { ArrayList<Pair<Int, Char>>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(Pair(b, c[0]))
    }
    val dfa = DFAC(n, terminalStates, graph)
    output {
        println(dfa.count())
    }
}
