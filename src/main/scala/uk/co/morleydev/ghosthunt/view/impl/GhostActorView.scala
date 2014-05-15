package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import org.jsfml.graphics.{IntRect, Sprite, RenderTarget}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor, Ghost}

class GhostActorView(entities : EntityComponentStore, content : ContentFactory) extends View(height = 1) {

  val spritesheet = {
    val texture = content.loadTexture("spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  val ghostRightSprite = Map[Int, Sprite](0 -> new Sprite(spritesheet, new IntRect(39, 99, 28, 28)),
    1 -> new Sprite(spritesheet, new IntRect(67, 99, 28, 28)),
    2 -> new Sprite(spritesheet, new IntRect(95, 99, 28, 28)))
  val ghostLeftSprite = Map[Int, Sprite](0 -> new Sprite(spritesheet, new IntRect(39, 127, 28, 28)),
    1 -> new Sprite(spritesheet, new IntRect(67, 127, 28, 28)),
    2 -> new Sprite(spritesheet, new IntRect(95, 127, 28, 28)))
  val ghostUpSprite = Map[Int, Sprite](0 -> new Sprite(spritesheet, new IntRect(39, 155, 28, 28)),
    1 -> new Sprite(spritesheet, new IntRect(67, 155, 28, 28)),
    2 -> new Sprite(spritesheet, new IntRect(95, 155, 28, 28)))
  val ghostDownSprite = Map[Int, Sprite](0 -> new Sprite(spritesheet, new IntRect(39, 183, 28, 28)),
    1 -> new Sprite(spritesheet, new IntRect(67, 183, 28, 28)),
    2 -> new Sprite(spritesheet, new IntRect(95, 183, 28, 28)))

  override def draw(drawable: RenderTarget): Unit = {

    entities.get("Ghost", "Actor")
      .map(ec => (ec._1, ec._2("Ghost").asInstanceOf[Ghost], ec._2("Actor").asInstanceOf[Actor]))
      .toList
      .sortBy(_._2.id)
      .foreach(ec => {
      val sprite = if (ec._3.direction.x < 0.0f) {
        ghostLeftSprite(ec._2.id)
      } else if (ec._3.direction.y < 0.0f) {
        ghostUpSprite(ec._2.id)
      } else if (ec._3.direction.y > 0.0f) {
        ghostDownSprite(ec._2.id)
      } else {
        ghostRightSprite(ec._2.id)
      }

      sprite.setPosition(ec._3.position)
      sprite.setOrigin(ActorDetails.halfDimensions)

      drawable.draw(sprite)
    })
  }
}
