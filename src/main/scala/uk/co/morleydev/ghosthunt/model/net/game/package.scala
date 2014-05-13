package uk.co.morleydev.ghosthunt.model.net

import uk.co.morleydev.ghosthunt.model.GameTime

package object game {
  class BaseCreateNetworkMessageWithParam[T: Manifest](final val name : String) {
    def apply(data : T, time : GameTime) : NetworkMessage =
      NetworkMessage(name, data, time)
  }

  class BaseCreateNetworkMessage(final val name : String) {
    def apply(time : GameTime) : NetworkMessage =
      NetworkMessage(name, time)
  }

  val JoinGameRequest = new BaseCreateNetworkMessage("JoinGameRequest")

  val AcceptJoinGameRequest = new BaseCreateNetworkMessageWithParam[AcceptJoinGameRequest]("AcceptJoinGameRequest")
  val InformJoinedGame = new BaseCreateNetworkMessageWithParam[(Int, ClientId)]("InformJoinedGame")

  val StartGameRequest = new BaseCreateNetworkMessage("StartGameRequest")
}
