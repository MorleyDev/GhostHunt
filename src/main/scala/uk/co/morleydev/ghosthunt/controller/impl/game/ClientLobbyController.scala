package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{ClientId, NetworkMessage, game}
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.model.event
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.model.component.game._
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.model.component.game.Actor
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.component.game.Ghost
import scala.concurrent.duration.Duration
import scala.concurrent.duration
import uk.co.morleydev.ghosthunt.model.component.menu.Text
import java.util.UUID
import uk.co.morleydev.ghosthunt.data.ContentFactory

/**
 * The client lobby controller is responsible for joining a game, and maintaining awareness of the current
 * actors in the game so when the game begins no entities are missing. It also reacts to commands to start the game,
 * enabling the ClientGameController when required.
 *
 * @param entities
 * @param client
 * @param events
 * @param gameTime
 * @param content
 * @param maze
 */
class ClientLobbyController(entities : EntityComponentStore,
                            client : Client,
                            events : EventQueue,
                            gameTime : GameTime,
                            content : ContentFactory,
                            maze : Maze)
  extends Controller(messages = Seq(game.AcceptJoinGameRequest.name,
                                    game.InformLeftGame.name,
                                    game.InformJoinedGame.name,
                                    game.StartGame.name)) {
  maze.pellets.reset()

  val waitingMessage = entities.createEntity()
  entities.link(waitingMessage, "Text", new Text(new Vector2f(10.0f, 10.0f), 32.0f, "Waiting for game to Start"))

  client.send(game.JoinGameRequest(gameTime))

  protected override def onClientMessage(message : NetworkMessage, gameTime : GameTime) = {
    message.name match {
      case game.AcceptJoinGameRequest.name =>
        val accept = game.AcceptJoinGameRequest.extract(message)
        val timeDiff = gameTime.totalTime - Duration(accept.clientTime, duration.NANOSECONDS)
        val newTime = message.time + timeDiff
        events.enqueue(sys.UpdateGameRunningTime((gameTime.totalTime, newTime)))

        val me = entities.createEntity()
        entities.link(me, "Local", new Local)
        // Link the player or ghost
        if (accept.id == -1) {
          entities.link(me, "Player", new Player)
          entities.link(me, "Actor", new Actor(new Vector2f(336.0f, 48.0f)))
        } else {
          entities.link(me, "Ghost", new Ghost(accept.id))
          entities.link(me, "Actor", new Actor(new Vector2f(336.0f, 272.0f)))
        }

      case game.InformJoinedGame.name =>
        val inform = game.InformJoinedGame.extract(message)
        val them = entities.createEntity()
        entities.link(them, "Remote", new Remote(new ClientId(UUID.fromString(inform._2))))

        // Link the player or ghost
        if (inform._1 == -1) {
          entities.link(them, "Player", new Player)
          entities.link(them, "Actor", new Actor(new Vector2f(336.0f, 48.0f)))
        } else {
          entities.link(them, "Ghost", new Ghost(inform._1))
          entities.link(them, "Actor", new Actor(new Vector2f(336.0f, 272.0f)))
        }

      case game.InformLeftGame.name =>
        val inform = new ClientId(UUID.fromString(game.InformLeftGame.extract(message)))
        entities.get("Remote")
          .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
          .filter(s => s._2 == inform)
          .foreach(s => entities.removeEntity(s._1))

      case game.StartGame.name =>
        entities.removeEntity(waitingMessage)
        events.enqueue(sys.CreateController(() => new ClientGameController(content, events, entities, client, maze)))
        events.enqueue(event.game.ShowScore)
        events.enqueue(event.game.EnableLocalActors)
        kill()
    }
  }
}
