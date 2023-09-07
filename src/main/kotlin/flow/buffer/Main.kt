package flow.buffer

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

/**
Производитель и потребитель работают в синхронном режиме по умолчанию. Если один из них тормозит,
то тормозят оба. Если они оба тормозят то они будут ждать друг друга, тормоза каждого суммируются и
время простоя соответственно будет в два раза больше.
Мы можем это побороть, заставив их работать параллельно.

Сделаем так, чтобы производитель и потребитель работали на разных корутинах.
Внутри это будет использован канал, который передают данные из одной корутины в другую.
Просто допишем buffer()  к ints, как внизу. Теперь общее время передачи данных ускорится,
что можно увидеть экспериментально удалив/добавив функцию  buffer()  в коде ниже.

 */
suspend fun main(args: Array<String>) = coroutineScope {
     val ints = flow<Int> { //передатчик
         for (i in 0..9){
             delay(100)

             emit(i)
             println("emit $i")
         }
     }

    val time = measureTimeMillis {  //приемник
        ints.buffer().collect {//
            delay(100)
            println("collect $it")
        }
    }

    println("Collected in $time ms")

}