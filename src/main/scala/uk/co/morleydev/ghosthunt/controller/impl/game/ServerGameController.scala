package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.{event, GameTime, net}
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.data.net.Server
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId}
import uk.co.morleydev.ghosthunt.model.component.game.{ActorDetails, Actor, Remote}
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import org.jsfml.graphics.FloatRect
import org.jsfml.system.Vector2f

/**
 * The server game controller is in charge of the running game state, namely for detecting when collisions occur between
 * hero and ghost, when pellets are removed from play, and when the game is ended with victory or defeat for the hero.
 * It also detects when players disconnect, and responds by returning all clients to the lobby.
 *
 * @param entities
 * @param events
 * @param server
 * @param maze
 */
class ServerGameController(entities : EntityComponentStore, events : EventQueue, server : Server, maze : Maze)
  extends Controller(messages = Seq(net.game.Disconnected.name)) {

  override def update(gameTime: GameTime): Unit = {

    entities.get("Player", "Actor")
      .map(ec => (ec._1, ec._2("Actor").asInstanceOf[Actor]))
      .map(ec => (ec._1, ec._2, ((ec._2.position.x / 32).toInt, (ec._2.position.y / 32).toInt)))
      .filter(ec => maze.pellets.get(ec._3._1, ec._3._2))
      .map(ec => (ec._3, new FloatRect(Vector2f.sub(ec._2.position, ActorDetails.halfDimensions), ActorDetails.dimensions)))
      .filter(ec => ec._2.intersection(new FloatRect(ec._1._1 * 32.0f + 4.0f, ec._1._2 * 32.0f + 4.0f, 28.0f, 28.0f)) != null)
      .foreach(ec =>
    {
      maze.pellets.remove(ec._1._1, ec._1._2)
      entities.get("Remote")
        .map(ec => ec._2("Remote").asInstanceOf[Remote].id)
        .foreach(i => server.send(i, net.game.RemovePellet(ec._1, gameTime)))
    })

    lazy val isCollision = entities.get("Player", "Actor")
      .map(s => (s._1, s._2("Actor").asInstanceOf[Actor]))
      .map(s => new FloatRect(Vector2f.sub(s._2.position, ActorDetails.halfDimensions), ActorDetails.dimensions))
      .exists(player => {
        entities.get("Ghost", "Actor")
          .map(s => (s._1, s._2("Actor").asInstanceOf[Actor]))
          .map(s => new FloatRect(Vector2f.sub(s._2.position, ActorDetails.halfDimensions), ActorDetails.dimensions))
          .exists(ghost => ghost.intersection(player) != null)
      })

    if (maze.pellets.countPellets == 0) {
      entities.get("Remote")
        .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
        .foreach(s => { entities.removeEntity(s._1); server.send(s._2, net.game.GameOver(true, gameTime)); })

      events.enqueue(event.game.HideScore)
      events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, server, events, maze)))
      kill()
    }
    else if (isCollision) {
      entities.get("Remote")
        .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
        .foreach(s => { entities.removeEntity(s._1); server.send(s._2, net.game.GameOver(false, gameTime)); })

      events.enqueue(event.game.HideScore)
      events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, server, events, maze)))
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

        events.enqueue(event.game.HideScore)
        events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, server, events, maze)))
        kill()
    }
  }
}
