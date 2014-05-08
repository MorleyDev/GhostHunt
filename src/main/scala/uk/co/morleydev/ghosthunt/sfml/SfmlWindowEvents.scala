package uk.co.morleydev.ghosthunt.sfml

import org.jsfml.window.event.Event
import org.jsfml.window.event.Event.Type
import org.jsfml.window.Keyboard.Key

class SfmlWindowEvents {
  private var closeHandlers = Seq[() => Unit]()
  private var focusHandlers = Seq[Boolean => Unit]()
  private var keyHandlers = Seq[(Key, Boolean) => Unit]()
  
  def onClosed(handler : () => Unit) : Unit = 
    closeHandlers = closeHandlers ++ Seq[() => Unit](handler)

  def onChangeFocus(handler : (Boolean) => Unit) : Unit =
    focusHandlers = focusHandlers ++ Seq[Boolean => Unit](handler)

  def onKey(handler : (Key, Boolean) => Unit) : Unit =
    keyHandlers = keyHandlers ++ Seq[(Key, Boolean) => Unit](handler)

  def invoke(e : Event) {
    e.`type` match {
      case Type.CLOSED => closeHandlers.foreach(f => f.apply())
      case Type.LOST_FOCUS => focusHandlers.foreach(f => f.apply(false))
      case Type.GAINED_FOCUS => focusHandlers.foreach(f => f.apply(true))
      case Type.KEY_PRESSED => keyHandlers.foreach(f => f.apply(e.asKeyEvent().key, true))
      case Type.KEY_RELEASED => keyHandlers.foreach(f => f.apply(e.asKeyEvent().key, false))
      case Type.MOUSE_WHEEL_MOVED =>
      case Type.MOUSE_BUTTON_PRESSED =>
      case Type.MOUSE_BUTTON_RELEASED =>
      case Type.MOUSE_MOVED =>
      case Type.MOUSE_ENTERED =>
      case Type.MOUSE_LEFT =>
      case Type.JOYSTICK_BUTTON_PRESSED =>
      case Type.JOYSTICK_BUTTON_RELEASED =>
      case Type.JOYSTICK_MOVED =>
      case Type.JOYSTICK_CONNECETED =>
      case Type.JOYSTICK_DISCONNECTED =>
      case _ =>
    }
  }
}
