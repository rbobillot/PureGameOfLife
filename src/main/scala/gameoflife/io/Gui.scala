package gameoflife.io

import java.awt.{ Color, Dimension, Graphics, Graphics2D }

import cats.effect.IO
import gameoflife.game.grid.Grid
import javax.swing.{ JFrame, JPanel }

object Gui {

  private val SCALE: Short = 10

  class LifePanel(grid: Grid) extends JPanel {
    override def paint(gs: Graphics): Unit = {
      for {
        g <- IO.apply(gs.asInstanceOf[Graphics2D])
        _ <- IO.apply(this.removeAll())
        _ <- IO.apply(this.setBackground(Color.BLACK))
        _ <- IO.apply(g.setColor(Color.WHITE))
      } yield grid.livingCells.foreach(c => g.fillRect(c.x * SCALE, c.y * SCALE, SCALE, SCALE))
    }.unsafeRunSync()
  }

  private def createPanel(grid: Grid): IO[JPanel] =
    for {
      p <- IO.pure(new LifePanel(grid))
      d <- IO.pure(new Dimension(grid.columns * SCALE, grid.rows * SCALE))
      _ <- IO.apply(p.setPreferredSize(d))
    } yield p

  def initGUI(grid: Grid): IO[JFrame] =
    for {
      f <- IO.pure(new JFrame("Game Of Life - Iteration #0"))
      p <- createPanel(grid)
      _ <- IO.apply(f.getContentPane.add(p))
      _ <- IO.apply(f.pack())
      _ <- IO.apply(f.setVisible(true))
    } yield f

}
