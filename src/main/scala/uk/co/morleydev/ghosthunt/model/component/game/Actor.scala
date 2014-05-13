package uk.co.morleydev.ghosthunt.model.component.game

import org.jsfml.system.Vector2f

object ActorDetails {
  val width = 32.0f
  val height = 32.0f
  val speed = 100.0f

}

case class Actor(position : Vector2f,
                 direction : Vector2f = new Vector2f(0.0f, 0.0f))
