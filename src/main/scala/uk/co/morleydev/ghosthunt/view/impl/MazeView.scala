package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.{EntityComponentStore, CellType, Maze}
import org.jsfml.graphics.{IntRect, Sprite, RenderTarget}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor}

class MazeView(maze : Maze,
               entities : EntityComponentStore,
               content : ContentFactory) extends View(height = 0) {

  val spritesheet = {
    val texture = content.loadTexture("resource/spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  // (up down left right)
  val sprites = Map((false, true, false, true) -> new Sprite(spritesheet, new IntRect(0, 0, 32, 32)),
    (false, false, true, true) -> new Sprite(spritesheet, new IntRect(32, 0, 32, 32)),
    (false, true, true, false) -> new Sprite(spritesheet, new IntRect(64, 0, 32, 32)),
    (true, true, false, false) -> new Sprite(spritesheet, new IntRect(32, 32, 32, 32)),
    (true, true, true, false) -> new Sprite(spritesheet, new IntRect(96, 0, 32, 32)),
    (true, true, true, true) -> new Sprite(spritesheet, new IntRect(128, 0, 32, 32)),
    (false, true, true, true) -> new Sprite(spritesheet, new IntRect(128, 32, 32, 32)),
    (true, false, true, true) -> new Sprite(spritesheet, new IntRect(160, 0, 32, 32)),
    (true, true, false, true) -> new Sprite(spritesheet, new IntRect(96, 32, 32, 32)),
    (false, false, false, false) -> new Sprite(spritesheet, new IntRect(160, 32, 32, 32)),
    (true, false, false, false) -> new Sprite(spritesheet, new IntRect(192, 0, 32, 32)),
    (false, true, false, false) -> new Sprite(spritesheet, new IntRect(192, 32, 32, 32)),
    (false, false, true, false) -> new Sprite(spritesheet, new IntRect(224, 0, 32, 32)),
    (false, false, false, true) -> new Sprite(spritesheet, new IntRect(224, 32, 32, 32)),
    (false, false, true, false) -> new Sprite(spritesheet, new IntRect(224, 0, 32, 32)),
    (false, true, false, true) -> new Sprite(spritesheet, new IntRect(192, 64, 32, 32)),
    (false, true, true, false) -> new Sprite(spritesheet, new IntRect(224, 64, 32, 32)),
    (true, false, false, true) -> new Sprite(spritesheet, new IntRect(64, 32, 32, 32)),
    (true, false, true, false) -> new Sprite(spritesheet, new IntRect(0, 32, 32, 32))
  )

  private def distanceFromAViewLessThan(x: Int, y: Int, view: Actor, dist: Float): Boolean = {
    val xDist = x * 32.0f - view.position.x
    val yDist = y * 32.0f - view.position.y
    val mag = xDist * xDist + yDist * yDist
    val distMag = dist * dist
    Math.abs(mag) <= distMag
  }

  def getWallSprite(x: Int, y: Int): Sprite = {
    val hasUp = maze.get(x, y - 1) == CellType.Wall
    val hasDown = maze.get(x, y + 1) == CellType.Wall
    val hasLeft = maze.get(x - 1, y) == CellType.Wall
    val hasRight = maze.get(x + 1, y) == CellType.Wall

    sprites(hasUp, hasDown, hasLeft, hasRight)
  }

  override def draw(drawable: RenderTarget): Unit = {
    val isUnlimitedView = entities.get("Ghost", "Local").size == 0
    lazy val views = entities.get("Actor", "Ghost").map(s => (s._1, s._2("Actor").asInstanceOf[Actor]))

    (0 to (maze.getWidth - 1)).foreach({
      x =>
        (0 to (maze.getHeight - 1)).foreach({
          y =>
            lazy val cellInRangeOfLocal = views.exists(v => distanceFromAViewLessThan(x, y, v._2, ActorDetails.los))

            maze.get(x, y) match {
              case CellType.Empty =>

              case CellType.Wall =>
                if (isUnlimitedView || cellInRangeOfLocal) {
                  val sprite = getWallSprite(x, y)
                  sprite.setPosition(new Vector2f(x * 32.0f, y * 32.0f))
                  drawable.draw(sprite)
                }
            }
        })
    })
  }
}
