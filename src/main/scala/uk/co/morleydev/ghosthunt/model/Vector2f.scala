package uk.co.morleydev.ghosthunt.model

import org.jsfml.system.Vector2f

object Vector2f {
  def apply(x : Float, y : Float) = new Vector2f(x,y)

  def sum(a : Vector2f, b : Vector2f) = new Vector2f(a.x + b.x, a.y + b.y)
  def sub(a : Vector2f, b : Vector2f) = new Vector2f(a.x - b.x, a.y - b.y)
  def mult(a : Vector2f, b : Float) = new Vector2f(a.x * b, a.y * b)
  def div(a : Vector2f, b : Float) = new Vector2f(a.x / b, a.y / b)
}
