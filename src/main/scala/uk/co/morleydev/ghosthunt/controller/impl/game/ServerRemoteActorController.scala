package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Remote, Actor}
import org.jsfml.system.Vector2f
import scala.concurrent.duration
import uk.co.morleydev.ghosthunt.data.net.Server

class ServerRemoteActorController(entities : EntityComponentStore, server : Server) extends Controller(messages = Seq(game.MoveRemoteActorOnServer.name)) {

  protected override def onServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = {
    // 0 = Up, 1 = Down, 2 = Left, 3 = Right
    val direction = game.MoveRemoteActorOnServer.extract(message)
    val latency : Float = (1.0f + (gameTime.totalTime - message.time).toUnit(duration.SECONDS)).toFloat

    direction._1 match {
      case 0 =>
        moveInDirection(client, new Vector2f(0.0f, -ActorDetails.speed * latency), new Vector2f(direction._2, direction._3))

      case 1 =>
        moveInDirection(client, new Vector2f(0.0f, ActorDetails.speed * latency), new Vector2f(direction._2, direction._3))

      case 2 =>
        moveInDirection(client, new Vector2f(-ActorDetails.speed * latency, 0.0f), new Vector2f(direction._2, direction._3))

      case 3 =>
        moveInDirection(client, new Vector2f(ActorDetails.speed * latency, 0.0f), new Vector2f(direction._2, direction._3))
    }

    entities.get("Remote")
      .map(ec => ec._2("Remote").asInstanceOf[Remote])
      .filter(r => r.id != client)
      .foreach(r => server.send(r.id, game.MoveRemoteActorOnClient((client.value.toString, direction._1, direction._2, direction._3), gameTime)))
  }

  private def moveInDirection(client : ClientId, dir: Vector2f, pos : Vector2f) {

    def moveActorInDirection(dir : Vector2f, actor : Actor) : Actor =
      actor.copy(direction = dir)

    entities.get("Actor", "Remote")
      .filter(ec => ec._2("Remote").asInstanceOf[Remote].id == client)
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .map(ec => (ec._1, ec._2.copy(position = pos)))
      .map(ec => (ec._1, moveActorInDirection(dir, ec._2)))
      .foreach(ec => entities.link(ec._1, "Actor", ec._2))
  }
}
