package gameoflife.game

import cats.effect.{ ExitCode, IO }
import cats.effect.concurrent.Ref

import gameoflife.game.grid.Grid
import gameoflife.io.Output

class Life // empty class with its fake companion object: made to test Life's private methods

object Life {

  /**
    * Updating grid's state (iteration and livingCells)
    * Update results in an IO Monad, using Ref, rather than StateT (https://github.com/typelevel/cats-effect/issues/272)
    */
  private def nextGridState(state: Ref[IO, Grid]): IO[Grid] =
    state.modify(_.nextState)

  def displayAndEvolve(grid: Grid, iteration: Int = 0)(implicit interval: Int): IO[Unit] =
    for {
      r <- Ref.of[IO, Grid](grid) // define State Ref for grid
      _ <- Output.displayAndSleep(grid, iteration)
      g <- nextGridState(r)
      _ <- if (g.livingCells == grid.livingCells) IO.unit else displayAndEvolve(g, iteration + 1)
    } yield ()

}
