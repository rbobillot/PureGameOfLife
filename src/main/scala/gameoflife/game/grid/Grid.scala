package gameoflife.game.grid

import gameoflife.io.Config

case class Grid(columns:     Int,
                rows:        Int,
                livingCells: Set[Cell],
                iteration:   Int       = 0) {

  // returns a tuple: (PreviousGrid, NextGrid)
  def nextState: (Grid, Grid) =
    this -> this.copy(iteration   = iteration + 1, livingCells = this.livingCells
      .flatMap(_.neighbours)
      .flatMap(_.updateCell(this.livingCells)))

  def stringify: String =
    (0 until this.rows).map { y =>
      (0 until this.columns).map { x =>
        if (this.livingCells(Cell(x, y))) Config.cellOutputColors.alive
        else Config.cellOutputColors.dead
      }.mkString(" ")
    }.mkString(s"\n  Iteration n#: $iteration\n  ", "\n  ", "\n")

}
