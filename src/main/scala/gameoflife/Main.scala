package gameoflife

import scala.util.Try

import cats.effect.{ ExitCode, IO, IOApp }
import cats.syntax.all._

import gameoflife.game.Life
import gameoflife.game.grid.Setup
import gameoflife.io.Output
import gameoflife.io.Config.refreshInterval.defaultInterval
import gameoflife.io.Config.outputMethod.gui

object Main extends IOApp with Setup {

  private def createLife(file: String)(implicit interval: Int): IO[ExitCode] =
    initGrid(file).attempt.flatMap {
      case Right(grid) => Life.displayAndEvolve(grid, gui).as(ExitCode.Success)
      case Left(error) => Output.logError(error.getMessage).as(ExitCode.Error)
    }

  def run(args: List[String]): IO[ExitCode] =
    args match {
      case file :: interval :: Nil => createLife(file)(Try(interval.toInt).getOrElse(defaultInterval))
      case file :: Nil             => createLife(file)(defaultInterval)
      case _                       => Output.logError("no grid file given").as(ExitCode.Error)
    }

}
