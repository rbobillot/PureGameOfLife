package gameoflife.io

import cats.effect.IO

import gameoflife.game.grid.{ Cell, Grid }
import Config.cellOutputColors

object Output {

  def logError[T](msg: String): IO[Unit] =
    IO { println("\u001b[91merror\u001b[0m: " + msg) }

  private def displayTermGrid(grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      _ <- IO { println("\u001bc") } // clear screen
      _ <- IO { println(grid stringify iteration) }
    } yield ()

  private def displayGuiGrid(grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    IO.unit

  def displayAndSleep(grid: Grid, gui: Boolean, iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      _ <- if (gui) displayGuiGrid(grid, iteration) else displayTermGrid(grid, iteration)
      _ <- IO { Thread sleep interval } // sleep before re-print
    } yield ()

}
