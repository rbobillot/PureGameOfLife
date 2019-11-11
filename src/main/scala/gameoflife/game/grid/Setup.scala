package gameoflife.game.grid

import cats.effect.IO

import gameoflife.io.Config.cellInputColors

trait Setup {

  private val isFullInputCell: Set[String] = Set(cellInputColors.full)
  private val isEmptyInputCell: Set[String] = Set(cellInputColors.empty)
  private val isValidInputCell: Set[String] = isFullInputCell ++ isEmptyInputCell

  private def isValidRowsContent(rows: Seq[Seq[String]]): Boolean =
    rows.forall(_.forall(isValidInputCell))

  private def isValidRowsLength(rows: Seq[Seq[String]]): Boolean =
    rows.forall(_.lengthCompare(rows.head.size) == 0)

  /**
    * Produces a Boolean Cells Matrix
    * Each cell is:
    *   - false, if the input cell is empty (dead)
    *   - true, if the input cell is full (alive)
    */
  private def validateGrid(rows: Seq[Seq[String]]): IO[Seq[Seq[Boolean]]] =
    for {
      l <- IO.pure(isValidRowsLength(rows))
      b <- IO.pure(isValidRowsContent(rows))
      grid <- (l, b) match {
        case (false, _) => IO.raiseError(new Exception(s"Input grid contains non equal rows sizes"))
        case (_, false) => IO.raiseError(new Exception(s"Input grid contains an invalid character"))
        case _          => IO.pure(rows.map(_.map(isFullInputCell)))
      }
    } yield grid

  protected def linesToGridCells(lines: Seq[Seq[Boolean]]): Seq[Cell] =
    for {
      y <- lines.indices
      x <- lines.head.indices if lines(y)(x)
    } yield Cell(x, y)

  protected def initGrid(file: String): IO[Grid] =
    for {
      rows <- gameoflife.io.Input.gridFromFile(file)
      grid <- validateGrid(rows)
      cols <- IO.pure(grid.head.size)
      rows <- IO.pure(grid.size)
      livingCells <- IO.pure(linesToGridCells(grid).toSet)
    } yield Grid(cols, rows, livingCells)

}
