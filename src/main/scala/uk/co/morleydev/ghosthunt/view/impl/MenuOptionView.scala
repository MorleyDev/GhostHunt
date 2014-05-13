package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import org.jsfml.graphics.{Font, Color, Text, RenderTarget}
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.menu.MenuOption
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.FileNotFoundException

class MenuOptionView(entityComponentStore : EntityComponentStore,
                     content : ContentFactory) extends View {

  private val defaultFont = {
    val font = content.loadFont("resource/font.ttf")
    if (font.isEmpty)
      throw new FileNotFoundException()
    font.get
  }

  private def newPosition(pos : Vector2f, size: Vector2f, depth: Int) : Vector2f =
    new Vector2f(pos.x, pos.y + size.y * depth)

  override def draw(drawable: RenderTarget): Unit = {
    entityComponentStore.get("MenuOption")
      .map(ec => ec._2("MenuOption").asInstanceOf[MenuOption])
      .flatMap(menu =>(0 to menu.text.size-1)
                        .map(i => (i == menu.active,
                                   menu.text.toIndexedSeq.apply(i),
                                   newPosition(menu.position, menu.size, i),
                                   menu.size)))
      .foreach(s => {
      val text = new Text(s._2, defaultFont)

      text.setPosition(s._3)
      text.setCharacterSize(s._4.y.asInstanceOf[Int])
      text.setColor(if (s._1) Color.GREEN else Color.WHITE)
      drawable.draw(text)
    })
  }
}
