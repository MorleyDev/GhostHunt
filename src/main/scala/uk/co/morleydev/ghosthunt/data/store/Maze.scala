package uk.co.morleydev.ghosthunt.data.store

import java.util.concurrent.ConcurrentHashMap

/**
 * Enum representing whether a cell in the maze is a wall or not
 */
object CellType extends Enumeration {
  val Empty = Value("Empty")
  val Wall = Value("Wall")
}

/**
 * The maze stores the cells in the game, and whether those cells are passable or not
 * It also stores information about what pellets are available in the game, through the PelletStore
 */
class Maze {
  private final val width = 20
  private final val height = 15

  private val maze = Seq[Int](
    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    1,0,1,1,1,0,1,1,1,1,0,1,1,1,1,0,1,1,0,1,
    1,0,1,0,0,0,0,0,1,1,0,1,1,1,1,0,1,1,0,1,
    1,0,1,0,1,1,1,0,1,1,0,1,1,1,1,0,0,0,0,1,
    1,0,1,0,1,1,1,0,1,1,0,1,1,1,1,0,1,1,0,1,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    1,0,1,0,1,0,1,1,1,1,0,1,1,1,0,1,1,1,0,1,
    1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,
    1,0,1,0,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,
    1,0,1,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,1,
    1,0,0,0,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,
    1,0,1,0,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,
    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
  )

  def getHeight : Int = height
  def getWidth : Int = width

  def get(x : Int, y : Int) : CellType.Value = if ( x < 0 || x >= width || y < 0 || y >= height )
    CellType.Empty
  else maze(x + y * width) match {
    case 0 => CellType.Empty
    case 1 => CellType.Wall
    case _ => CellType.Empty
  }

  class PelletStore(maze : Maze) {
    private val pellets = new ConcurrentHashMap[(Int, Int), Boolean]()
    private var pelletCount = 0
    private var totalPelletCount = 0
    reset()

    def reset() = {
      pellets.clear()
      pelletCount = 0
      totalPelletCount = 0
      (0 to maze.getWidth-1).map(x => {
        (0 to maze.getHeight-1).map(y => {
          val containsPellet = maze.get(x, y) == CellType.Empty
          pellets.put((x,y), containsPellet)
          if (containsPellet)
            pelletCount = pelletCount + 1
        })
      })
      totalPelletCount = pelletCount
    }

    def get(x : Int, y : Int) : Boolean = pellets.get((x,y))

    def remove(x : Int, y : Int) : Unit = if ( pellets.put((x,y), false) ) pelletCount = pelletCount - 1

    def countPellets = pelletCount

    def totalPellets = totalPelletCount
  }
  var pellets = new PelletStore(this)

}
