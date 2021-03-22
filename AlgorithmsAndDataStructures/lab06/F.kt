// "Aa".hashCode() == "BB".hashCode()
// 65 * 31 + 97 == 66 * 31 + 66

fun main() {
    for (cur in 0 until readLine()!!.toInt()) println(
        Integer.toBinaryString(cur).padStart(32, '0').replace("0", "Aa").replace("1", "BB")
    )
}
