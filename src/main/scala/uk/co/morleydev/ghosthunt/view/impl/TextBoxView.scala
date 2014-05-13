package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import org.jsfml.graphics.{Color, Text, RenderTarget}
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.menu.TextBox
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException

class TextBoxView(entities : EntityComponentStore, content : ContentFactory) extends View {

  private val defaultFont = {
    val font = content.loadFont("resource/font.ttf")
    if (font.isEmpty)
      throw new FileNotFoundException()
    font.get
  }

  override def draw(drawable: RenderTarget): Unit = {
    entities.get("TextBox")
      .map(f => (f._1, f._2("TextBox").asInstanceOf[TextBox]))
      .foreach(ec => {
        val text = new Text(ec._2.name + ": " + ec._2.text, defaultFont)
        text.setPosition(ec._2.position)
        text.setCharacterSize(ec._2.size.y.asInstanceOf[Int])
        text.setColor(if (ec._2.isActive) Color.WHITE else new Color(100,100,100))
        drawable.draw(text)
      })
  }
}
