package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, game}
import uk.co.morleydev.ghosthunt.model.GameTime

class ClientLobbyController(client : Client, entities : EntityComponentStore, launchTime : GameTime)
  extends Controller(messages = Seq(game.AcceptJoinGameRequest.name, game.InformJoinedGame.name)) {

  client.send(game.JoinGameRequest(launchTime))

  override protected def onClientMessage(message: NetworkMessage, gameTime: GameTime): Unit = {
    message.name match {
      case game.AcceptJoinGameRequest.name =>

      case game.InformJoinedGame.name =>

    }
  }
}
