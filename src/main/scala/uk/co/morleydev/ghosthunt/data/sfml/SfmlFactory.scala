package uk.co.morleydev.ghosthunt.data.sfml

import org.jsfml.graphics.RenderWindow
import org.jsfml.window.{WindowStyle, VideoMode}

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

    new RenderWindow(videoMode, "Ghost Hunt", windowStyle)
  }
}
