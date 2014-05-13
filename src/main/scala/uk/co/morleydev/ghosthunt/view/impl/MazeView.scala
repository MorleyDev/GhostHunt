package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.{CellType, Maze}
import org.jsfml.graphics.{IntRect, Sprite, RenderTarget}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.Vector2f

class MazeView(maze : Maze,
               content : ContentFactory) extends View(height = 0) {

  val spritesheet = {
    val texture = content.loadTexture("resource/spritesheet.png")
    if (texture.isEmpty)
      throw new FileNotFoundException()
    texture.get
  }
  val sprite = new Sprite(spritesheet, new IntRect(0,0,32,32))

  override def draw(drawable: RenderTarget): Unit = {
    (0 to (maze.getWidth-1)).foreach({ x =>
      (0 to (maze.getHeight-1)).foreach({y =>

        maze.get(x, y) match {
          case CellType.Empty =>

          case CellType.Spawn =>

          case CellType.Wall =>
            sprite.setPosition(Vector2f(x * 32.0f, y * 32.0f))
            drawable.draw(sprite)
        }
      })
    })
  }
}
