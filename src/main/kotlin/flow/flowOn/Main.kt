package flow.flowOn

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext


/**
 * Если нам надо выполнить какую нибудь тяжелую операцию в потоке, не заблокировав
 * главный поток, то надо указать указать в каком потоке мы хотим это сделать.
 * Не правильно: вмешаться в поток с помощью withContext(Dispatchers.Default) как в main1,
 * так делать запрещено потому что могло бы привести к тяжело отслеживаемым ошибкам.
 *
 * Правильно:
 * 1 Указать withContext(Dispatchers.Default) в саспенд функции, которая предполагает тяжелые операции. Но
 * это может быть не удобно если надо весь код выполнить на этом диспатчере, а не только его часть. См main2
 *
 * 2 По умолчанию билдер flow {} работает в том же контексте что и его потребитель. Использовать оператор
 * flowOn, который создаст отдельную корутину(теперь производитель-потребитьель не будут работать
 * последовательно) и в ней выполнит все эти операции. flowOn действует на всё, что написано выше него.
 * См main
 *
 * dataFlow()
 * .map { opA(it) } // in contextA
 * .flowOn(contextA)
 * .map { opB(it) } // in collector's context
 */
suspend fun main(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        var i = 10
        while (isActive && i > 0) {
            emit(someDataComputation())
            i--
        }
    }.flowOn(Dispatchers.Default)


    f.collect {
        println(it)
    }
}


suspend fun main2(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        var i = 10
        while (isActive && i > 0) {
            emit(someDataComputation2()) //2
            i--
        }
    }

    f.collect {
        println(it)
    }
}



suspend fun main1(args: Array<String>) = coroutineScope {
    val f = flow<Int> {
        withContext(Dispatchers.Default) {  //1
            var i = 10
            while (isActive && i > 0) {
                emit(someDataComputation())
                i--
            }
        }
    }


    f.collect {
        println(it)
    }
}



suspend fun someDataComputation2(): Int =
    withContext(Dispatchers.Default) {
        return@withContext 555
    }

fun someDataComputation(): Int =   555