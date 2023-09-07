package flow.cancelation

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


/**
 *  take() - промежуточный оператор. ставит лимит на значения и вызывает cancel:
 *, когда лимит будет исчерпан она вызывает cancel
 */
fun numbers():  Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun main() =  runBlocking <Unit> {
    numbers()
        .take(2) // take only the first two
        .collect { value -> println(value) }
}