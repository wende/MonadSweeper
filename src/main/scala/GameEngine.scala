import java.awt.{Color, Dimension}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JPanel, Timer}

import scala.swing._

abstract class GameEngine extends JPanel with ActionListener {

  val timer = new Timer(10, this)
  timer.start()

  override val preferredSize = new Dimension(200, 200)


  def actionPerformed(e: ActionEvent) {
    this.onTick()
    repaint()
  }
  def onTick()
}



