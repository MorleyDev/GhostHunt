package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.{CellType, Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.component.game.Actor
import org.jsfml.system.Vector2f
import scala.concurrent.duration.Duration

class ActorPhysicsController(entities : EntityComponentStore, maze : Maze) extends Controller {

  private def updatePosition(actor : Actor, dt : Duration) : Actor = {

    val nextPosition = Vector2f.add(actor.position, Vector2f.mul(actor.direction, dt.toMicros / 1000000.0f))

    val actorTileX = (actor.position.x / 32.0f).toInt
    val actorTileY = (actor.position.y / 32.0f).toInt

    val nextActorTileX = (nextPosition.x / 32.0f).toInt
    val nextActorTileY = (nextPosition.y / 32.0f).toInt
    val resolvedPosition = if (actorTileX != nextActorTileX || actorTileY != nextActorTileY) {
      if (maze.get(nextActorTileX, nextActorTileY) == CellType.Empty)
        nextPosition
      else
        actor.position
    } else
      nextPosition

    val bouncedPosition = if (resolvedPosition.x < 0.0f)
      new Vector2f(640.0f, resolvedPosition.y)
    else if (resolvedPosition.x > 640.0f)
      new Vector2f(0.0f, resolvedPosition.y)
    else if (resolvedPosition.y < 0.0f)
      new Vector2f(resolvedPosition.x, 480.0f)
    else if (resolvedPosition.y > 480.0f)
      new Vector2f(resolvedPosition.x, 0.0f)
    else
      resolvedPosition

    actor.copy(position = bouncedPosition)
  }

  override def update(gameTime: GameTime): Unit = {
    entities.get("Actor")
            .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
            .map(ec => (ec._1, updatePosition(ec._2, gameTime.deltaTime)))
            .foreach(ec => entities.link(ec._1, "Actor", ec._2))
  }
}
