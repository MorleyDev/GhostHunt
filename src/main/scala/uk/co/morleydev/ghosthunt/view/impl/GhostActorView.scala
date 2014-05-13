package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import org.jsfml.graphics.{IntRect, Sprite, RenderTarget}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor, Ghost}

class GhostActorView(entities : EntityComponentStore, content : ContentFactory) extends View(height = 1) {

  val spritesheet = {
    val texture = content.loadTexture("resource/spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  val ghostSprite = Map[Int, Sprite](0 -> new Sprite(spritesheet, new IntRect(39, 99, 27, 27)),
    1 -> new Sprite(spritesheet, new IntRect(71, 99, 27, 27)),
    2 -> new Sprite(spritesheet, new IntRect(100, 100, 27, 27)))

  override def draw(drawable: RenderTarget): Unit = {
    entities.get("Ghost", "Actor")
      .map(ec => (ec._1, ec._2("Ghost").asInstanceOf[Ghost], ec._2("Actor").asInstanceOf[Actor]))
      .foreach(ec => {
      val sprite = ghostSprite(ec._2.id)
      sprite.setPosition(ec._3.position)
      sprite.setOrigin(ActorDetails.width / 2.0f, ActorDetails.height / 2.0f)
      sprite.setScale(ActorDetails.width / 32.0f, ActorDetails.height / 32.0f)

      if (ec._3.direction.x < 0.0f) {
        sprite.setScale(1.0f, 1.0f)
        sprite.setRotation(180.0f)
      } else if (ec._3.direction.y < 0.0f) {
        sprite.setRotation(270.0f)
        sprite.setScale(1.0f, 1.0f)
      } else if (ec._3.direction.y > 0.0f) {
        sprite.setRotation(90.0f)
        sprite.setScale(1.0f, 1.0f)
      } else {
        sprite.setScale(1.0f, 1.0f)
        sprite.setRotation(0.0f)
      }

      drawable.draw(sprite)
    })
  }
}
