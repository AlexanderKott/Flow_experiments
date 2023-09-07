package flow.hot.shared_flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow


/**
 *
 * tryEmit - non suspending variant of emit
 * Note that tryEmit will never emit a value with the default parameters
 * since the buffer is zero. It will only really work properly in cases where you know the
 * buffer is non zero and the overflow strategy is not SUSPEND.
 *
 *  Либо reply или extraBufferCapacity должен быть равен больше нуля.
 *  Работает не из суспенд функции.
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


fun main() {
    val f = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )


    CoroutineScope(Job() + Dispatchers.Default)
        .launch {

            f.collect {
                println("1] $it")
            }
        }

    //Thread.sleep(200)



    repeat(10) {
        f.tryEmit(it)
          Thread.sleep(100)
    }


    //Thread.sleep(3000)


}


