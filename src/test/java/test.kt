import java.time.LocalDate

fun myfun(arg1: Int): Int {
    return 5
}


fun main() {
    parse(arrayOf(0, 1, 2, 3 ,4 ,5 ,6 ,7 ,8 ,9))

}

fun calcTax(sum: Double, ratio: Float = 0.13F): Double {
    return sum * ratio
}

fun test(arg1: Int, arg2: Int) = arg1 + arg2

fun parse(arr: Array<Int>) {
    for (i in arr.indices) {
        println("$i, ${arr[i]}")
    }
}