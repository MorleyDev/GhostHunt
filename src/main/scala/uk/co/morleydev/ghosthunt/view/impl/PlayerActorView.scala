package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import org.jsfml.graphics.{RenderTarget, IntRect, Sprite}
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor, Ghost}
import org.jsfml.system.Vector2f

/**
 * The player actor view is responsible for drawing the hero with the correct sprite.
 * It is also responsible for not drawing the hero if they do not exist, or if the local
 * player is a ghost and cannot currently see the hero due to the hero being outside of
 * their line of sight.
 *
 * @param entities
 * @param content
 */
class PlayerActorView(entities : EntityComponentStore, content : ContentFactory) extends View(height = 1) {

  val spritesheet = {
    val texture = content.loadTexture("spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  val playerRightSprite = new Sprite(spritesheet, new IntRect(6, 95, 28, 28))
  val playerLeftSprite = new Sprite(spritesheet, new IntRect(6, 123, 28, 28))
  val playerUpSprite = new Sprite(spritesheet, new IntRect(6, 151, 28, 28))
  val playerDownSprite = new Sprite(spritesheet, new IntRect(6, 179, 28, 28))

  private def isWithinGhostsSight(pos: Vector2f): Boolean = {
    entities.get("Ghost", "Actor")
      .map(ec => ec._2("Actor").asInstanceOf[Actor].position)
      .exists(actor => {
        val xdist = actor.x - pos.x
        val ydist = actor.y - pos.y
        val mag = xdist * xdist + ydist * ydist
        mag <= (ActorDetails.los * ActorDetails.los)
      })
  }

  override def draw(drawable: RenderTarget): Unit = {
    val isUnlimitedView = entities.get("Ghost", "Local").size == 0

    entities.get("Player", "Actor")
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .filter(ec => isUnlimitedView || isWithinGhostsSight(ec._2.position))
      .foreach(ec => {

      val sprite = if (ec._2.direction.x < 0.0f) {
        playerLeftSprite
      } else if (ec._2.direction.y < 0.0f) {
        playerUpSprite
      } else if (ec._2.direction.y > 0.0f) {
        playerDownSprite
      } else {
        playerRightSprite
      }
      sprite.setPosition(ec._2.position)
      sprite.setOrigin(ActorDetails.halfDimensions)
      drawable.draw(sprite)
    })
  }
}
