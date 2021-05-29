import java.util.*
import kotlin.random.Random

fun main() {
    randomTests()
}

fun randomTests(n: Int = 10000, m: Int = 10000) {
    val (badTree, slowTree) = randomTrees(n)
    badTree.initLCA()
    slowTree.initLCA()
    for (i in 0 until m) {
        val u = Random.nextInt(0, n)
        val v = Random.nextInt(0, n)
        val goodAns = slowTree.LCA(u, v)
        val badAns = badTree.LCA(u, v)
        require(goodAns == badAns) { "WA for u = $u, v = $v. Correct answer = $goodAns, your = $badAns" }
    }
    println("OK, $m tests passed")
}

fun randomTrees(n: Int): Pair<Tree, TreeTest> {
    println("n = $n")
    val parents = IntArray(n - 1) { Random.nextInt(0, it + 1) }
    val graph = List(n) { ArrayList<Int>() }
    for ((i, value) in parents.withIndex()) {
        graph[value].add(i + 1)
    }
    println("parents: ${parents.contentToString()}")
    return Pair(Tree(n, graph), TreeTest(n, graph, parents))
}


class TreeTest(
    private val n: Int,
    private val graph: List<List<Int>>,
    private val p: IntArray,
) {
    private val timeIn = IntArray(n)
    private val timeOut = IntArray(n)
    private var counter = 0

    fun initLCA() {
        dfs(0, 0)
    }

    private fun dfs(v: Int, depth: Int) {
        timeIn[v] = counter++
        for (u in graph[v]) {
            dfs(u, depth + 1)
        }
        timeOut[v] = counter++
    }

    fun isAncestor(u: Int, v: Int) = timeIn[u] <= timeIn[v] && timeOut[u] >= timeOut[v]

    fun LCA(u: Int, v: Int): Int {
        var cur = u
        while (!isAncestor(cur, v)) {
            cur = p[cur - 1]
        }
        return cur
    }
}
