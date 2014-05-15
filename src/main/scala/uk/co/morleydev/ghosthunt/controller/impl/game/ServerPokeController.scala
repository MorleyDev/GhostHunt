package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.net.Server
import scala.concurrent.duration.Duration
import scala.concurrent.duration
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.component.game.Remote
import uk.co.morleydev.ghosthunt.model.net.game
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore

class ServerPokeController(entities : EntityComponentStore, server : Server) extends Controller {

  private var nextPokeTimer = Duration(1, duration.SECONDS)

  override def update(gameTime: GameTime): Unit = if (server.isConnected) {

    nextPokeTimer = Duration(nextPokeTimer.toNanos - gameTime.deltaTime.toNanos, duration.NANOSECONDS)
    if ( nextPokeTimer < Duration(0, duration.SECONDS) ) {
      nextPokeTimer = Duration(1, duration.SECONDS)

      entities.get("Remote")
        .map(s => s._2("Remote").asInstanceOf[Remote].id)
        .foreach(s => server.send(s, game.Poke(gameTime)))
    }
  }
}
