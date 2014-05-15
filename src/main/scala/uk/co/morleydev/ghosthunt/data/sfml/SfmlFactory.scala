package uk.co.morleydev.ghosthunt.data.sfml

import org.jsfml.graphics.{FloatRect, View, RenderWindow}
import org.jsfml.window.{WindowStyle, VideoMode}
import org.jsfml.system.Vector2f

object SfmlFactory {
  def create(fullscreen : Boolean, width : Int, height : Int) : RenderWindow = {

    val videoMode = {
      if (fullscreen) {
        val matchingMode = VideoMode.getFullscreenModes.find(mode => mode.width == width && mode.height == height)
        if (matchingMode.isDefined)
          matchingMode.get
        else
          VideoMode.getDesktopMode
      } else if (VideoMode.getDesktopMode.width > width && VideoMode.getDesktopMode.height > height)
        new VideoMode(width, height)
      else
        new VideoMode(VideoMode.getDesktopMode.width / 2, VideoMode.getDesktopMode.height / 2)
    }

    val windowStyle = if (fullscreen) WindowStyle.FULLSCREEN else WindowStyle.TITLEBAR | WindowStyle.CLOSE

    val window = new RenderWindow(videoMode, "Ghost Hunt", windowStyle)
    window.setView(new View(new FloatRect(new Vector2f(0.0f, 0.0f), new Vector2f(640.0f, 480.0f))))
    window
  }
}
