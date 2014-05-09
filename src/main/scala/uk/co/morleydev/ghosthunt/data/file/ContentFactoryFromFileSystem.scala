package uk.co.morleydev.ghosthunt.data.file

import java.io.{InputStream, FileInputStream, File}
import uk.co.morleydev.ghosthunt.util.using
import uk.co.morleydev.ghosthunt.data.ContentFactory

class ContentFactoryFromFileSystem extends ContentFactory {

  protected override def loadFile[T](filename : String, handler : InputStream => Option[T]) : Option[T] = {
    val file = new File(filename)
    if (file.exists() && file.isFile)
      None
    else using(new FileInputStream(file)) {
      stream => handler(stream)
    }
  }
}
