package flow.cancelation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
  Фло который отменится по таймауту с помощью withTimeoutOrNull
 */
fun main() =  runBlocking <Unit> {

    fun simple(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    withTimeoutOrNull(250) { // Timeout after 250ms
        simple().collect { value -> println(value) }
    }
    println("Done")
}
