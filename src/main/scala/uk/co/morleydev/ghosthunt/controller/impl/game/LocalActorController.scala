package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.{Event, sys}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor}
import org.jsfml.system.Vector2f

class LocalActorController(entities : EntityComponentStore) extends Controller(events =
  Seq(sys.MoveLocalUp.name,
      sys.MoveLocalDown.name,
      sys.MoveLocalLeft.name,
      sys.MoveLocalRight.name)) {
  
  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    event.name match {
      case sys.MoveLocalUp.name =>
        moveInDirection(new Vector2f(0.0f, -ActorDetails.speed))

      case sys.MoveLocalDown.name =>
        moveInDirection(new Vector2f(0.0f, ActorDetails.speed))

      case sys.MoveLocalLeft.name =>
        moveInDirection(new Vector2f(-ActorDetails.speed, 0.0f))

      case sys.MoveLocalRight.name =>
        moveInDirection(new Vector2f(ActorDetails.speed, 0.0f))
    }
  }

  private def moveInDirection(dir: Vector2f) {

    def moveActorInDirection(dir : Vector2f, actor : Actor) : Actor =
        actor.copy(direction = dir)

    entities.get("Actor", "Local")
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .map(ec => (ec._1, moveActorInDirection(dir, ec._2)))
      .foreach(ec => entities.link(ec._1, "Actor", ec._2))
  }
}
