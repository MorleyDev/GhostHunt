package uk.co.morleydev.ghosthunt.controller.impl

import uk.co.morleydev.ghosthunt.controller.Controller
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.GameTime
import uk.co.morleydev.ghosthunt.model.event.sys
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.data.store.EntityComponentStore
import uk.co.morleydev.ghosthunt.model.component.menu.MenuOption
import org.jsfml.graphics.FloatRect

class MenuOptionController(components : EntityComponentStore) extends Controller(events = Seq(sys.LocalClick.name)) {

  override protected def onEvent(event: Event, gameTime: GameTime): Unit = {
    event.name match {
      case sys.LocalClick.name =>
        onClick(event.data.asInstanceOf[Vector2f])
    }
  }

  private def onClick(pos : Vector2f) = {

    def isOverlap(position : Vector2f, size : Vector2f, depth : Int) : Boolean =
      new FloatRect(new Vector2f(position.x, position.y + size.y * depth), size).contains(pos)

    def isOverlapWithEntireBlock(position : Vector2f, size : Vector2f, depth : Int) : Boolean =
      new FloatRect(position, new Vector2f(size.x, size.y * depth)).contains(pos)

    components.get("MenuOption")
      .toSeq
      .map(ec => (ec._1, ec._2("MenuOption").asInstanceOf[MenuOption]))
      .filter(ec => isOverlapWithEntireBlock(ec._2.position, ec._2.size, ec._2.text.size))
      .foreach(ec => {
        (0 to ec._2.text.size-1).foreach(i => { if (isOverlap(ec._2.position, ec._2.size, i))
          components.link(ec._1, "MenuOption", ec._2.copy(active = i))
        })
      })
  }
}
