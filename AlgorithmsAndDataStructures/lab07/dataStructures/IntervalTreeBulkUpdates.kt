package dataStructures

import kotlin.math.log2
import kotlin.math.min

class IntervalTreeBulkUpdates(private val array: LongArray) {

    private val n = 1 shl (log2(array.size.toDouble()).toInt() + 2)
    private val t = LongArray(n)
    // 0 - nothing, 1 - add, 2 - set
    private val updateType = IntArray(n)
    private val updateValue = LongArray(n)

    init {
        fun build(v: Int = 1, l: Int = 1, r: Int = array.size) {
            if (l == r) {
                t[v] = array[l - 1]
                return
            }
            val m = (l + r) / 2
            build(v * 2, l, m)
            build(v * 2 + 1, m + 1, r)
            updateNode(v)
        }
        build()
    }

    private fun updateNode(v: Int) {
        t[v] = min(t[v * 2], t[v * 2 + 1])
    }

    // Будем считать что если update[v] не пусто, то эта операция уже применена
    private fun push(v: Int) {
        val type = updateType[v]
        val x = updateValue[v]
        if (type != 0) {
            if (v * 2 < n) updateBulk(v * 2, x, type)
            if (v * 2 + 1 < n) updateBulk(v * 2 + 1, x, type)
            updateType[v] = 0
        }
    }

    private fun updateBulk(v: Int, x: Long, type: Int) {
        require(v < n)
        when (type) {
            2 -> updateSet(v, x)
            1 -> updateAdd(v, x)
            else -> throw AssertionError()
        }
    }

    private fun updateSet(v: Int, x: Long) {
        t[v] = x
        updateType[v] = 2
        updateValue[v] = x
    }

    private fun updateAdd(v: Int, x: Long) {
        when (updateType[v]) {
            2 -> {
                val newValue = updateValue[v] + x
                t[v] = newValue
                updateType[v] = 2
                updateValue[v] = newValue
            }
            1 -> {
                val newAdd = updateValue[v] + x
                t[v] += x
                updateType[v] = 1
                updateValue[v] = newAdd
            }
            else -> {
                t[v] += x
                updateType[v] = 1
                updateValue[v] = x
            }
        }
    }

    private fun update(l: Int, r: Int, x: Long, type: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size) {
        if (l == tl && r == tr) {
            updateBulk(v, x, type)
            return
        }
        push(v)
        val tm = (tl + tr) / 2
        when {
            r <= tm -> update(l, r, x, type, v * 2, tl, tm)
            l <= tm -> {
                update(l, tm, x, type, v * 2, tl, tm)
                update(tm + 1, r, x, type, v * 2 + 1, tm + 1, tr)
            }
            else -> update(l, r, x, type, v * 2 + 1, tm + 1, tr)
        }
        updateNode(v)
    }

    fun set(i: Int, j: Int, x: Long) {
        update(i, j, x, 2)
    }

    fun add(i: Int, j: Int, x: Long) {
        update(i, j, x, 1)
    }

    fun get(l: Int, r: Int, v: Int = 1, tl: Int = 1, tr: Int = array.size): Long {
        if (l == tl && r == tr) return t[v]
        push(v)
        val tm = (tl + tr) / 2
        return when {
            r <= tm -> get(l, r, v * 2, tl, tm)
            l <= tm -> min(get(l, tm, v * 2, tl, tm), get(tm + 1, r, v * 2 + 1, tm + 1, tr))
            else -> get(l, r, v * 2 + 1, tm + 1, tr)
        }
    }
}
