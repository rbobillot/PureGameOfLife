package gameoflife.io.gui

import java.awt.{ Color, Graphics, Graphics2D }
import java.awt.event.{ KeyEvent, KeyListener }

import cats.effect.IO
import gameoflife.game.grid.{ Cell, Grid }
import javax.swing.JPanel

object LifePanel {
  private var scale = 10
  private var x, y = 0
  private var negative = true

  var pause = false
}

class LifePanel(grid: Grid) extends JPanel with KeyListener {

  import gameoflife.io.gui.LifePanel._

  addKeyListener(this)

  override def keyPressed(e: KeyEvent): Unit = {
    e.getKeyCode match {
      case 61 /* VK_PLUS? */ | KeyEvent.VK_I if scale < 30 => scale += 1
      case KeyEvent.VK_MINUS | KeyEvent.VK_O if scale > 1  => scale -= 1
      case KeyEvent.VK_LEFT                                => x += 1
      case KeyEvent.VK_RIGHT                               => x -= 1
      case KeyEvent.VK_UP                                  => y += 1
      case KeyEvent.VK_DOWN                                => y -= 1
      case KeyEvent.VK_N                                   => negative = !negative
      case KeyEvent.VK_SPACE                               => pause = !pause
      case _                                               => ()
    }
  }
  override def keyReleased(e: KeyEvent): Unit = ()
  override def keyTyped(e: KeyEvent): Unit = ()

  private def drawAxis(g: Graphics2D): IO[Unit] =
    for {
      _ <- IO.apply(g.setColor(if (negative) Color.DARK_GRAY else Color.LIGHT_GRAY))
      _ <- IO.apply(g.drawLine(-5000 + x, y * scale, 5000 + x, y * scale))
      _ <- IO.apply(g.drawLine(x * scale, -5000 + y, x * scale, 5000 + y))
    } yield ()

  private def drawCells(g: Graphics2D): IO[Unit] =
    for {
      _ <- IO.apply(g.setColor(if (negative) Color.WHITE else Color.BLACK))
      lvCells <- IO.pure(grid.livingCells)
      padCell <- IO.pure((c: Cell) => c.copy(x = c.x + x, y = c.y + y))
      putCell <- IO.pure((c: Cell) => g.fillRect(c.x * scale, c.y * scale, scale - 1, scale - 1))
    } yield lvCells.foreach(padCell andThen putCell)

  override def paint(gs: Graphics): Unit = {
    for {
      g <- IO.apply(gs.asInstanceOf[Graphics2D])
      _ <- IO.apply(removeAll())
      _ <- IO.apply(if (negative) setBackground(Color.BLACK))
      _ <- drawAxis(g)
      _ <- drawCells(g)
    } yield ()
  }.unsafeRunSync()

}