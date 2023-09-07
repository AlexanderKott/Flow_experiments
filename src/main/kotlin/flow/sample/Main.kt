package flow.sample

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.sample


/**
а sample  – берет значение из потока каждые (сколько указанно) секунд, остальные значения пропадают
 */
suspend fun main(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        repeat(20) {
            emit(it)
            delay(100)
        }

    }.sample(300)


    f.collect {
        println(it)
    }
}


