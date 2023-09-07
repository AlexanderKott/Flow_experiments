package flow.flatMap

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

/**
а flatMapMerge – тоже перемножает значения как flatMapConcat, но делает это параллельно,
порядок следования значений не гарантируется, но зато быстрее за счето параллельности.
Подходит например для сетевого запроса для списка значений. Нужно отобразить автараки для пользователей,
не важно чтобы они отобразились одна за одной, важно чтобы это произошло как можно быстрее, а порядок не важен.

Эксперемент Задержки влияют на порядок обработкие результата:
flatMapMerge – дал три разных результа
при отсутствии задержек – данные коллектятся рандомно (каждый новый запуск новый рандом)
при задержке в основном потоке комбинация будет одна
при задержке в внутреннем потоке – комбинация будет другой


Пример выполнения с обычной последовательной map

listOf("s","b","s","g","d","r","t").asFlow()
.map { fetchColumns(it) }
.onEach { resultMutableData.postValue(it) }
.launchIn(viewModelScope)

А вот пример того же самого с параллельным выполнением:
listOf("s","b","s","g","d","r","t").asFlow()
.onEach { println("onEach: $it") }
.flatMapMerge {
flow {
emit(fetchColumns(it))
}
}
.onEach { resultMutableData.postValue(it)) }
.launchIn(viewModelScope)



 */
suspend fun main(args: Array<String>) = coroutineScope {

    val startTime = System.currentTimeMillis() //приемник

    flow<Int> {
        repeat(3) {
            emit(it)
        }
    }

        .flatMapMerge {value ->
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