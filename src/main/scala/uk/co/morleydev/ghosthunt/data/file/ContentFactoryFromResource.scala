package uk.co.morleydev.ghosthunt.data.file

import uk.co.morleydev.ghosthunt.data.ContentFactory
import java.io.InputStream

/**
 * Handles loading content (textures, sounds and music) from the resources for the java project so that they can be
 * shaded into the jar safely.
 */
class ContentFactoryFromResource extends ContentFactory {

  protected override def loadFile[T](filename: String, handler: InputStream => Option[T]): Option[T] = {
    val stream = getClass.getResourceAsStream("/" + filename)
    if (stream != null && stream.available() > 0)
      handler(stream)
    else
      None
  }
}
