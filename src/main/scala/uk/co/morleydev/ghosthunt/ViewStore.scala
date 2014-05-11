package uk.co.morleydev.ghosthunt

import org.jsfml.graphics.RenderTarget
import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.model.event.Event

class ViewStore {
  private var views: Seq[View] = Seq[View]()

  def add(view: View): Unit =
    synchronized { views = (views ++ Seq(view)).sortBy(_.height) }

  def draw(target : RenderTarget): Unit = {
    val aliveViews = synchronized { views.filter(_.isAlive) }
    aliveViews.seq.foreach(_.draw(target))
    synchronized { views = aliveViews }
  }

  def onEvent(event: Event): Unit =
    synchronized { views.filter(_.isAlive) }.par.foreach(_.handleEvent(event))
}
