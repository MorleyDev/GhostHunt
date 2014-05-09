package uk.co.morleydev.ghosthunt.controller

import uk.co.morleydev.ghosthunt.data.event.Event
import uk.co.morleydev.ghosthunt.data.net.{ClientId, NetworkMessage}
import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.util.Killable
import uk.co.morleydev.ghosthunt.model.GameTime

abstract class Controller(events : Seq[String] = Seq[String](),
                          messages : Seq[String] = Seq[String]()) extends Killable {

  def handleEvent(event : Event, gameTime : GameTime) = {
    if ( events.contains(event.name) )
      onEvent(event, gameTime)
  }

  def handleClientMessage(message : NetworkMessage, gameTime : GameTime) = {
    if ( messages.contains(message.name) )
      onClientMessage(message, gameTime)
  }

  def handleServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = {
    if ( messages.contains(message.name) )
      onServerMessage(client, message, gameTime)
  }

  def update(gameTime : GameTime) = { }

  protected def onEvent(event : Event, gameTime : GameTime) = { }

  protected def onClientMessage(message : NetworkMessage, gameTime : GameTime) = { }

  protected def onServerMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) = { }
}
