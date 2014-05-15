package uk.co.morleydev.ghosthunt.model.component.game

import org.jsfml.system.Vector2f

/**
 * Global useful values related to actors, such as the width and heigh of the sprite
 */
object ActorDetails {
  val width = 28.0f
  val height = 28.0f
  val speed = 100.0f
  val dimensions = new Vector2f(width, height)
  val halfDimensions = new Vector2f(width / 2.0f, height / 2.0f)
  val los = 96.0f
}

/**
 * The actor is an entity that has a position and direction (aka velocity), used to represent either a ghost or a hero.
 *
 * @param position
 * @param direction
 */
case class Actor(position : Vector2f,
                 direction : Vector2f = new Vector2f(0.0f, 0.0f))
