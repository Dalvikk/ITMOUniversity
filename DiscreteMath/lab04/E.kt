import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

private val INPUT = FileInputStream("problem5.in")
private val OUTPUT = FileOutputStream("problem5.out")
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

class DFA_E(
    val n: Int,
    private val terminalStates: List<Int>,
    private val graph: List<List<Int>>
) {
    private val mod = 1_000_000_007

    fun count(length: Int): Int {
        val dp = Array(length + 1) { IntArray(n + 1) }
        dp[0][1] = 1
        for (i in 0 until length) {
            for (j in 1..n) {
                graph[j].forEach {
                    dp[i + 1][it] += dp[i][j]
                    dp[i + 1][it] %= mod
                }
            }
        }
        return terminalStates.map { dp[length][it] }.reduce { acc, i -> (acc + i) % mod }
    }
}

class NFA_E(
    val n: Int,
    private val terminalStates: IntArray,
    private val graph: List<List<Pair<Int, Char>>>
) {

    fun toDFA(): DFA_E {
        val set = ArrayDeque<BitSet>()
        val index = HashMap<BitSet, Int>()
        val terminalSet = BooleanArray(n + 1)
        terminalStates.forEach { terminalSet[it] = true }

        val newTerminalStates = ArrayList<Int>()
        val newGraph = ArrayList<ArrayList<Int>>()
        newGraph.add(ArrayList()) // Zero vertex
        newGraph.add(ArrayList()) // First vertex

        val start = BitSet(n + 1)
        start.set(1)
        set.addLast(start)
        index[start] = 1
        if (terminalSet[1]) newTerminalStates.add(1)

        while (set.isNotEmpty()) {
            val curSet = set.removeFirst()
            for (i in 'a'..'z') {
                val newSet = BitSet(n + 1)
                var isTerminal = false
                var state = curSet.nextSetBit(0)
                while (state != -1) {
                    graph[state].filter { it.second == i }.forEach { (v, _) ->
                        newSet.set(v)
                        isTerminal = isTerminal or terminalSet[v]
                    }
                    state = curSet.nextSetBit(state + 1)
                }
                if (newSet.isEmpty) continue
                val curIdx = index[curSet]!!
                var newIdx = 0
                if (newSet !in index) {
                    newIdx = newGraph.size
                    index[newSet] = newIdx
                    set.addLast(newSet)
                    if (isTerminal) newTerminalStates.add(newIdx)
                    newGraph.add(ArrayList())
                } else {
                    newIdx = index[newSet]!!
                }
                newGraph[curIdx].add(newIdx)
            }
        }
        return DFA_E(newGraph.size - 1, newTerminalStates, newGraph)
    }
}

fun main() {
    val (n, m, k, l) = readInts(4)
    val terminalStates = readIntArray(k)
    val graph = (0..n).map { ArrayList<Pair<Int, Char>>() }
    for (i in 1..m) {
        val (a, b) = readInts(2)
        val c = read()
        graph[a].add(Pair(b, c[0]))
    }
    val nfa = NFA_E(n, terminalStates, graph)
    output {
        println(nfa.toDFA().count(l))
    }
}
