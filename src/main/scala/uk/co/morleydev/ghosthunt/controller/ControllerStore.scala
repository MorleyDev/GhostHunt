package uk.co.morleydev.ghosthunt.controller

import scala.concurrent.duration.Duration
import uk.co.morleydev.ghosthunt.data.event.Event
import uk.co.morleydev.ghosthunt.data.net.{ClientId, NetworkMessage}
import scala.collection.parallel.mutable.ParSet

class ControllerStore {
  private val controllers: ParSet[Controller] = ParSet[Controller]()

  def add(controller : Controller) : Unit =
    synchronized { controllers += controller }

  def update(dt : Duration) : Unit = {
      val aliveDeadControllers = synchronized { controllers.partition(_.isAlive) }
      aliveDeadControllers._2.par.foreach(controller => synchronized { controllers -= controller })
      aliveDeadControllers._1.par.foreach(_.update(dt))
  }

  def onEvent(event : Event) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleEvent(event))

  def onClientMessage(message : NetworkMessage) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleClientMessage(message))

  def onServerMessage(client : ClientId, message : NetworkMessage) : Unit =
    synchronized { controllers.clone() }.par.foreach(_.handleServerMessage(client, message))
}
