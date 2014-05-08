package uk.co.morleydev.ghosthunt.data.file

import org.jsfml.graphics.Texture
import java.io.{InputStream, IOException, FileInputStream, File}
import uk.co.morleydev.ghosthunt.util.using
import org.jsfml.audio.{SoundBuffer, Music}
import scala.io.Source
import com.lambdaworks.jacks.JacksMapper

class ContentFactory {

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

  def loadString(filename : String) : Option[String] =
    loadFile[String](filename, stream =>
      using(Source.fromInputStream(stream)) {
        source => Some(source.getLines().mkString("\n"))
      })

  private def loadFile[T](filename : String, handler : InputStream => Option[T]) : Option[T] = {
    val file = new File(filename)
    if (file.exists() && file.isFile)
      None
    else using(new FileInputStream(file)) {
      stream => handler(stream)
    }
  }
}
