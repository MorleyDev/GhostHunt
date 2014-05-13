package uk.co.morleydev.ghosthunt.data.store

object CellType extends Enumeration {
  val Empty = Value("Empty")
  val Wall = Value("Wall")
  val Spawn = Value("Spawn")
}

class Maze {
  private final val width = 20
  private final val height = 15

  private val maze = Seq[Int](
    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    1,0,1,1,1,0,1,1,1,1,0,1,1,1,1,0,1,1,0,1,
    1,0,1,0,0,0,0,0,1,1,0,1,1,1,1,0,0,1,1,1,
    1,0,1,0,1,1,1,0,1,1,0,1,1,1,1,1,0,0,0,1,
    1,0,1,0,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    1,0,1,0,1,0,1,1,1,1,2,1,1,1,0,1,1,1,0,1,
    1,0,1,0,1,0,1,1,1,0,2,0,1,1,0,0,0,1,0,1,
    1,0,1,0,1,0,1,1,1,1,2,1,1,1,0,1,0,1,0,1,
    1,0,1,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,1,
    1,0,0,0,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,
    1,0,1,0,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,
    1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
  )

  def getHeight : Int = height
  def getWidth : Int = width

  def get(x : Int, y : Int) : CellType.Value = maze(x + y * width) match {
    case 0 => CellType.Empty
    case 1 => CellType.Wall
    case 2 => CellType.Spawn
  }
}
