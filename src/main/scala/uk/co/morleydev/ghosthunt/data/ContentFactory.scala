package uk.co.morleydev.ghosthunt.data

import org.jsfml.graphics.{Font, Texture}
import org.jsfml.audio.{SoundBuffer, Music}
import java.io.{IOException, InputStream}
import uk.co.morleydev.ghosthunt.util.using
import scala.io.Source

trait ContentFactory {
  def loadTexture(filename: String): Option[Texture] =
    loadFile[Texture](filename, input => {
      try {
        val texture = new Texture()
        texture.loadFromStream(input)
        Some(texture)
      } catch {
        case e : IOException => None
      }
    })

  def openMusic(filename: String): Option[Music] =
    loadFile[Music](filename, input => {
      try {
        val music = new Music()
        music.openFromStream(input)
        Some(music)
      } catch {
        case e : IOException => None
      }
    })

  def loadSoundBuffer(filename: String): Option[SoundBuffer] =
    loadFile[SoundBuffer](filename, input => {
      try {
        val buffer = new SoundBuffer()
        buffer.loadFromStream(input)
        Some(buffer)
      } catch {
        case e : IOException => None
      }
    })

  def loadFont(filename: String) : Option[Font] =
    loadFile[Font](filename, input => {
      try {
        val font = new Font()
        font.loadFromStream(input)
        Some(font)
      } catch {
        case e: IOException => None
      }
    })

  def loadString(filename : String) : Option[String] =
    loadFile[String](filename, stream =>
      using(Source.fromInputStream(stream)) {
        source => Some(source.getLines().mkString("\n"))
      })

  protected def loadFile[T](filename : String, handler : InputStream => Option[T]) : Option[T]
}