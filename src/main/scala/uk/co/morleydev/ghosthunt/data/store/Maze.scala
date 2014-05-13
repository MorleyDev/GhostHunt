package uk.co.morleydev.ghosthunt.data.store

class Maze(width : Int, height : Int) {
  object Type extends Enumeration {
    type Type = Value
    val Empty = Value("Empty")
    val Wall = Value("Wall")
    val Spawn = Value("Spawn")
  }

  def getHeight : Int = width
  def getWidth : Int = height
  def get(x : Int, y : Int) : Type.Type = Type.Empty
}
