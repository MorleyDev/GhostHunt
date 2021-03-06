package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.data.ContentFactory
import uk.co.morleydev.ghosthunt.view.View
import java.io.FileNotFoundException
import org.jsfml.graphics.{Color, Text, RenderTarget}
import uk.co.morleydev.ghosthunt.model.component.menu

/**
 *
 * The text view is responsible for drawing what text is currently on the screen to
 * allow for them to be used as a part of the Graphical User Interface for the game.
 *
 * @param entities
 * @param content
 */
class TextView(entities : EntityComponentStore, content : ContentFactory)
  extends View(height = 254) {

  private val defaultFont = {
    val font = content.loadFont("font.ttf")
    if (font.isEmpty)
      throw new FileNotFoundException()
    font.get
  }

  override def draw(drawable: RenderTarget): Unit = {
    entities.get("Text")
      .map(f => (f._1, f._2("Text").asInstanceOf[menu.Text]))
      .foreach(ec => {
      val text = new Text(ec._2.text, defaultFont)
      text.setPosition(ec._2.position)
      text.setCharacterSize(ec._2.size.asInstanceOf[Int])
      text.setColor(Color.YELLOW)
      drawable.draw(text)
    })
  }
}
