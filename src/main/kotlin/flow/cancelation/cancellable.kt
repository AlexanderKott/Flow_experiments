package flow.cancelation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 Фло как и корутину можно отменить, и он отменится в саспеншин поинт, но в примере ниже
 их нет, потому искусственно пишем cancellable(). Оно добавляет под капотом такую точку в методе onEach
 */


fun main() = runBlocking<Unit> {
    (1..5).asFlow().cancellable().collect { value ->
        if (value == 3) cancel()
        println(value)
    }
}
