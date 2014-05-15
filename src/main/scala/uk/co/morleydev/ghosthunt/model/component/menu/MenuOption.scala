package uk.co.morleydev.ghosthunt.model.component.menu

import org.jsfml.system.Vector2f

/**
 * A menu option is a series of buttons, where one of those buttons can be selected.
 * That button will then become considered the active button.
 *
 * @param position
 * @param size
 * @param text
 * @param active
 */
case class MenuOption(position : Vector2f, size : Vector2f, text : Seq[String], active : Int = -1)
