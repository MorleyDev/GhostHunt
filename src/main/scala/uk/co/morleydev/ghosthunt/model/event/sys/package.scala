package uk.co.morleydev.ghosthunt.model.event

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.controller.Controller
import scala.concurrent.duration.Duration
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.data.net.{Client, Server}

package object sys {
  class BaseCreateEventWithParam[T](final val name : String) {
    def apply(data : T) : Event =
      new Event(name, data)
  }

  val CreateView = new BaseCreateEventWithParam[View]("CreateView")
  val CreateController = new BaseCreateEventWithParam[() => Controller]("CreateController")
  val UpdateGameRunningTime = new BaseCreateEventWithParam[Duration]("UpdateGameRunningTime")
  val CloseGame = new Event("CloseGame")

  val MoveLocalUp = new Event("MoveLocalUp")
  val MoveLocalDown = new Event("MoveLocalDown")
  val MoveLocalLeft = new Event("MoveLocalLeft")
  val MoveLocalRight = new Event("MoveLocalRight")
  val LocalActivate = new Event("LocalActivate")
  val LocalClick = new BaseCreateEventWithParam[Vector2f]("LocalClick")
  val LocalCancel = new Event("LocalCancel")
  val TextType = new BaseCreateEventWithParam[Char]("TextType")

  val ConnectToServer = new BaseCreateEventWithParam[(String, Int)]("ConnectToServer")
  val CheckConnectedToServer = new Event("CheckConnectedToServer")
  val ConnectedToServer = new BaseCreateEventWithParam[Client]("ConnectedToServer")
  val FailedConnectToServer = new Event("FailedConnectToServer")

  val HostServer = new BaseCreateEventWithParam[Int]("HostServer")
  val CheckServerHosting = new Event("CheckServerHosting")
  val ServerHosting = new BaseCreateEventWithParam[Server]("ServerHosting")
  val FailedHostServer = new Event("FailedHostServer")
}
