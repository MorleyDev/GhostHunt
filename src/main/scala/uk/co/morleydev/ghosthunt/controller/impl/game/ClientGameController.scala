package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.ContentFactory
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.model.{event, GameTime, net}
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.net.NetworkMessage
import uk.co.morleydev.ghosthunt.model.event.game
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}

/**
 * The game controller for the client reacts to state change messages from the server related to the ending of a game
 * due to win/lose conditions being met, or a return to the lobby due to a player disconnecting.
 *
 * @param content
 * @param events
 * @param entities
 * @param client
 * @param maze
 */
class ClientGameController(content : ContentFactory, events : EventQueue, entities : EntityComponentStore, client : Client, maze : Maze)
  extends Controller(messages = Seq(net.game.GameOver.name, net.game.ReturnToLobby.name)) {
  private val music = {
    val music = content.openMusic("game.ogg")
    if (music.isEmpty)
      throw new FileNotFoundException()
    music.get
  }

  private val deathSound = {
    val sound = content.loadSound("player_death.ogg")
    if (sound.isEmpty)
      throw new FileNotFoundException()
    sound.get
  }

  music.setLoop(true)
  music.play()

  protected override def onClientMessage(message : NetworkMessage, gameTime : GameTime) = {
    music.stop()
    kill()
    events.enqueue(game.DisableLocalActors)

    message.name match {
      case net.game.GameOver.name =>
        deathSound.play()
        events.enqueue(sys.CreateController(() => new ClientGameOverScreen(net.game.GameOver.extract(message), entities, content, events, client, maze)))
        events.enqueue(event.game.HideScore)

      case net.game.ReturnToLobby.name =>
        entities.get("Actor").foreach(e => entities.removeEntity(e._1))
        events.enqueue(sys.CreateController(() => new ClientLobbyController(entities, client, events, gameTime, content, maze)))
        events.enqueue(event.game.HideScore)
    }
  }
}
