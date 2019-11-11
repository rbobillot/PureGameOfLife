package gameoflife.io

import cats.effect.IO

import scala.io.Source

object Input {

  private def linesToGrid(lines: Seq[String]): IO[Seq[Seq[String]]] =
    IO.pure(lines.filter(_.nonEmpty).map(_.trim.split("").toSeq))

  def gridFromFile(file: String): IO[Seq[Seq[String]]] =
    for {
      src <- IO.apply(Source fromFile file)
      lns <- IO.apply(src.getLines.toSeq)
      grd <- linesToGrid(lns)
      _ <- IO.apply(src.close)
    } yield grd

}
