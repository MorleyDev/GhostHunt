package uk.co.morleydev.ghosthunt.model.component.menu

import org.jsfml.system.Vector2f

/**
 * A text box component represents an editable text box GUI item that can have characters added or removed in order
 * to allow users to enter some data, which can be attached to an entity,
 *
 * @param position
 * @param size
 * @param name
 * @param text
 * @param filter
 * @param isActive
 */
case class TextBox(position : Vector2f,
                   size : Vector2f,
                   name : String,
                   text : String = "",
                   filter : (Char => Boolean) = !_.isControl,
                   isActive : Boolean = false)

