package uk.co.morleydev.ghosthunt.data

import uk.co.morleydev.ghosthunt.model.InputConfiguration
import uk.co.morleydev.ghosthunt.data.event.EventQueue
import org.jsfml.window.Keyboard.Key
import uk.co.morleydev.ghosthunt.model.event.Event
import uk.co.morleydev.ghosthunt.model.event.sys._
import org.jsfml.window.Mouse.Button
import org.jsfml.system.Vector2f

/**
 * Maps input from SFML events to events that hold meaningful value to the game
 *
 * @param inputConfig
 * @param eventQueue
 */
class InputMapper(inputConfig : InputConfiguration, eventQueue : EventQueue) {

  val keyMap = Map[Key, Event](
    Key.valueOf(inputConfig.up) -> MoveLocalUp,
    Key.valueOf(inputConfig.down) -> MoveLocalDown,
    Key.valueOf(inputConfig.left) -> MoveLocalLeft,
    Key.valueOf(inputConfig.right) -> MoveLocalRight,
    Key.valueOf(inputConfig.activate) -> LocalActivate,
    Key.valueOf(inputConfig.cancel) -> LocalCancel
  )

  def apply(key: Key, isPressed: Boolean): Unit = {
    if (isPressed) {
      keyMap.get(key) match {
        case value: Some[Event] => eventQueue.enqueue(value.get)
        case None =>
      }
    }
  }

  def apply(character : Char): Unit = {
    eventQueue.enqueue(TextType(character))
  }

  def apply(mouse : Button, pos : Vector2f, isPressed: Boolean) : Unit = {
    if (!isPressed) {
      mouse match {
        case Button.LEFT => eventQueue.enqueue(LocalClick(pos))
        case Button.RIGHT =>
        case Button.MIDDLE =>
        case Button.XBUTTON1 =>
        case Button.XBUTTON2 =>
      }
    }
  }
}
