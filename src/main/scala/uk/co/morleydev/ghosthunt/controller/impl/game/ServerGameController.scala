package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.net
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.net.Server
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId}
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor, Remote}
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import org.jsfml.graphics.FloatRect
import org.jsfml.system.Vector2f

class ServerGameController(entities : EntityComponentStore, events : EventQueue, server : Server) extends Controller(messages = Seq(net.game.Disconnected.name)) {

  override def update(gameTime: GameTime): Unit = {

    val isCollision = entities.get("Player", "Actor")
      .map(s => (s._1, s._2("Actor").asInstanceOf[Actor]))
      .map(s => new FloatRect(Vector2f.sub(s._2.position, ActorDetails.halfDimensions), ActorDetails.dimensions))
      .exists(player => {
        entities.get("Ghost", "Actor")
          .map(s => (s._1, s._2("Actor").asInstanceOf[Actor]))
          .map(s => new FloatRect(Vector2f.sub(s._2.position, ActorDetails.halfDimensions), ActorDetails.dimensions))
          .exists(ghost => ghost.intersection(player) != null)
      })

    if (isCollision) {
      entities.get("Remote")
        .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
        .foreach(s => { entities.removeEntity(s._1); server.send(s._2, net.game.GameOver(false, gameTime)); })

      events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, server, events)))
      kill()
    }
  }

  protected override def onServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = {
    message.name match {
      case net.game.Disconnected.name =>
        entities.get("Remote")
          .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
          .filter(s => s._2 != client)
          .foreach(s => { entities.removeEntity(s._1); server.send(s._2, net.game.ReturnToLobby(gameTime)); })
        events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, server, events)))
        kill()
    }
  }
}
