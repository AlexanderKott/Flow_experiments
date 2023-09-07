package flow.xxxxxIn

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 *  launchIn - коллект холодного потока на требуемой корутине
 *
 *  TODO а горячий поток она может запустить?
 */
val flow = flow<String> {
    val ctx = currentCoroutineContext()
    val name = ctx[CoroutineName]?.name
    emit("Message1 $name")
    } .onEach {
    println("el $it")
}


suspend fun main(): Unit = coroutineScope {

    flow.launchIn(this)

    withContext(CoroutineName("Name1")) {
        flow.launchIn(this)
    }

    withContext(CoroutineName("Name2")) {
        flow.launchIn(this)
    }

}


