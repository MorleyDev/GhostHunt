package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{AcceptJoinGameRequest, NetworkMessage, ClientId, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.game.Remote
import uk.co.morleydev.ghosthunt.data.net.Server
import uk.co.morleydev.ghosthunt.model.store.EntityId
import uk.co.morleydev.ghosthunt.model.component.menu.Text
import org.jsfml.system.Vector2f

class ServerLobbyController(entities : EntityComponentStore, server : Server)
  extends Controller(messages = Seq[String](game.JoinGameRequest.name, game.StartGameRequest.name)) {

  private var connectedId = -1
  private val textWaiting = Array[EntityId](entities.createEntity(),
    entities.createEntity(),
    entities.createEntity(),
    entities.createEntity())

  (0 to 3).foreach(i => entities.link(textWaiting(i), "Text", new Text(new Vector2f(10.0f, 40.0f * i + 10.0f), 36.0f, "Waiting...")))

  protected override def onServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = {
    message.name match {
      case game.JoinGameRequest.name =>
        synchronized {
          val remote = entities.createEntity()
          entities.link(remote, "Remote", new Remote(client))

          val tuple = new AcceptJoinGameRequest(connectedId, message.time)
          server.send(client, game.AcceptJoinGameRequest(tuple, gameTime))

          entities.link(textWaiting(connectedId), "Text", new Text(new Vector2f(10.0f, 40.0f * connectedId + 10.0f), 36.0f, "Connected!"))
          connectedId = connectedId + 1
        }

      case game.StartGameRequest.name =>
    }
  }
}
