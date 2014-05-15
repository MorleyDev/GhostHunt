package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.{Event, sys, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.model.net
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor}
import org.jsfml.system.Vector2f

class LocalActorController(entities : EntityComponentStore, client : Client) extends Controller(events =
  Seq(sys.MoveLocalUp.name,
      sys.MoveLocalDown.name,
      sys.MoveLocalLeft.name,
      sys.MoveLocalRight.name,
      game.EnableLocalActors.name,
      game.DisableLocalActors.name)) {

  private var isEnabled = false

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    if (!isEnabled)
      return

    event.name match {
      case game.EnableLocalActors.name => isEnabled = true
      case game.DisableLocalActors.name => isEnabled = false

      case sys.MoveLocalUp.name =>
        moveInDirection(0, new Vector2f(0.0f, -ActorDetails.speed), gameTime)

      case sys.MoveLocalDown.name =>
        moveInDirection(1, new Vector2f(0.0f, ActorDetails.speed), gameTime)

      case sys.MoveLocalLeft.name =>
        moveInDirection(2, new Vector2f(-ActorDetails.speed, 0.0f), gameTime)

      case sys.MoveLocalRight.name =>
        moveInDirection(3, new Vector2f(ActorDetails.speed, 0.0f), gameTime)
    }
  }

  private def moveInDirection(dirNo : Int, dir: Vector2f, gameTime : GameTime) {

    def moveActorInDirection(dir : Vector2f, actor : Actor) : Actor =
        actor.copy(direction = dir)

    entities.get("Actor", "Local")
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .map(ec => {
        client.send(net.game.MoveRemoteActorOnServer((dirNo, ec._2.position.x, ec._2.position.y), gameTime))
        (ec._1, moveActorInDirection(dir, ec._2))
      })
      .foreach(ec => entities.link(ec._1, "Actor", ec._2))
  }
}
