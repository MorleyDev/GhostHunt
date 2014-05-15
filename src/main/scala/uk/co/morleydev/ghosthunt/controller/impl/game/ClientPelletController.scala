package uk.co.morleydev.ghosthunt.controller.impl.game

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.net.NetworkMessage
import uk.co.morleydev.ghosthunt.model.net.game
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.Maze
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException

/**
 * The client pellet controller reacts to messages from the server telling the client a pellet
 * has been collected, removing that pellet from the maze.
 *
 * @param maze
 * @param content
 */
class ClientPelletController(maze : Maze, content : ContentFactory) extends Controller(messages = Seq(game.RemovePellet.name)) {

  val beep = {
    val beep = content.loadSound("beep.ogg")
    if (beep.isEmpty)
      throw new FileNotFoundException()
    beep.get
  }

  override protected def onClientMessage(message: NetworkMessage, gameTime: GameTime): Unit = {
    message.name match {
      case game.RemovePellet.name =>
        val xy = game.RemovePellet.extract(message)
        maze.pellets.remove(xy._1, xy._2)
        beep.play()
    }
  }
}
