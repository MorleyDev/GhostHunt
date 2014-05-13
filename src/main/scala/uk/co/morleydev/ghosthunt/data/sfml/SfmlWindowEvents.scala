package uk.co.morleydev.ghosthunt.data.sfml

import org.jsfml.window.event.Event
import org.jsfml.window.event.Event.Type
import org.jsfml.window.Keyboard.Key
import org.jsfml.system.Vector2f
import org.jsfml.window.Mouse.Button
import org.jsfml.graphics.RenderWindow

class SfmlWindowEvents(window : RenderWindow) {
  private var closeHandlers = Seq[() => Unit]()
  private var focusHandlers = Seq[Boolean => Unit]()
  private var keyHandlers = Seq[(Key, Boolean) => Unit]()
  private var textHandlers = Seq[Char => Unit]()
  private var mouseHandlers = Seq[(Button, Vector2f, Boolean) => Unit]()
  
  def onClosed(handler : () => Unit) : Unit = 
    closeHandlers = closeHandlers ++ Seq(handler)

  def onChangeFocus(handler : (Boolean) => Unit) : Unit =
    focusHandlers = focusHandlers ++ Seq(handler)

  def onKey(handler : (Key, Boolean) => Unit) : Unit =
    keyHandlers = keyHandlers ++ Seq(handler)

  def onText(handler : Char => Unit) : Unit =
    textHandlers = textHandlers ++ Seq(handler)

  def onMouse(handler : (Button, Vector2f, Boolean) => Unit) : Unit =
    mouseHandlers = mouseHandlers ++ Seq(handler)

  def invoke(e : Event) {
    e.`type` match {
      case Type.CLOSED =>
        closeHandlers.foreach(f => f.apply())

      case Type.LOST_FOCUS =>
        focusHandlers.foreach(f => f.apply(false))
      case Type.GAINED_FOCUS =>
        focusHandlers.foreach(f => f.apply(true))

      case Type.TEXT_ENTERED =>
        textHandlers.foreach(_.apply(e.asTextEvent().character))

      case Type.KEY_PRESSED =>
        keyHandlers.foreach(f => f.apply(e.asKeyEvent().key, true))
      case Type.KEY_RELEASED =>
        keyHandlers.foreach(f => f.apply(e.asKeyEvent().key, false))

      case Type.MOUSE_BUTTON_PRESSED =>
        mouseHandlers.foreach(f =>
          f.apply(e.asMouseButtonEvent().button, window.mapPixelToCoords(e.asMouseButtonEvent().position), false))
      case Type.MOUSE_BUTTON_RELEASED =>
        mouseHandlers.foreach(f =>
          f.apply(e.asMouseButtonEvent().button, window.mapPixelToCoords(e.asMouseButtonEvent().position), true))

      case Type.MOUSE_WHEEL_MOVED =>
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
