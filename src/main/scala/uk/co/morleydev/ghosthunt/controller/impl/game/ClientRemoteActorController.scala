package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{game, NetworkMessage, ClientId}
import uk.co.morleydev.ghosthunt.model.GameTime
import scala.concurrent.duration
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.model.component.game.{Actor, Remote, ActorDetails}
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import java.util.UUID

class ClientRemoteActorController(entities: EntityComponentStore) extends Controller(messages = Seq(game.MoveRemoteActorOnClient.name)) {

  protected override def onClientMessage(message : NetworkMessage, gameTime : GameTime) = {
    // 0 = Up, 1 = Down, 2 = Left, 3 = Right
    val clientDirection = game.MoveRemoteActorOnClient.extract(message)
    val latency : Float = (1.0f + (gameTime.totalTime - message.time).toUnit(duration.SECONDS)).toFloat

    val client = new ClientId(UUID.fromString(clientDirection._1))
    val direction = clientDirection._2
    val position = new Vector2f(clientDirection._3, clientDirection._4)
    direction match {
      case 0 =>
        moveInDirection(client, new Vector2f(0.0f, -ActorDetails.speed * latency), position)

      case 1 =>
        moveInDirection(client, new Vector2f(0.0f, ActorDetails.speed * latency), position)

      case 2 =>
        moveInDirection(client, new Vector2f(-ActorDetails.speed * latency, 0.0f), position)

      case 3 =>
        moveInDirection(client, new Vector2f(ActorDetails.speed * latency, 0.0f), position)
    }
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