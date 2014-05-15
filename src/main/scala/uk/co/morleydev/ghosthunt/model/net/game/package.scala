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
  val InformJoinedGame = new BaseCreateNetworkMessageWithParam[(Int, String)]("InformJoinedGame") // actor Id (-1 = Player, 0,1,2 = Ghost)
  val InformLeftGame = new BaseCreateNetworkMessageWithParam[String]("InformLeftGame") // ClientId
  val MoveRemoteActorOnServer = new BaseCreateNetworkMessageWithParam[(Int, Float, Float)]("MoveRemoteActorOnServer") // Direction (0U,1D,2L,3R), xpos, ypos
  val MoveRemoteActorOnClient = new BaseCreateNetworkMessageWithParam[(String, Int, Float, Float)]("MoveRemoteActorOnClient") // ClientId, Direction (0U,1D,2L,3R), xpos, ypos

  val StartGame = new BaseCreateNetworkMessage("StartGame")
  val GameOver = new BaseCreateNetworkMessageWithParam[Boolean]("GameOver") // True = is player victory, false = is ghost
  val ReturnToLobby = new BaseCreateNetworkMessage("ReturnToLobby")

  val RemovePellet = new BaseCreateNetworkMessageWithParam[(Int,Int)]("RemovePellet")

  val Disconnected = new BaseCreateNetworkMessage("Disconnected")
  val Poke = new BaseCreateNetworkMessage("Poke")
}
