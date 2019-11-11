package gameoflife.io

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object Config {

  case class CellInputColor(empty: String, full: String)

  case class CellOutputColor(dead: String, alive: String)

  case class RefreshInterval(defaultInterval: Int)

  case class OutputMethod(gui: Boolean)

  lazy val cellInputColors: CellInputColor =
    ConfigSource.default
      .load[CellInputColor]
      .getOrElse(CellInputColor(empty = ".", full = "o"))

  lazy val cellOutputColors: CellOutputColor =
    ConfigSource.default
      .load[CellOutputColor]
      .getOrElse(CellOutputColor(dead  = " ", alive = "o"))

  lazy val refreshInterval: RefreshInterval =
    ConfigSource.default
      .load[RefreshInterval]
      .getOrElse(RefreshInterval(defaultInterval = 100))

  lazy val outputMethod: OutputMethod =
    ConfigSource.default
      .load[OutputMethod]
      .getOrElse(OutputMethod(gui = false))

}
