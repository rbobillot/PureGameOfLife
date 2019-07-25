package gameoflife.io

import pureconfig.generic.auto._

object Config {

  case class CellInputColor(empty: String, full: String)

  case class CellOutputColor(dead: String, alive: String)

  case class RefreshInterval(defaultInterval: Int)

  lazy val cellInputColors: CellInputColor =
    pureconfig
      .loadConfig[CellInputColor]
      .getOrElse(CellInputColor(empty = ".", full = "o"))

  lazy val cellOutputColors: CellOutputColor =
    pureconfig
      .loadConfig[CellOutputColor]
      .getOrElse(CellOutputColor(dead  = " ", alive = "o"))

  lazy val refreshInterval: RefreshInterval =
    pureconfig
      .loadConfig[RefreshInterval]
      .getOrElse(RefreshInterval(defaultInterval = 100))

}
