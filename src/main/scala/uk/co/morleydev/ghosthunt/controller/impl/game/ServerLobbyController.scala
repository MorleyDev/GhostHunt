package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{AcceptJoinGameRequest, NetworkMessage, ClientId, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.model.component.game.{Actor, Ghost, Player, Remote}
import uk.co.morleydev.ghosthunt.data.net.Server
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.model.store.EntityId
import uk.co.morleydev.ghosthunt.model.component.menu.{MenuOption, Text}
import org.jsfml.system.Vector2f
import java.util.concurrent.ConcurrentLinkedQueue
import uk.co.morleydev.ghosthunt.data.event.EventQueue

class ServerLobbyController(entities : EntityComponentStore, server : Server, events : EventQueue, maze : Maze)
  extends Controller(messages = Seq[String](game.JoinGameRequest.name, game.Disconnected.name)) {
  maze.pellets.reset()

  private val textWaiting = Map[Int, EntityId](-1 -> entities.createEntity(),
    0 -> entities.createEntity(),
    1 -> entities.createEntity(),
    2 -> entities.createEntity())

  private val entityQueue = new ConcurrentLinkedQueue[Int]()
  entityQueue.add(-1)
  entityQueue.add(0)
  entityQueue.add(1)
  entityQueue.add(2)

  private val startGameButton = entities.createEntity()

  (entities.get("Remote", "Ghost").map(s => s._2("Ghost").asInstanceOf[Ghost].id) ++ entities.get("Remote", "Player").map(s => -1))
    .foreach(id => entities.link(textWaiting(id), "Text", new Text(new Vector2f(10.0f, 40.0f * id + 50.0f), 36.0f, "Connected!")))

  (-1 to 2).foreach(i => entities.link(textWaiting(i), "Text", new Text(new Vector2f(10.0f, 40.0f * i + 50.0f), 36.0f, "Waiting...")))


  override def update(gameTime: GameTime): Unit = {
    entities.get(startGameButton).filter(_._1 == "MenuOption").map(_._2.asInstanceOf[MenuOption].active).filter(_ > -1).foreach(menu => {
      entities.removeEntity(startGameButton)
      textWaiting.map(_._2).foreach(id => entities.removeEntity(id))

      entities.get("Remote")
        .map(s => s._2("Remote").asInstanceOf[Remote].id)
        .foreach(s => server.send(s, game.StartGame(gameTime)))

      events.enqueue(sys.CreateController(() => new ServerGameController(entities, events, server, maze)))
      kill()
    })
  }

  protected override def onServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = {
    message.name match {
      case game.Disconnected.name => synchronized {
        entities.get("Remote")
          .map(s => (s._1, s._2("Remote").asInstanceOf[Remote].id))
          .filter(s => s._2 == client)
          .foreach(s => {
          if (entities.has(s._1, "Ghost")) {
            val id = entities.get(s._1)("Ghost").asInstanceOf[Ghost].id
            entityQueue.add(id)
            entities.link(textWaiting(id), "Text", new Text(new Vector2f(10.0f, 40.0f * id + 50.0f), 36.0f, "Waiting..."))
          }
          else {
            entities.link(textWaiting(-1), "Text", new Text(new Vector2f(10.0f, 40.0f * -1 + 50.0f), 36.0f, "Waiting..."))
            entityQueue.add(-1)
          }
          entities.removeEntity(s._1)
          entities.get("Remote")
            .map(s => s._2("Remote").asInstanceOf[Remote].id)
            .foreach(r => { println("Server (" + r.toString + "): " + s._2.toString()); server.send(r, game.InformLeftGame(s._2.value.toString, gameTime)) })

          if (entities.get("Ghost").size == 0 || entities.get("Player").size == 0)
            entities.unlink(startGameButton, "MenuOption")
        })
      }

      case game.JoinGameRequest.name => synchronized {
        val remote = entities.createEntity()
        entities.link(remote, "Remote", new Remote(client))
        val connectedId = entityQueue.poll()

        // Link the player or ghost
        if (connectedId == -1) {
          entities.link(remote, "Player", new Player)
          entities.link(remote, "Actor", new Actor(new Vector2f(336.0f, 48.0f)))
        } else {
          entities.link(startGameButton, "MenuOption", new MenuOption(new Vector2f(400.0f, 400.0f), new Vector2f(100.0f, 32.0f), Seq("Start Game")))
          entities.link(remote, "Ghost", new Ghost(connectedId))
          entities.link(remote, "Actor", new Actor(new Vector2f(336.0f, 272.0f)))
        }

        // Response with an accept join game request
        val tuple = new AcceptJoinGameRequest(connectedId, message.time.toNanos)
        server.send(client, game.AcceptJoinGameRequest(tuple, gameTime))

        // Get the entities that are not of this
        val otherEntities = entities.get("Remote", "Ghost")
          .map(s => (s._2("Remote").asInstanceOf[Remote].id, s._2("Ghost").asInstanceOf[Ghost].id))
          .filter(s => s._1 != client) ++ entities.get("Remote", "Player")
          .map(s => (s._2("Remote").asInstanceOf[Remote].id, -1))
          .filter(s => s._1 != client)

        // Tell other players this player joined
        otherEntities.foreach(s => server.send(s._1, game.InformJoinedGame((connectedId, client.value.toString), gameTime)))

        // Tell this player what other players exist
        otherEntities.foreach(s => server.send(client, game.InformJoinedGame((s._2, s._1.value.toString), gameTime)))

        // Record player as connected
        entities.link(textWaiting(connectedId), "Text", new Text(new Vector2f(10.0f, 40.0f * connectedId + 50.0f), 36.0f, "Connected!"))
      }
    }
  }
}
