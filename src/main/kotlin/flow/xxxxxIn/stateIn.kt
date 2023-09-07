package flow.xxxxxIn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


suspend fun main(args: Array<String>) = coroutineScope {
    val f = flow<Int> {

    }.flowOn(Dispatchers.Default)


    f.collect {
        println(it)
    }
}


