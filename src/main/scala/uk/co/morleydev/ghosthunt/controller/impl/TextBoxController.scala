package uk.co.morleydev.ghosthunt.controller.impl

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.{Event, sys}
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.menu.TextBox
import org.jsfml.system.Vector2f
import org.jsfml.graphics.FloatRect

/**
 * The text box controller is responsible for updating the text boxes in the system to allow for text to be entered
 * via the keyboard
 *
 * @param entities
 */
class TextBoxController(entities : EntityComponentStore) extends Controller(events = Seq(sys.LocalClick.name, sys.TextType.name)){

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    event.name match {
      case sys.LocalClick.name => onClick(event.data.asInstanceOf[Vector2f])
      case sys.TextType.name => onType(event.data.asInstanceOf[Char])
    }
  }

  private def onClick(pos : Vector2f) : Unit = {
    val clickedMisses = entities.get("TextBox")
      .map(f => (f._1, f._2("TextBox").asInstanceOf[TextBox]))
      .partition(f => new FloatRect(f._2.position, f._2.size).contains(pos))

    clickedMisses._1.foreach(f => entities.link(f._1, "TextBox", f._2.copy(isActive = true)))
    clickedMisses._2.foreach(f => entities.link(f._1, "TextBox", f._2.copy(isActive = false)))
  }

  private def onType(c : Char) : Unit = {
    entities.get("TextBox")
      .map(f => (f._1, f._2("TextBox").asInstanceOf[TextBox]))
      .filter(_._2.isActive)
      .foreach(ec => {

      val newText = if (c == '\b')
        ec._2.text.dropRight(1)
      else if (ec._2.filter(c))
        ec._2.text + c
      else
        ec._2.text

      entities.link(ec._1, "TextBox", ec._2.copy(text = newText))
    })
  }
}
