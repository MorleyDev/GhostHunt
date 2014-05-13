package uk.co.morleydev.ghosthunt.data.store

object CellType extends Enumeration {
  val Empty = Value("Empty")
  val Wall = Value("Wall")
}

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
}
