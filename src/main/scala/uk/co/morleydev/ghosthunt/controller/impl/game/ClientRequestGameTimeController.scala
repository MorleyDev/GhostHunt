package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, game}
import scala.concurrent.duration.Duration
import scala.concurrent.duration
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.event.EventQueue

/**
 * The client request game time controller will, every second the client is connected, emit a message asking for the
 * current game time from the server's point of view. It will latency correct this time, and update the local view of
 * the current game time if needed.
 *
 * @param client
 * @param events
 */
class ClientRequestGameTimeController(client : Client, events : EventQueue) extends Controller(messages = Seq(game.ResponseGameTime.name)) {

  private var nextPokeTimer = Duration(1, duration.SECONDS)

  override def update(gameTime: GameTime): Unit = if (client.isConnected) {

    nextPokeTimer = Duration(nextPokeTimer.toNanos - gameTime.deltaTime.toNanos, duration.NANOSECONDS)
    if ( nextPokeTimer < Duration(0, duration.SECONDS) ) {
      nextPokeTimer = Duration(1, duration.SECONDS)

      client.send(game.RequestGameTime(gameTime))
    }
  }

  override protected def onClientMessage(message: NetworkMessage, gameTime: GameTime): Unit = {
    message.name match {
      case game.ResponseGameTime.name =>
        val timeDiff = gameTime.totalTime - Duration(game.ResponseGameTime.extract(message), duration.NANOSECONDS)
        val newTime = message.time + timeDiff
        if (newTime != gameTime.totalTime)
          events.enqueue(sys.UpdateGameRunningTime((gameTime.totalTime, newTime)))
    }
  }
}
