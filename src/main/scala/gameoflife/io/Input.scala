package gameoflife.io

import cats.effect.IO

object Input {

  private def linesToGrid(lines: Seq[String]): IO[Seq[Seq[String]]] =
    IO.pure(lines.filter(_.nonEmpty).map(_.trim.split("").toSeq))

  def gridFromFile(file: String): IO[Seq[Seq[String]]] =
    for {
      lines <- IO { scala.io.Source.fromFile(file).getLines.toSeq }
      grid <- linesToGrid(lines)
    } yield grid

}
