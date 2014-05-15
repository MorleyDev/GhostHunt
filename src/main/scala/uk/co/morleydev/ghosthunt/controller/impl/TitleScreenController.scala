package uk.co.morleydev.ghosthunt.controller.impl

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.model.component.menu.{Text, MenuOption}
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.event.sys

class TitleScreenController(events : EventQueue, entities : EntityComponentStore) extends Controller {
  private val menuOptions = entities.createEntity()
  private val titleText = entities.createEntity()

  entities.link(menuOptions, "MenuOption",
               new MenuOption(new Vector2f(200.0f, 100.0f),
                              new Vector2f(200.0f, 48.0f), Seq("Connect", "Host", "Quit")))

  entities.link(titleText, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "Ghost-Hunt"))

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = super.onEvent(event, gameTime)

  override def update(gameTime: GameTime): Unit = {
    val menu = entities.get(menuOptions)("MenuOption").asInstanceOf[MenuOption]
    if (menu.active >= 0) {
      menu.active match {
        case 0 => onConnect()
        case 1 => onHost()
        case 2 => onQuit()
      }
      closeController()
    }
  }

  private def onConnect() {
    events.enqueue(sys.CreateController(() => new ClientConnectController(events, entities)))
  }

  private def onHost() {
    events.enqueue(sys.CreateController(() => new ServerHostController(events, entities)))
  }

  private def onQuit() {
    events.enqueue(sys.CloseGame)
  }

  private def closeController() {
    entities.removeEntity(menuOptions)
    entities.removeEntity(titleText)
    kill()
  }
}
