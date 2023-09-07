package flow.hot.rx_subject

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.util.Random
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea


/**
 Симуляция rx subject на фло

 Если вызвать у холодного потока внутри коллект из горячего потока, то холодный поток
 никогда не завершится, что и требуется


 Трюк (1) с replay = 1 делает так чтобы фло имитил сразу же
 */

val triggerFlow = MutableSharedFlow<Unit>(replay = 1) //1  replay = 1
val scope = CoroutineScope(Dispatchers.Default)


fun getRandomValue() : Flow<Int> = flow {
    triggerFlow.emit(Unit) //1
    triggerFlow.collect {
        emit(Random().nextInt())
    }
}

suspend fun main(): Unit = coroutineScope {
   val (field, button) = initForm()

    button.addActionListener {
        scope.launch {
            triggerFlow.emit(Unit)
        }

    }

    getRandomValue().collect {
        field.append("\n random value = $it")
        println("random value = $it" )
    }


}



private fun initForm():  Pair<JTextArea, JButton> {
    val textAreaE = JTextArea("====emit====", 23, 20)
    textAreaE.setOpaque(false)
    textAreaE.isEditable = false
    textAreaE.setBorder(BorderFactory.createLineBorder(Color.BLACK));


   val button = JButton("Next value")

    val frame = JFrame("Flow demo")
    val pnl = JPanel()
    pnl.layout = FlowLayout()
    pnl.add(textAreaE)
    pnl.add(button)
    frame.add(pnl)
    frame.pack()
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(Dimension(700, 500))
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
    return  Pair(textAreaE, button)
}
