package gameoflife.io

import cats.effect.IO
import gameoflife.game.grid.{ Cell, Grid }
import javax.swing.JFrame

object Output {

  def logError[T](msg: String): IO[Unit] =
    IO { println("\u001b[91merror\u001b[0m: " + msg) }

  private def displayTermGrid(grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      _ <- IO { println("\u001bc") } // clear screen
      _ <- IO { println(grid stringify iteration) }
    } yield ()

  private def displayGuiGrid(gui: Option[JFrame], grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      f <- IO.apply(gui.get) // crash case handled in displayAndSleep
      p <- IO.pure(new Gui.LifePanel(grid))
      _ <- IO.apply(f.setContentPane(p))
      _ <- IO.apply(f.setFocusable(true))
      _ <- IO.apply(f.setTitle(s"Game Of Life - Iteration #$iteration"))
      _ <- IO.apply(f.validate())
    } yield ()

  def displayAndSleep(grid: Grid, gui: Option[JFrame], iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      _ <- displayGuiGrid(gui, grid, iteration).handleErrorWith(_ => displayTermGrid(grid, iteration))
      _ <- IO.apply(Thread sleep interval).handleErrorWith(_ => IO.unit) // sleep before re-print
    } yield ()

}
