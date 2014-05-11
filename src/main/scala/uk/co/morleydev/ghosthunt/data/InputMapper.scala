package uk.co.morleydev.ghosthunt.data

import uk.co.morleydev.ghosthunt.model.InputConfiguration
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import org.jsfml.window.Keyboard.Key
import uk.co.morleydev.ghosthunt.model.event.Event

class InputMapper(inputConfig : InputConfiguration, eventQueue : EventQueue) extends ((Key, Boolean) => Unit) {

  val keyMap = Map[Key, Event](
    Key.valueOf(inputConfig.up) -> new Event("MoveLocalUp"),
    Key.valueOf(inputConfig.down) -> new Event("MoveLocalDown"),
    Key.valueOf(inputConfig.left) -> new Event("MoveLocalLeft"),
    Key.valueOf(inputConfig.right) -> new Event("MoveLocalRight"),
    Key.valueOf(inputConfig.activate) -> new Event("Activate"),
    Key.valueOf(inputConfig.cancel) -> new Event("Cancel")
  )

  override def apply(key: Key, isPressed: Boolean): Unit = {
    if (isPressed) {
      keyMap.get(key) match {
        case value: Some[Event] => eventQueue.enqueue(value.get)
        case None =>
      }
    }
  }
}
