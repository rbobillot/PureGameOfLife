package gameoflife

import scala.util.Try
import cats.effect.{ ExitCode, IO, IOApp }
import cats.syntax.all._
import gameoflife.game.Life
import gameoflife.game.grid.Setup
import gameoflife.io.Output
import gameoflife.io.Config.refreshInterval.defaultInterval
import gameoflife.io.Config.outputMethod.gui
import gameoflife.io.gui.Gui

object Main extends IOApp with Setup {

  private def createLife(file: String)(implicit interval: Int): IO[ExitCode] =
    initGrid(file).attempt.flatMap {
      case Left(error) => Output.logError(error.getMessage).as(ExitCode.Error)
      case Right(grid) =>
        for {
          g <- Gui.initGUI(grid).map(Option(_).filter(_ => gui)) // returns Some(JFrame) if GUI
          _ <- Life.displayAndEvolve(grid, g)
        } yield ExitCode.Success
    }

  def run(args: List[String]): IO[ExitCode] =
    args match {
      case file :: interval :: Nil => createLife(file)(Try(interval.toInt).getOrElse(defaultInterval))
      case file :: Nil             => createLife(file)(defaultInterval)
      case _                       => Output.logError("no grid file given").as(ExitCode.Error)
    }

}
