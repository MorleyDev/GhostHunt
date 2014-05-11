package uk.co.morleydev.ghosthunt.view

import org.jsfml.graphics.RenderTarget
import uk.co.morleydev.ghosthunt.util.Killable
import uk.co.morleydev.ghosthunt.model.event.Event

class View(val height: Int = 0, events : Seq[String] = Seq[String]()) extends Killable {

  def handleEvent(event : Event) = {
    if ( events.contains(event.name) )
      onEvent(event)
  }

  def draw(drawable : RenderTarget) = { }

  protected def onEvent(event : Event) = { }
}
