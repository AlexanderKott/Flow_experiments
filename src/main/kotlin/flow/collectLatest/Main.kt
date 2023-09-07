package flow.collectLatest

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

/**
 collectLatest – это такой collect, который прерывает свое выполнение в suspension point,
в тот момент из эмиттера летит новое значение. Обычный коллект всё бы принял, а этот сколлектит
входящее значение только если его не прервали. Поэтому тут часть данных (потенциально) теряется.

 Медленный коллектор получит только часть значений
 */
suspend fun main(args: Array<String>) = coroutineScope {
     val ints = flow<Int> { //передатчик
         for (i in 0..9){
             emit(i)
             println("emit $i")
         }
     }

    val time = measureTimeMillis {  //приемник
        ints.collectLatest  {
            println("collecting $it")
             delay(10) // точка саспенд-поинт
            println("collected $it")  // до сюда может и недойти
        }
    }

    println("Collected in $time ms")

}