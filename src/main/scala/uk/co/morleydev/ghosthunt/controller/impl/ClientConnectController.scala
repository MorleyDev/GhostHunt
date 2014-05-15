package uk.co.morleydev.ghosthunt.controller.impl

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.net.Client
import uk.co.morleydev.ghosthunt.model.component.menu.{Text, MenuOption, TextBox}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.event.sys
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.controller.impl.game.ClientLobbyController
import uk.co.morleydev.ghosthunt.data.ContentFactory

/**
 * The client connect controller is responsible for launching the low-level client with the host and port the user
 * specifies.
 *
 * @param events
 * @param entities
 * @param content
 * @param maze
 */
class ClientConnectController(events : EventQueue, entities : EntityComponentStore, content : ContentFactory, maze : Maze)
  extends Controller(events = Seq(sys.ConnectedToServer.name, sys.FailedConnectToServer.name)) {

  private val hostBox = entities.createEntity()
  private val portBox = entities.createEntity()
  private val connectButton = entities.createEntity()

  private def initialise() = {
    entities.get(hostBox).foreach(ec => entities.unlink(hostBox, ec._1))
    entities.get(portBox).foreach(ec => entities.unlink(portBox, ec._1))
    entities.get(connectButton).foreach(ec => entities.unlink(connectButton, ec._1))

    entities.link(hostBox, "TextBox", new TextBox(new Vector2f(10.0f, 10.0f), new Vector2f(620.0f, 32.0f), "Host",
      isActive = true))


  entities.link(portBox, "TextBox", new TextBox(new Vector2f(10.0f, 50.0f), new Vector2f(620.0f, 32.0f), "Port",
      text = "8000",
      filter = _.isDigit))

    entities.link(connectButton, "MenuOption", new MenuOption(
      new Vector2f(160.0f, 100.0f),
      new Vector2f(300.0f, 48.0f),
      Seq("Connect")))
  }
  initialise()

  override def update(gameTime: GameTime): Unit = {
    val connectButtonMenu = entities.get(connectButton).get("MenuOption")
    if ( connectButtonMenu.isDefined && connectButtonMenu.get.asInstanceOf[MenuOption].active >= 0 ) {

      val host = entities.get(hostBox)("TextBox").asInstanceOf[TextBox].text
      val port = entities.get(portBox)("TextBox").asInstanceOf[TextBox].text.toInt

      entities.unlink(hostBox, "TextBox")
      entities.unlink(portBox, "TextBox")
      entities.unlink(connectButton, "MenuOption")

      entities.link(connectButton, "Text", new Text(new Vector2f(10.0f, 50.0f), 64.0f, "Connecting..."))

      events.enqueue(sys.ConnectToServer((host, port)))
    }
  }

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    event.name match {
      case sys.ConnectedToServer.name =>
        entities.removeEntity(hostBox)
        entities.removeEntity(portBox)
        entities.removeEntity(connectButton)
        events.enqueue(sys.CreateController(() => new ClientLobbyController(entities, event.data.asInstanceOf[Client], events, gameTime, content, maze)))
        kill()

      case sys.FailedConnectToServer.name =>
        initialise()
    }
  }
}
