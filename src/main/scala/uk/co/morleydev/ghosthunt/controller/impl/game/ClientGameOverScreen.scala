package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.data.ContentFactory
import uk.co.morleydev.ghosthunt.model.component.menu.{MenuOption, Text}
import org.jsfml.system.Vector2f
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.event.sys
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.net.Client

/**
 * The client game over screen displays to the players the results of a finished game, whether or not the ghosts or hero
 * won the game. It also creates and watches the menu option to return to the lobby.
 *
 * @param isHeroVictory
 * @param entities
 * @param content
 * @param events
 * @param client
 * @param maze
 */
class ClientGameOverScreen(val isHeroVictory : Boolean, entities : EntityComponentStore, content : ContentFactory, events : EventQueue, client : Client, maze : Maze) extends Controller {

  private val music = {
    val music = content.openMusic("endgame.ogg")
    if (music.isEmpty)
      throw new FileNotFoundException()
    music.get
  }
  music.setLoop(true)
  music.play()

  val victoryTextEntity = entities.createEntity()
  if (isHeroVictory) {
    entities.link(victoryTextEntity, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "The hero was\nVictorious!"))
    entities.get("Ghost").foreach(s => entities.removeEntity(s._1))
  } else {
    entities.link(victoryTextEntity, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "The hero failed.\nGo Ghosts!"))
    entities.get("Player").foreach(s => entities.removeEntity(s._1))
  }

  val returnToLobbyButton = entities.createEntity()
  entities.link(returnToLobbyButton, "MenuOption", new MenuOption(new Vector2f(10.0f, 300.0f), new Vector2f(200.0f, 40.0f), Seq("Return To Lobby")))

  override def update(gameTime: GameTime): Unit = {
    if (entities.get(returnToLobbyButton)("MenuOption").asInstanceOf[MenuOption].active >= 0) {
      kill()
      music.stop()
      entities.get("Actor").foreach(s => entities.removeEntity(s._1))
      entities.removeEntity(victoryTextEntity)
      entities.removeEntity(returnToLobbyButton)
      events.enqueue(sys.CreateController(() => new ClientLobbyController(entities, client, events, gameTime, content, maze)))
    }
  }
}
