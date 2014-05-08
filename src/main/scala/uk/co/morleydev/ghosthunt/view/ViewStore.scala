package uk.co.morleydev.ghosthunt.view

import scala.collection.parallel.mutable.ParSet
import uk.co.morleydev.ghosthunt.data.event.Event
import org.jsfml.graphics.RenderTarget

class ViewStore {
  private val views: ParSet[View] = ParSet[View]()

  def add(view: View): Unit =
    synchronized { views += view }

  def draw(target : RenderTarget): Unit = {
    val aliveDeadViews = synchronized { views.partition(_.isAlive) }
    aliveDeadViews._2.par.foreach(view => synchronized { views -= view })
    aliveDeadViews._1.seq.foreach(_.draw(target))
  }

  def onEvent(event: Event): Unit =
    synchronized { views.clone() }.par.foreach(_.handleEvent(event))
}