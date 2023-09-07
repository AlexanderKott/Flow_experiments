package flow.combine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * merge  – соединяет два и более потоков в один (все значения приходят из разных потоков а получаются из
 * одного). Сколько угодно потоков. Количество значений у потока может быть любым, например потоки не обязаны
 * иметь равное количество значений чтобы свестись в один в конце. Порядок прихода значений не гарантируется.
 * Задержки в эммитерах сильно влияют на порядок прихода значений.
 *
 */
suspend fun main() {
    val ints: Flow<Int> = flowOf(1, 2)
    val doubles: Flow<Double> = flowOf(0.1, 0.2, 0.3)
    val doubles2: Flow<Double> = flowOf(0.1111, 0.2222, 0.3333, 0.4444, 0.5555)
    val ints2: Flow<Int> = flowOf(-1, -2, -3)

    val together: Flow<Number> = merge(ints, doubles, ints2, doubles2)
    together.onEach { println(it)
    delay(500)
    }.collect()
    //  any other combination
}


//эксперимент с задержкой
suspend fun main1() {
    val ints: Flow<Int> = flowOf(1, 2, 3)
        .onEach { delay(1000) }
    val doubles: Flow<Double> = flowOf(0.1, 0.2, 0.3)

    val together: Flow<Number> = merge(ints, doubles)
    together.collect { println(it) }
}