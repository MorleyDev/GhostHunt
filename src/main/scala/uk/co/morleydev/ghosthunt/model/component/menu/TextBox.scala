package uk.co.morleydev.ghosthunt.model.component.menu

import org.jsfml.system.Vector2f

case class TextBox(position : Vector2f,
                   size : Vector2f,
                   name : String,
                   text : String = "",
                   filter : (Char => Boolean) = !_.isControl,
                   isActive : Boolean = false)

