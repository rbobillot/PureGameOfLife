package gameoflife.game.grid

case class Cell(x: Int, y: Int) {
  def neighbours: Seq[Cell] =
    for {
      x <- this.x - 1 to this.x + 1
      y <- this.y - 1 to this.y + 1 if !(x == this.x && y == this.y)
    } yield Cell(x, y)

  def updateCell(livingCells: Set[Cell]): Option[Cell] =
    this.neighbours.count(livingCells) match {
      case 3 | 2 if livingCells.contains(this) => Some(this)
      case 3 if !livingCells.contains(this)    => Some(this)
      case _                                   => None
    }
}
