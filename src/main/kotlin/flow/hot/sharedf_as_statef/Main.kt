package flow.hot.sharedf_as_statef

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*


/**
Шаред фло настроеный на типичный режим работы стэйт фло
 */


suspend fun main(): Unit = coroutineScope {
    val shared = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    shared.tryEmit("InitialState") // emit the initial value
    val state = shared.distinctUntilChanged() // get StateFlow-like behavior

}

