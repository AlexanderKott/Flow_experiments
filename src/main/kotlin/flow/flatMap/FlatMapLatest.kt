package flow.flatMap

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
а flatMapLatest – так же как и предыдущие склеивает два потока. По работе похож на flatMapConcat,
но с большим отличием – он стремится обработать только самое последние значение. Предполагается что
мне как дизайнеру программы не важны все значения, а важно только то что было последним. Например
пользователь вводит в поле адрес, при дописывании каждый новой буквы ему выдается подсказка.
Подсказка нужна только для того набора букв который введен последним, для старых наборов она уже
не нужна, и они будут автоматически отбрасываться.

Поэтому каждое новое значение в этом режиме отменяет предыдущее. Но если эммитер работает медленно
то в теории они обработаются все (но на это не стоит полагаться)

 */
suspend fun main(args: Array<String>) = coroutineScope {

   val startTime = System.currentTimeMillis()

    flow<Int> {
        repeat(3) {
         //    delay(1000)
            emit(it)
        }
    }

        .flatMapLatest {value ->
            flow<String> {
                repeat(3) {
                    delay(1000)
                    emit("$value A $it")
                }

            }
        }
        .collect { value ->
            println("$value              at ${System.currentTimeMillis() - startTime} ms from start")
        }


}