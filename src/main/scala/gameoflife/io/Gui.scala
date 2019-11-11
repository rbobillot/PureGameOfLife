package gameoflife.io

import java.awt.event.{ KeyEvent, KeyListener, MouseWheelListener }
import java.awt.{ Color, Dimension, Graphics, Graphics2D }

import cats.effect.IO
import gameoflife.game.grid.{ Cell, Grid }
import javax.swing.border.TitledBorder
import javax.swing.{ JFrame, JPanel, JScrollPane }

object Gui {

  private val BASESCALE: Int = 10

  private var SCALE: Int = BASESCALE
  private var x, y = 0
  private var negative = true

  class LifePanel(grid: Grid) extends JPanel with KeyListener {

    addKeyListener(this)

    override def keyPressed(e: KeyEvent): Unit = {
      e.getKeyCode match {
        case 61 /* VK_PLUS? */ | KeyEvent.VK_I if SCALE < 30 => SCALE += 1
        case KeyEvent.VK_MINUS | KeyEvent.VK_O if SCALE > 1  => SCALE -= 1
        case KeyEvent.VK_LEFT                                => x += 1
        case KeyEvent.VK_RIGHT                               => x -= 1
        case KeyEvent.VK_UP                                  => y += 1
        case KeyEvent.VK_DOWN                                => y -= 1
        case KeyEvent.VK_N                                   => negative = !negative
        case _                                               => ()
      }
    }
    override def keyReleased(e: KeyEvent): Unit = ()
    override def keyTyped(e: KeyEvent): Unit = ()

    private def drawAxis(g: Graphics2D): IO[Unit] =
      for {
        _ <- IO.apply(g.setColor(if (negative) Color.DARK_GRAY else Color.LIGHT_GRAY))
        _ <- IO.apply(g.drawLine(-5000 + x, y * SCALE, 5000 + x, y * SCALE))
        _ <- IO.apply(g.drawLine(x * SCALE, -5000 + y, x * SCALE, 5000 + y))
      } yield ()

    private def drawCells(g: Graphics2D): IO[Unit] =
      for {
        _ <- IO.apply(g.setColor(if (negative) Color.WHITE else Color.BLACK))
        lvCells <- IO.pure(grid.livingCells)
        padCell <- IO.pure((c: Cell) => c.copy(x = c.x + x, y = c.y + y))
        putCell <- IO.pure((c: Cell) => g.fillRect(c.x * SCALE, c.y * SCALE, SCALE, SCALE))
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

  def initGUI(grid: Grid): IO[JFrame] =
    for {
      f <- IO.pure(new JFrame)
      p <- IO.pure(new LifePanel(grid))
      d <- IO.pure(new Dimension(grid.columns * BASESCALE, grid.rows * BASESCALE))
      _ <- IO.apply(p.setPreferredSize(d))
      _ <- IO.apply(f.setDefaultCloseOperation(3)) // EXIT_ON_CLOSE
      _ <- IO.apply(f.getContentPane.add(p))
      _ <- IO.apply(f.pack())
      _ <- IO.apply(f.setVisible(true))
      _ <- IO.apply(f.addKeyListener(p))
    } yield f

}
