package flow.hot.shared_flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.swing.Swing
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.util.concurrent.TimeUnit
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea


/**
Эксперемент с буфером

replay - влияет на подписчиков, запоминает сколько элементов должны получить новые подписчики,
 если они подпишутся не сразу. Не влияет на источник данных

extraBufferCapacity - позволяет источникам не замедляться если потребители медленные

 */


suspend fun main(): Unit = coroutineScope {

    val (textAreaEmit, textAreaConsume1, textAreaConsume2) = initForm()

    val shared = MutableSharedFlow<Int>(
        extraBufferCapacity = 10,
        replay = 0,
        onBufferOverflow  = BufferOverflow.DROP_OLDEST

    )

       //consume 1
      CoroutineScope(Job() + Dispatchers.Swing).launch {
        shared.collect {
            val start = System.nanoTime()
                delay(1000)
                println("collected: $it")
                textAreaConsume1.append("\n  $it     ${calcTime(start)}")
            }
        }

    //consume 2
    CoroutineScope(Job() + Dispatchers.Swing).launch {
        shared.collect {
            val start = System.nanoTime()
            delay(2000)
            println("collected: $it")
            textAreaConsume2.append("\n  $it     ${calcTime(start)}")
        }
    }



        //emit
    CoroutineScope(Job() + Dispatchers.Swing).launch {
        delay(100)
        repeat(20) {
            val start = System.nanoTime()
            delay(400)
            shared.emit(it)
           // if (it == 6) delay(7000)
            println("emitted $it")
            textAreaEmit.append("\n  $it     ${calcTime(start)}")

        }
    }

    //consume.join()
}

private fun calcTime(start: Long) = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)


private fun initForm():  Triple<JTextArea,JTextArea,JTextArea> {
    val textAreaE = JTextArea("====emit====", 23, 15)
    textAreaE.setOpaque(false)
    textAreaE.isEditable = false
    textAreaE.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    val textAreaC1 = JTextArea("===consume 1===", 23, 15)
    textAreaC1.isEditable = false
    textAreaC1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    val textAreaC2 = JTextArea("===consume 2===", 23, 15)
    textAreaC2.isEditable = false
    textAreaC2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    val frame = JFrame("Flow demo")
    val pnl = JPanel()
    pnl.layout = FlowLayout()
    pnl.add(textAreaE)
    pnl.add(textAreaC1)
    pnl.add(textAreaC2)
    frame.add(pnl)
    frame.pack()
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(Dimension(700, 500))
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
    return  Triple(textAreaE, textAreaC1,textAreaC2)
}


