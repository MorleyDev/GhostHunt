package uk.co.morleydev.ghosthunt.controller.impl

import uk.co.morleydev.ghosthunt.data.event.EventQueue
import uk.co.morleydev.ghosthunt.data.store.{Maze, EntityComponentStore}
import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.{Event, sys}
import uk.co.morleydev.ghosthunt.model.component.menu.{Text, MenuOption, TextBox}
import uk.co.morleydev.ghosthunt.model.GameTime
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.controller.impl.game.ServerLobbyController
import uk.co.morleydev.ghosthunt.data.net.Server

/**
 * The server host controller is responsible for launching the low-level server with the port the user specifies
 *
 * @param events
 * @param entities
 * @param maze
 */
class ServerHostController(events : EventQueue, entities : EntityComponentStore, maze : Maze)
  extends Controller(events = Seq(sys.ServerHosting.name, sys.FailedHostServer.name)) {

  private val portBox = entities.createEntity()
  private val hostButton = entities.createEntity()

  private def initialise() = {
    entities.get(portBox).foreach(ec => entities.unlink(portBox, ec._1))
    entities.get(hostButton).foreach(ec => entities.unlink(hostButton, ec._1))

    entities.link(portBox, "TextBox", new TextBox(new Vector2f(10.0f, 50.0f), new Vector2f(620.0f, 32.0f), "Port",
      text = "8000",
      filter = _.isDigit,
      isActive = true))

    entities.link(hostButton, "MenuOption", new MenuOption(new Vector2f(160.0f, 100.0f), new Vector2f(300.0f, 48.0f), Seq("Host")))
  }

  initialise()

  override def update(gameTime: GameTime): Unit = {
    val hostButtonMenu = entities.get(hostButton).get("MenuOption")
    if (hostButtonMenu.isDefined && hostButtonMenu.get.asInstanceOf[MenuOption].active >= 0) {

      val port = entities.get(portBox)("TextBox").asInstanceOf[TextBox].text.toInt
      entities.unlink(portBox, "TextBox")
      entities.unlink(hostButton, "MenuOption")

      entities.link(hostButton, "Text", new Text(new Vector2f(10.0f, 10.0f), 64.0f, "Launching..."))

      events.enqueue(sys.HostServer(port))
    }
  }

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    event.name match {
      case sys.ServerHosting.name =>
        entities.removeEntity(portBox)
        entities.removeEntity(hostButton)
        events.enqueue(sys.CreateController(() => new ServerLobbyController(entities, event.data.asInstanceOf[Server], events, maze)))
        kill()

      case sys.FailedHostServer.name =>
        initialise()
    }
  }
}