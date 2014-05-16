package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.{CellType, Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.component.game.Actor
import org.jsfml.system.Vector2f
import scala.concurrent.duration.Duration

object UpdateActorPosition {
  def apply(maze : Maze, position : Vector2f, direction : Vector2f, dt: Duration): Vector2f = {

    val nextPosition = Vector2f.add(position, Vector2f.mul(direction, dt.toMicros / 1000000.0f))

    val nextActorTileX = (nextPosition.x / 32.0f).toInt
    val nextActorTileY = (nextPosition.y / 32.0f).toInt
    val resolvedPosition =
      if (maze.get(nextActorTileX, nextActorTileY) == CellType.Empty)
        nextPosition
      else
        position

    if (resolvedPosition.x < 0.0f)
      new Vector2f(640.0f, resolvedPosition.y)
    else if (resolvedPosition.x > 640.0f)
      new Vector2f(0.0f, resolvedPosition.y)
    else if (resolvedPosition.y < 0.0f)
      new Vector2f(resolvedPosition.x, 480.0f)
    else if (resolvedPosition.y > 480.0f)
      new Vector2f(resolvedPosition.x, 0.0f)
    else
      resolvedPosition
  }
}

/**
 * The actor physics controller runs the physics logic for each frame of gameplay, moving actors to their new positions
 * and resolving collisions with walls as needed.
 *
 * @param entities
 * @param maze
 */
class ActorPhysicsController(entities : EntityComponentStore, maze : Maze) extends Controller {

  override def update(gameTime: GameTime): Unit = {
    entities.get("Actor")
            .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
            .map(ec => (ec._1, ec._2.copy(position = UpdateActorPosition(maze, ec._2.position, ec._2.direction, gameTime.deltaTime))))
            .foreach(ec => entities.link(ec._1, "Actor", ec._2))
  }
}
