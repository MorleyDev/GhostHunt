package uk.co.morleydev.ghosthunt.data.file

import java.io.{InputStream, FileInputStream, File}
import uk.co.morleydev.ghosthunt.util.using
import uk.co.morleydev.ghosthunt.data.ContentFactory

/**
 * Handles loading content (textures, sounds and music) from the file system.
 */
class ContentFactoryFromFileSystem(root : String) extends ContentFactory {

  protected override def loadFile[T](filename : String, handler : InputStream => Option[T]) : Option[T] = {
    val file = new File(root, filename)
    if (file.exists() && file.isFile)
      using(new FileInputStream(file)) {
        stream => handler(stream)
      }
    else None
  }
}

