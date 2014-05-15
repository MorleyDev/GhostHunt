package uk.co.morleydev.ghosthunt.model.component.menu

import org.jsfml.system.Vector2f

/** A text box component represents an editable text box GUI item that can have characters added or removed in order
  * to allow users to enter some data, which can be attached to an entity */
case class Text(position : Vector2f,
                size : Float,
                text : String)
