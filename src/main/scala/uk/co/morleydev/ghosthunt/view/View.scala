package uk.co.morleydev.ghosthunt.view

import org.jsfml.graphics.RenderTarget
import uk.co.morleydev.ghosthunt.util.Killable
import uk.co.morleydev.ghosthunt.model.event.Event

/**
 * A view represents the discrete pieces of rendering logic in the system
 *
 * @param height The height of a view is used to order the views for rendering. Higher views are drawn on top of
 *               views of a lower height
 * @param events The events sequence stores what local events the view can respond to
 */
class View(val height: Int = 0, events : Seq[String] = Seq[String]()) extends Killable {

  def handleEvent(event : Event) = {
    if ( events.contains(event.name) )
      onEvent(event)
  }

  def draw(drawable : RenderTarget) = { }

  protected def onEvent(event : Event) = { }
}
