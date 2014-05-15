package uk.co.morleydev.ghosthunt.model.net

import uk.co.morleydev.ghosthunt.model.GameTime

package object game {
  class BaseCreateNetworkMessageWithParam[T: Manifest](final val name : String) {
    def apply(data : T, time : GameTime) : NetworkMessage =
      NetworkMessage(name, data, time)

    def extract(msg : NetworkMessage) : T =
      NetworkMessage.extract[T](msg)
  }

  class BaseCreateNetworkMessage(final val name : String) {
    def apply(time : GameTime) : NetworkMessage =
      NetworkMessage(name, time)
  }

  val JoinGameRequest = new BaseCreateNetworkMessage("JoinGameRequest")

  val AcceptJoinGameRequest = new BaseCreateNetworkMessageWithParam[AcceptJoinGameRequest]("AcceptJoinGameRequest")
  val InformJoinedGame = new BaseCreateNetworkMessageWithParam[(Int, String)]("InformJoinedGame")
  val InformLeftGame = new BaseCreateNetworkMessageWithParam[String]("InformLeftGame")
  val MoveRemoteActorOnServer = new BaseCreateNetworkMessageWithParam[(Int, Float, Float)]("MoveRemoteActorOnServer")
  val MoveRemoteActorOnClient = new BaseCreateNetworkMessageWithParam[(String, Int, Float, Float)]("MoveRemoteActorOnClient")

  val StartGame = new BaseCreateNetworkMessage("StartGame")
  val GameOver = new BaseCreateNetworkMessage("GameOver")
  val ReturnToLobby = new BaseCreateNetworkMessage("ReturnToLobby")

  val Disconnected = new BaseCreateNetworkMessage("Disconnected")
  val Poke = new BaseCreateNetworkMessage("Poke")
}
