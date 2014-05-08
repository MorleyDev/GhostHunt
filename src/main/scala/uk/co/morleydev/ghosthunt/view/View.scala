package uk.co.morleydev.ghosthunt.view

import uk.co.morleydev.ghosthunt.data.event.Event
import org.jsfml.graphics.RenderTarget
import uk.co.morleydev.ghosthunt.util.Killable

class View(events : Seq[String] = Seq[String]()) extends Killable {

  def handleEvent(event : Event) = {
    if ( events.contains(event.name) )
      onEvent(event)
  }

  def draw(drawable : RenderTarget) = { }

  protected def onEvent(event : Event) = { }
}
