package flow.catch_operator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


/**
 * Правило работы потока: Поток выполняется успешно или падает с ошибкой.
 * catch – используется чтобы ловить ошибки декларативным способом (как противоположность классическому try/catch). Он ловит ошибки в действиях выше себя. Например он может словить ошибки как и в эмиттере, так при потреблении, если блок catch {} написан ниже.
 *
 *  В функции main1 он словит только ошибку в эмиттере. А в функции main он словит ошибку и в эмиттере и при потреблении.
 *
 * Существуют так же способы перехватывать ошибки неправильно, надо свериться с документацией.
 */
suspend fun main(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        for (i in 0..9) {
            delay(100)
            emit(i)
            if (i > 7) throw error("error3!")
        }
    }



    f
        .onEach { value ->
        println(value)
        if (value > 8) throw error("error2!")
    }
        .catch { e -> println("cought $e") }
        .collect()

    println()
}

suspend fun main1(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        for (i in 0..9) {
            delay(100)
            emit(i)
            if (i > 5) throw error("error1!")
        }
    }.flowOn(Dispatchers.Default)


    f.catch { e -> println(e) }
        .collect {
            println(it)
        }
}