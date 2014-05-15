package uk.co.morleydev.ghosthunt.view.impl

import uk.co.morleydev.ghosthunt.view.View
import uk.co.morleydev.ghosthunt.data.store.Maze
import org.jsfml.graphics.{Color, Text, RenderTarget}
import java.io.FileNotFoundException
import uk.co.morleydev.ghosthunt.data.ContentFactory
import org.jsfml.system.Vector2f
import uk.co.morleydev.ghosthunt.model.event.{Event, game}

class ScoreView(content : ContentFactory, maze : Maze)
  extends View(height = 254, events = Seq(game.ShowScore.name, game.HideScore.name)) {

  private var showScore = false

  private val defaultFont = {
    val font = content.loadFont("font.ttf")
    if (font.isEmpty)
      throw new FileNotFoundException()
    font.get
  }


  override protected def onEvent(event: Event): Unit = {
    event.name match {
      case game.ShowScore.name =>
        showScore = true

      case game.HideScore.name =>
        showScore = false
    }
  }

  override def draw(drawable: RenderTarget): Unit = if (showScore) {
    val foundPellets = maze.pellets.totalPellets - maze.pellets.countPellets
    val text = new Text("Pellets Found: %s/%s".format(foundPellets, maze.pellets.totalPellets), defaultFont)
    text.setPosition(new Vector2f(5.0f, 5.0f))
    text.setCharacterSize(18)
    text.setColor(Color.YELLOW)
    drawable.draw(text)
  }
}
