package flow.lifecycle

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
Здесь показаны эвенты, которые применимы к flow

fun updateNews() {
aFlow()
.onStart { showProgressBar() }  //перед началом эмита
.onCompletion { hideProgressBar() } //в конце эмита
.onEmpty {  println("onEmpty")  } //сработает если не было значений
.onEach { view.showNews(it) }  //на каждом эмите. Тут коллектить хорошо, потому что а а catch (ниже) отловит все ошибки если они встретятся. Если коллектить с помощью collect то  надо там писать дополнительный классический трай кетч
.catch { view.handleError(it)
view.showStab()  //показать заглушку (например пустой список) с случае ошибки
emit("Error") - либо заэмитить какое-то значение
}
.launchIn(viewModelScope)
}
 */
class MyError : Throwable("My error")

val flow = flow<String> {
     emit("Message1 ")
     emit("Message2 ")
}.onStart { println("Before") }
    .onEach {
        //throw MyError()
        println("el $it")
    }
    .onEmpty {  println("onEmpty")  }
    .onCompletion {
        println("end")
    }
    .catch { println("Caught $it") }

suspend fun main(): Unit = coroutineScope {
    flow.collect()

}

