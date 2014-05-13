package uk.co.morleydev.ghosthunt.model.component.menu

import org.jsfml.system.Vector2f

case class MenuOption(position : Vector2f, size : Vector2f, text : Seq[String], active : Int = -1)
