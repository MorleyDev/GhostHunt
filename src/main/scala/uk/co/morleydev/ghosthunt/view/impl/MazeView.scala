package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.{CellType, Maze}
import org.jsfml.graphics.{IntRect, Sprite, RenderTarget}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import org.jsfml.system.Vector2f

class MazeView(maze : Maze,
               content : ContentFactory) extends View(height = 0) {

  val spritesheet = {
    val texture = content.loadTexture("resource/spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  // (up down left right)
  val sprites = Map((false,true,false,true) -> new Sprite(spritesheet, new IntRect(0,0,32,32)),
                    (false, false, true, true) -> new Sprite(spritesheet, new IntRect(32,0,32,32)),
                    (false, true, true, false) -> new Sprite(spritesheet, new IntRect(64,0,32,32)),
                    (true, true, false, false) -> new Sprite(spritesheet, new IntRect(32,32,32,32)),
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

  def getWallSprite(x : Int, y : Int) : Sprite = {
    val hasUp = maze.get(x, y-1) == CellType.Wall
    val hasDown = maze.get(x, y+1) == CellType.Wall
    val hasLeft = maze.get(x-1, y) == CellType.Wall
    val hasRight = maze.get(x+1, y) == CellType.Wall

    sprites(hasUp, hasDown, hasLeft, hasRight)
  }

  override def draw(drawable: RenderTarget): Unit = {
    (0 to (maze.getWidth-1)).foreach({ x =>
      (0 to (maze.getHeight-1)).foreach({y =>

        maze.get(x, y) match {
          case CellType.Empty =>

          case CellType.Wall =>
            val sprite = getWallSprite(x,y)
            sprite.setPosition(new Vector2f(x * 32.0f, y * 32.0f))
            drawable.draw(sprite)
        }
      })
    })
  }
}
