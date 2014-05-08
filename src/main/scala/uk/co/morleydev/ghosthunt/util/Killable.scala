package uk.co.morleydev.ghosthunt.util

trait Killable {

  private var alive = true

  def kill() : Unit =
    alive = false

  def isAlive : Boolean =
    alive
}
