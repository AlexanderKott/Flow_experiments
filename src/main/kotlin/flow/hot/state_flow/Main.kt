package flow.hot.state_flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*


/**

 */


suspend fun main(): Unit = coroutineScope {
    val state = MutableStateFlow<String>("init")

    state.tryEmit("InitialState")  //bool
    state.emit("InitialState") //unit
    state.compareAndSet("expect", "update") //bool
    state.value = "abc"

}

