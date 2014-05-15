package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId, game}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.net.Server

class ServerResponseGameTimeController(server : Server) extends Controller(messages = Seq(game.RequestGameTime.name)) {
  override protected def onServerMessage(client: ClientId, message: NetworkMessage, gameTime: GameTime): Unit =
    if (server.isConnected) {
      message.name match {
        case game.RequestGameTime.name =>
          server.send(client, game.ResponseGameTime(message.time.toNanos, gameTime))
      }
    }
}
