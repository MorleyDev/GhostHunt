package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.ContentFactory
import uk.co.morleydev.ghosthunt.model.component.menu.{MenuOption, Text}
import org.jsfml.system.Vector2f
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.net.Client

class ClientGameOverScreen(val isPlayerVictory : Boolean, entities : EntityComponentStore, content : ContentFactory, events : EventQueue, client : Client) extends Controller {

  private val music = {
    val music = content.openMusic("resource/endgame.ogg")
    if (music.isEmpty)
      throw new FileNotFoundException()
    music.get
  }
  music.setLoop(true)
  music.play()

  val victoryTextEntity = entities.createEntity()
  if (isPlayerVictory)
    entities.link(victoryTextEntity, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "The player was\nVictorious!"))
  else
    entities.link(victoryTextEntity, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "The player failed.\nGo Ghosts!"))

  val returnToLobbyButton = entities.createEntity()
  entities.link(returnToLobbyButton, "MenuOption", new MenuOption(new Vector2f(10.0f, 300.0f), new Vector2f(200.0f, 40.0f), Seq("Return To Lobby")))

  override def update(gameTime: GameTime): Unit = {
    if (entities.get(returnToLobbyButton)("MenuOption").asInstanceOf[MenuOption].active >= 0) {
      kill()
      music.stop()
      entities.get("Actor").foreach(s => entities.removeEntity(s._1))
      entities.removeEntity(victoryTextEntity)
      entities.removeEntity(returnToLobbyButton)
      events.enqueue(sys.CreateController(() => new ClientLobbyController(entities, client, events, gameTime, content)))
    }
  }
}
