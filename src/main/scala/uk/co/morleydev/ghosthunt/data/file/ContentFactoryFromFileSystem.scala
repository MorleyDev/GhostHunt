package uk.co.morleydev.ghosthunt.data.file

import java.io.{InputStream, FileInputStream, File}
import uk.co.morleydev.ghosthunt.util.using
import uk.co.morleydev.ghosthunt.data.ContentFactory

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

class ContentFactoryFromResource extends ContentFactory {

  protected override def loadFile[T](filename: String, handler: InputStream => Option[T]): Option[T] = {
    val stream = getClass.getResourceAsStream("/" + filename)
    if (stream != null && stream.available() > 0)
      handler(stream)
    else
      None
  }
}
