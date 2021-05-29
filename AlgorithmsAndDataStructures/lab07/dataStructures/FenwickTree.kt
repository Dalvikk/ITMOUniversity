package dataStructures

class FenwickTree<T>(private val n: Int) {
    val a = UIntArray(n) { 0u }

    fun sum(r: Int): UInt {
        var res = 0u
        var i = r
        while (i >= 0) {
            res += a[i]
            i = (i and (i + 1)) - 1
        }
        return res
    }

    fun sum(l: Int, r: Int): UInt {
        return sum(r) - sum(l - 1)
    }

    fun add(idx: Int, delta: Int) {
        var i = idx
        while (i < n) {
            a[i] += delta.toUInt()
            i = i or (i + 1)
        }
    }
}