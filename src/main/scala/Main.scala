import swing._
import java.awt.{Graphics, Color}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.Timer

object ColorPanel extends SimpleSwingApplication {
  private var c: Color = new Color(0)

  def top = new MainFrame {
    title = "Flash!"
    contents = p
    preferredSize = new Dimension(200,200)
    minimumSize = new Dimension(400,400)
  }

  val engine = new Panel with ActionListener {
    override val preferredSize = new Dimension(200, 200)

    override def paintComponent(g: Graphics2D) {
      g.setColor(c)
      g.fillRect(0, 0, size.width, size.height)
    }

    def actionPerformed(e: ActionEvent) {
      c = new Color((c.getRGB() + 1000) % 16777216)
      repaint
    }
  }

  val timer = new Timer(10, p)
  timer.start()
}