package gameoflife.game

import cats.effect.{ ExitCode, IO }
import cats.effect.concurrent.Ref
import gameoflife.game.grid.Grid
import gameoflife.io.Output
import gameoflife.io.gui.LifePanel
import javax.swing.JFrame

class Life // empty class with its fake companion object: made to test Life's private methods

object Life {

  /**
    * Updating grid's state (iteration and livingCells)
    * Update results in an IO Monad, using Ref, rather than StateT (https://github.com/typelevel/cats-effect/issues/272)
    */
  private def nextGridState(state: Ref[IO, Grid]): IO[Grid] =
    state.modify(_.nextState)

  def displayAndEvolve(grid: Grid, gui: Option[JFrame])(implicit interval: Int): IO[Unit] =
    for {
      r <- Ref.of[IO, Grid](grid) // define State Ref for grid
      _ <- Output.displayAndSleep(grid, gui)
      g <- if (!LifePanel.pause) nextGridState(r) else IO.pure(grid)
      _ <- if (g.livingCells == grid.livingCells && !LifePanel.pause) IO.unit else displayAndEvolve(g, gui)
    } yield ()

}
