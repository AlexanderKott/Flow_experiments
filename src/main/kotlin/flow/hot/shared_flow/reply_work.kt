package flow.hot.shared_flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow


/**
 *
 * tryEmit - non suspending variant of emit
 *
 *
 *
 * f.subscriptionCount.value- посмотреть сколько подписчиков
 * resetReplayCache - resets the replay cache. It is useful for very transient data that should
 * not be replayed to collectors when its upstream is not active.
 *
 *  replay - how many items are replayed to new subscribers (optional, defaults to zero);
 * extraBufferCapacity - how many items are buffered in addition to replay so that emit does not suspend while there is a buffer remaining (optional, defaults to zero);
 * onBufferOverflow - configures an action on buffer overflow (optional, defaults to suspending emit call, supported only when replay > 0 or extraBufferCapacity > 0).
 *
 */


/**
 * Эксперимент показывает, что подписчики горячего потока получают только что, что успело им придти сразу с
 * момента подписки. Что не похоже на холодный фло, где подписчики получают всё с самого начала.
 *
 * Если имитить из «место 1» никто не успеет подписаться и соответственно никто ничего не получит
 * (если задержки на эмит нет).
 * Если имитить из «место 2» то все получат всё, потому что успели подписаться.
 * Если имитить из «место 1» с reply = 1 то все получат последнее заимиченное значение, даже если
 * подписались поздно.
 *
 * Ещё. Горячий поток бесконечно имитит, отменить его работу если надо, можно с помощью кэнсела
 * корутины подписчика.
 */
suspend fun main() : Unit = coroutineScope {
    val f = MutableSharedFlow<Int>()

    println("a1 subscribers ${f.subscriptionCount.value}")

    //место 1
    launch {
        repeat(10) {

             f.emit(it)
            delay(500)
        }
     }




    val x = CoroutineScope(Job() + Dispatchers.Default)
        .launch {

            f.collect {
                println("1] $it")
            }
        }



/* delay(3000)

    val j2 = launch {
        f.collect {
            println("2] $it")
        }
    }*/


    //место 2



    //отмена
 /*   println("a2 subscribers ${f.subscriptionCount.value}")
    j2.cancelAndJoin()
    println("a3 subscribers ${f.subscriptionCount.value}")
    x.cancelAndJoin()
    println("a4 subscribers ${f.subscriptionCount.value}")*/
}


