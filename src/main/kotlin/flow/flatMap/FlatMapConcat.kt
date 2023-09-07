package flow.flatMap

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
Обычный flatMap для коллекции -  из списков значений делает один список, содержащий значения из всех
этих списков.
val allEmployees: List<Employee> = departments
.flatMap { department -> department.employees }

// а если бы мы использовали бы обычный map то получили бы список списков (что не то что надо)
val listOfListsOfEmployee: List<List<Employee>> = departments
.map { department -> department.employees }


а flatMapConcat –  объединяет два потока, причем внешний поток ждет все значения внутреннего до конца.
Получается как бы умножение значений внешнего потока на значения внутренних. Работает последовательно.
Значения не теряются. Порядок обработки сообщений не нарушается.

Эксперимент:
вне зависимости от задержек или их отсутствия показал стабильный результат:
данные пришли в прямом порядке перемноженными из двух потоков

0 A 0                    at 157 ms from start
0 A 1                    at 263 ms from start
0 A 2                    at 376 ms from start
1 A 0                    at 1494 ms from start
1 A 1                    at 1604 ms from start
1 A 2                    at 1715 ms from start
2 A 0                    at 2826 ms from start
2 A 1                    at 2935 ms from start
2 A 2                    at 3044 ms from start



 Еще пример объединения списков в отдельные значения
 */


suspend fun main(args: Array<String>) = coroutineScope {

    val startTime = System.currentTimeMillis() //приемник

    flow<List<Int>> {
            emit(listOf(1, 2, 3))
            emit(listOf(4, 5, 6))
            emit(listOf(7, 8, 9))
    }

        .flatMapConcat {
            it.asFlow()
        }

        .collect { value ->
            delay(300)
            println("  \"$value\"                    at ${System.currentTimeMillis() - startTime} ms from start")
        }
}



suspend fun main1(args: Array<String>) = coroutineScope {

    val startTime = System.currentTimeMillis() //приемник

    flow<Int> {
        repeat(3) {
            emit(it)
        }
    }

        .flatMapConcat {value ->
            flow<String> {
                repeat(3) {
                  //  delay(1000)
                    emit("$value A $it")
                }

            }
        }
        .collect { value ->
            println("$value                    at ${System.currentTimeMillis() - startTime} ms from start")
        }
}