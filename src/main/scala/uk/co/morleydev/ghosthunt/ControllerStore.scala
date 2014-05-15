package uk.co.morleydev.ghosthunt

import scala.collection.parallel.mutable.ParSet
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.net.{NetworkMessage, ClientId}

/**
 * The controller store contains the controllers, as well
 * as the logic for passing events and network messages to
 * controllers and updating the controllers
 */
class ControllerStore {
  private val controllers: ParSet[Controller] = ParSet[Controller]()

  def add(controller : Controller) : Unit =
    synchronized { controllers += controller }

  def update(gameTime : GameTime) : Unit = {
      val aliveDeadControllers = synchronized { controllers.partition(_.isAlive) }
      aliveDeadControllers._2.foreach(controller => synchronized { controllers -= controller })
      aliveDeadControllers._1.seq.foreach(_.update(gameTime))
  }

  def onEvent(event : Event, gameTime : GameTime) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleEvent(event, gameTime))

  /**
   * Called when the client receives a message from the server
   * @param message
   * @param gameTime
   */
  def onClientReceiveMessage(message : NetworkMessage, gameTime : GameTime) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleClientMessage(message, gameTime))

  /**
   * Called when the server receives a message from a client
   *
   * @param client
   * @param message
   * @param gameTime
   */
  def onServerReceiveMessage(client : ClientId, message : NetworkMessage, gameTime : GameTime) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleServerMessage(client, message, gameTime))
}
