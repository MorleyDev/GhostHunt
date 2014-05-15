package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.net.Server

/**
 * The server response game timer reacts to requests for the servers current time by replying with the servers current
 * time and the original time the client sent the message, to allow for the client to adjust it's own clock accordingly
 *
 * @param server
 */
class ServerResponseGameTimeController(server : Server) extends Controller(messages = Seq(game.RequestGameTime.name)) {
  override protected def onServerMessage(client: ClientId, message: NetworkMessage, gameTime: GameTime): Unit =
    if (server.isConnected) {
      message.name match {
        case game.RequestGameTime.name =>
          server.send(client, game.ResponseGameTime(message.time.toNanos, gameTime))
      }
    }
}
