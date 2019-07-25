package gameoflife.io

import cats.effect.IO

import gameoflife.game.grid.{ Cell, Grid }
import Config.cellOutputColors

object Output {

  def logError[T](msg: String): IO[Unit] =
    IO { println("\u001b[91merror\u001b[0m: " + msg) }

  def displayGrid(grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    IO { println(grid stringify iteration) }

  def displayAndSleep(grid: Grid, iteration: Int)(implicit interval: Int): IO[Unit] =
    for {
      _ <- IO { println("\u001bc") } // clear screen
      _ <- displayGrid(grid, iteration)
      _ <- IO { Thread sleep interval } // sleep before re-print
    } yield ()

}
