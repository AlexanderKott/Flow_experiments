package flow.combine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
combine – склеивает от 2-5 потоков в одно значение. Комбинации получаются исходя из того что прилетело
последним из каждого потока, потому какая комбинация будет в итоге значением, которое полетит в коллектор,
очень плохо предсказуемо. Они могут слепиться как zip'ом один-к-одному, а могут один-к-самому последнему,
не предсказуемо.
Комбинации получаются разные. Значения идут последовательно из всех потоков. Какие именно будут склейки,
сколько будет таких склеек – не известно, сильно зависит временем прихода значений. Гарантируется только что
последние значения, которые заимителись  – склеятся. И этого может быть вполне достаточно чтобы передать
последнее состояние системы, базирующееся на значениях из нескольких потоков:

userStateFlow
.combine(notificationsFlow) { userState, notifications ->
updateNotificationBadge(userState, notifications)
}
.collect()

 */
suspend fun main11() {



    val flow1 = flowOf("A", "B", "C", "D", "E")
          .onEach { delay(100) }

    val flow2 = flowOf(1, 2, 3, 4)
        .onEach { delay(100) }

    val flow3 = flowOf(0.1, 0.2)
        .onEach { delay(100) }

    combine(flow1, flow2, flow3 ) { f1, f2, f3 -> "${f1}_${f2}_${f3}" }
        .collect { println(it) }
}


suspend fun main() {

    val flow1 = flow<Int> {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)

    }
       // .onEach { delay(100) }

    val flow2 = flow<Int> {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
    }
       // .onEach { delay(100) }



    combine(flow1, flow2 ) { f1, f2  -> "${f1}_${f2}"  }
        .collect {

            println(it) }
}
