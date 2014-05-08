package uk.co.morleydev.ghosthunt.controller

import uk.co.morleydev.ghosthunt.data.event.Event
import uk.co.morleydev.ghosthunt.data.net.{ClientId, NetworkMessage}
import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.util.Killable

abstract class Controller(events : Seq[String] = Seq[String](),
                          messages : Seq[String] = Seq[String]()) extends Killable {

  def handleEvent(event : Event) = {
    if ( events.contains(event.name) )
      onEvent(event)
  }

  def handleClientMessage(message : NetworkMessage) = {
    if ( messages.contains(message.name) )
      onClientMessage(message)
  }

  def handleServerMessage(client : ClientId, message : NetworkMessage) = {
    if ( messages.contains(message.name) )
      onServerMessage(client, message)
  }

  def update(dt : Duration) = { }

  protected def onEvent(event : Event) = { }

  protected def onClientMessage(message : NetworkMessage) = { }

  protected def onServerMessage(client : ClientId, message : NetworkMessage) = { }
}
