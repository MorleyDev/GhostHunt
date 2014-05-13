package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import org.jsfml.graphics.{RenderTarget, IntRect, Sprite}
import uk.co.morleydev.ghosthunt.model.component.game.{Actor, Ghost}

class PlayerActorView(entities : EntityComponentStore, content : ContentFactory) extends View(height = 1) {

  val spritesheet = {
    val texture = content.loadTexture("resource/spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  val playerSprite = new Sprite(spritesheet, new IntRect(6, 95, 28, 28))

  override def draw(drawable: RenderTarget): Unit = {
    entities.get("Player", "Actor")
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .foreach(ec => {

      playerSprite.setPosition(ec._2.position)
      playerSprite.setOrigin(16.0f, 16.0f)

      if (ec._2.direction.x < 0.0f) {
        playerSprite.setScale(1.0f, 1.0f)
        playerSprite.setRotation(180.0f)
      } else if (ec._2.direction.y < 0.0f) {
        playerSprite.setRotation(270.0f)
        playerSprite.setScale(1.0f, 1.0f)
      } else if (ec._2.direction.y > 0.0f) {
        playerSprite.setRotation(90.0f)
        playerSprite.setScale(1.0f, 1.0f)
      } else {
        playerSprite.setScale(1.0f, 1.0f)
        playerSprite.setRotation(0.0f)
      }

      drawable.draw(playerSprite)
    })
  }
}
