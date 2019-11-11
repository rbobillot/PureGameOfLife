package testoflife.game

import cats.effect.IO
import cats.effect.concurrent.Ref

import org.scalatest.{ Matchers, PrivateMethodTester, WordSpec }

import gameoflife.game.grid.{ Cell, Grid }
import gameoflife.game.Life
import testoflife.Shapes

class LifeSpec extends WordSpec with Matchers with Shapes with PrivateMethodTester {

  private val deadCell0 = Cell(0, 0) // 0 neighbour
  private val deadCell1 = Cell(1, 3) // 4 neighbours
  private val deadCell2 = Cell(3, 2) // 3 neighbours
  private val deadCell3 = Cell(3, 4) // 2 neighbours

  private val livingCell0 = Cell(1, 2) // 2 neighbours
  private val livingCell1 = Cell(2, 2) // 2 neighbours
  private val livingCell2 = Cell(2, 3) // 3 neighbours
  private val livingCell3 = Cell(2, 4) // 1 neighbour
  private val livingCell4 = Cell(4, 2) // 1 neighbour

  private val livingCells = Set(livingCell0, livingCell1, livingCell2, livingCell3, livingCell4)

  private val emptyGrid = Grid(5, 5, Set())
  private val nonEmptyGrid = Grid(5, 5, livingCells)

  /* ****************** PRIVATE METHODS TO TEST ******************* */

  private val nextGridState = PrivateMethod[IO[Grid]](Symbol("nextGridState"))

  /* ************************ DEAD CELLS ************************** */

  "A dead cell" should {
    "not have neighbours" when {
      "the grid is empty" in {
        deadCell0.neighbours.count(emptyGrid.livingCells) should be(0)
      }
      "the grid is not empty, and the cell is alone" in {
        deadCell0.neighbours.count(emptyGrid.livingCells) should be(0)
      }
    }
    "have a valid amount of neighbours" when {
      "the grid is not empty, and the cell is surrounded by living cells" in {
        deadCell2.neighbours.count(nonEmptyGrid.livingCells) should be(3)
      }
    }
  }

  it must {
    "STAY DEAD" when {
      "it is not surrounded by 3 living cells" in {
        deadCell0.updateCell(livingCells) should be(None)
        deadCell1.updateCell(livingCells) should be(None)
        deadCell3.updateCell(livingCells) should be(None)
      }
    }
    "LIVE" when {
      "it is surrounded by 3 living cells" in {
        deadCell2.updateCell(livingCells) should not be None
      }
    }
  }

  /* ************************ LIVING CELLS ************************** */

  "A living cell" should {
    "not have neighbours" when {
      "the grid is empty" in {
        livingCell1.neighbours.count(emptyGrid.livingCells) should be(0)
      }
      "the grid is not empty, and the cell is alone" in {
        livingCell4.neighbours.count(emptyGrid.livingCells) should be(0)
      }
    }
    "have a valid amount of neighbours" when {
      "the grid is not empty, and the cell is surrounded by living cells" in {
        livingCell0.neighbours.count(nonEmptyGrid.livingCells) should be(2)
      }
    }
  }

  it must {
    "DIE" when {
      "it is not surrounded by 2 or 3 living cells" in {
        livingCell3.updateCell(livingCells) should be(None)
        livingCell4.updateCell(livingCells) should be(None)
      }
    }
    "STAY ALIVE" when {
      "it is surrounded by 2 or 3 living cells" in {
        livingCell0.updateCell(livingCells) should not be None
        livingCell1.updateCell(livingCells) should not be None
        livingCell2.updateCell(livingCells) should not be None
      }
    }
  }

  /* ************************ GENERAL STATE ************************** */

  "Every cells" must {
    "be properly updated" when {
      "the grid evolves, according to the live/die rules" in {
        /*
              . . . . . .          . . . . . .
              . . . . . .          . . . . . .
              . o o . o .          . o o o . .
              . . o . . .    ->    . . o . . .
              . . o . . .          . . . . . .
              . . . . . .          . . . . . .
         */
        val currentStateRef = Ref.of[IO, Grid](nonEmptyGrid)
        val nextState = nonEmptyGrid.copy(livingCells = Set(livingCell0, livingCell1, livingCell2, Cell(3, 2)))

        currentStateRef.flatMap(r => Life.invokePrivate(nextGridState(r))).unsafeRunSync shouldEqual nextState
      }
    }
  }

}
