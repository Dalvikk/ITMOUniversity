package dataStructures

import kotlin.math.log2

class IntervalTree<T>(private val array: Array<T>,
                      private val updateF: (T, T) -> T,
                      private val getF: (T, T) -> T) {
    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = MutableList<T?>(n) { null }

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = array[l - 1]
                return
            }
            val m = (l + r) / 2
            build(v * 2, l, m)
            build(v * 2 + 1, m + 1, r)
            t[v] = updateF(t[v * 2]!!, t[v * 2 + 1]!!)
        }
        build()
    }

    fun update(index: Int, value: T, v: Int = 1, l: Int = 1, r: Int = array.size) {
        if (l == r) {
            t[v] = value
            return
        }
        val m = (l + r) / 2
        if (index <= m) {
            update(index, value, v * 2, l, m)
        } else {
            update(index, value, v * 2 + 1, m + 1, r)
        }
        t[v] = updateF(t[v * 2]!!, t[v * 2 + 1]!!)
    }

    fun get(l: Int, r: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): T {
        // Inv: tl <= l <= r <= tr
        if (l == tl && r == tr) return t[v]!!
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, v * 2, tl, tm)
            l <= tm ->  // l <= tm < r
                getF(get(l, tm, v * 2, tl, tm), get(tm + 1, r, v * 2 + 1, tm + 1, tr))
            else ->  // tm < l <= r
                get(l, r, v * 2 + 1, tm + 1, tr)
        }
    }
}
